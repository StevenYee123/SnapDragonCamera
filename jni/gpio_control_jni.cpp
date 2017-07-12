/*
Copyright (c) 2016, The Linux Foundation. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above
      copyright notice, this list of conditions and the following
      disclaimer in the documentation and/or other materials provided
      with the distribution.
    * Neither the name of The Linux Foundation nor the names of its
      contributors may be used to endorse or promote products derived
      from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT
ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

#include <jni.h>
#include <assert.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <poll.h>
#include <pthread.h>

#ifdef __ANDROID__
#include "android/log.h"
#define LOGE(...) __android_log_print( ANDROID_LOG_ERROR, "jethinr GpioControl", __VA_ARGS__ )
#endif

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jint JNICALL Java_com_android_camera_PhotoModule_GPIOInitialize
        (JNIEnv* env, jobject instance);

JNIEXPORT jint JNICALL Java_com_android_camera_PhotoModule_GPIODeInitialize
        (JNIEnv* env, jobject instance);
#ifdef __cplusplus
}
#endif

#define VALUE_MAX 30
 
#define IN  0
#define OUT 1
 
#define LOW  0
#define HIGH 1
 
#define PIN  84   /* J54 P13*/
#define POUT 124  /* J54 P11 */


typedef struct gpio_context {  
    pthread_t        gpio_thread_id;
    pthread_mutex_t  lock;	
    JavaVM*          javaVM;
	JNIEnv*          env;
    jclass           mainActivityClz;
    jobject          mainActivityObj;
	jmethodID        callBackID;
    jboolean         thread_active;
    struct pollfd    pfd;	
} GPIOContext;
GPIOContext g_ctx;

 
static int
GPIOWrite(int pin, int value)
{
	static const char s_values_str[] = "01";
 
	char path[VALUE_MAX];
	int fd;
 
	snprintf(path, VALUE_MAX, "/sys/class/gpio/gpio%d/value", pin);
	fd = open(path, O_WRONLY);
	if (-1 == fd) {
		LOGE("Failed to open gpio value for writing! %s \n", path);
		return(-1);
	}
 
	if (1 != write(fd, &s_values_str[LOW == value ? 0 : 1], 1)) {
		LOGE("Failed to write value! %s \n", path);
		return(-1);
	}
 
	close(fd);
	return(0);
}


static int
GPIORead(int pin)
{
	char path[VALUE_MAX];
	char value_str[3];
	int fd;
 
	snprintf(path, VALUE_MAX, "/sys/class/gpio/gpio%d/value", pin);
	fd = open(path, O_RDONLY);
	if (-1 == fd) {
		LOGE( "Failed to open gpio value for reading! %s", path);
		return(-1);
	}
 
	if (-1 == read(fd, value_str, 3)) {
		LOGE("Failed to read value! %s", path);
		return(-1);
	}
 
	close(fd);
 
	return(atoi(value_str));
}

static int
GPIOWaitForInterrupt(GPIOContext *gpio_ctx)
{
    int gpioValue = 0;
    char buf[8];
	//LOGE("waiting for interrupt");
	poll(&gpio_ctx->pfd, 1, -1);		   /* wait for interrupt */
	lseek(gpio_ctx->pfd.fd, 0, SEEK_SET);    /* consume interrupt */
	int bytes_read = read(gpio_ctx->pfd.fd, buf, (int)sizeof(buf));
	buf[bytes_read] = '\0';
	gpioValue = atoi(buf);
	LOGE("Interrupt Received gpioValue %d", gpioValue);	
	return gpioValue;
}

void* gpio_control_thread(void *ctx)
{
	
	LOGE("gpio_control_thread ++");

	GPIOContext *gpio_ctx = (GPIOContext*)ctx;
    int value = HIGH;

	JNIEnv* env = NULL;
	
    JavaVM *javaVM = gpio_ctx->javaVM;
	javaVM->AttachCurrentThread((&env), NULL);
	gpio_ctx->env = env;
	
	while(gpio_ctx->thread_active){
		//LOGE("Thread Running %d", value);
		//GPIOWrite(POUT, value);
		//value = value == HIGH? LOW: HIGH;
		value = GPIOWaitForInterrupt(gpio_ctx);
		(gpio_ctx->env)->CallVoidMethod(gpio_ctx->mainActivityObj, gpio_ctx->callBackID, value);
		//sleep(1);
	}
	
	LOGE("gpio_control_thread --");

	return 0;
}

void getJniCallBack(JNIEnv *env, jobject instance, GPIOContext *ctx)
{	
	ctx->mainActivityClz = env->GetObjectClass(instance);
    ctx->mainActivityObj = env->NewGlobalRef(instance);

    ctx->callBackID      = env->GetMethodID(
            ctx->mainActivityClz, "GPIOCallback", "(I)V");
}

jint JNICALL Java_com_android_camera_PhotoModule_GPIOInitialize(
        JNIEnv *env, jobject instance)
{
	LOGE("GPIO Control Initialize ++");
	int err;
	GPIOContext *gpio_ctx = &g_ctx;
	
	getJniCallBack(env,instance, gpio_ctx);
	
	gpio_ctx->thread_active = 1;
    err = pthread_create(&g_ctx.gpio_thread_id, NULL, &gpio_control_thread, &g_ctx);
    if (err != 0) {
		LOGE("can't create thread :[%s]", strerror(err));
    } else {
		LOGE("Thread created successfully");
    }
	
	char path[VALUE_MAX];		
    sprintf(path, "/sys/class/gpio/gpio%d/value", PIN);
    if ((gpio_ctx->pfd.fd= open(path, O_RDONLY)) < 0)
    {
        LOGE("Failed, gpio %d not exported.\n", PIN);
        return -1;
    }
	gpio_ctx->pfd.events = POLLPRI;
	
	LOGE("GPIO Control Initialize --");
    return 0;
}

jint JNICALL Java_com_android_camera_PhotoModule_GPIODeInitialize(
        JNIEnv* env, jobject instance)
{
	LOGE("GPIO Control DeInitialize ++");
	GPIOContext *gpio_ctx = &g_ctx;
	gpio_ctx->thread_active  =  0;
	close(gpio_ctx->pfd.fd);
	LOGE("GPIO Control DeInitialize --");
    return 0;
}
 from

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    JNIEnv* env;
    memset(&g_ctx, 0, sizeof(g_ctx));

	LOGE("GPIO Control JNI_OnLoad ++");

    g_ctx.javaVM = vm;
    if (vm->GetEnv((void**)&env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR; // JNI version not supported.
    }
	
	LOGE("GPIO Control JNI_OnLoad --");
	
    return  JNI_VERSION_1_6;
}

