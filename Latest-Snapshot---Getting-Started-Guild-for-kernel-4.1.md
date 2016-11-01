## Instructions on how to build 4.1 + M from source from Github

### Toolchain

Using the AOSP toolchain.

### To build kernel and DTB

```
export AOSP_SRC=<aosp_source_location>
make ARCH=arm64 CROSS_COMPILE=$AOSP_SRC/prebuilts/gcc/linux-x86/aarch64/aarch64-linux-android-4.9/bin/aarch64-linux-android- hikey_defconfig 
make ARCH=arm64 CROSS_COMPILE=$AOSP_SRC/prebuilts/gcc/linux-x86/aarch64/aarch64-linux-android-4.9/bin/aarch64-linux-android- Image dtbs
```

**NOTE**: Above should work with released "kernel_config" .  Our build system passes KCFLAGS=" -fno-pic "  while building the kernel. Try setting the same. 

### To build AOSP (Android M)

```
repo init -u https://android.googlesource.com/platform/manifest -b android-6.0.0_r260
cd .repo/
git clone https://github.com/96boards/android_manifest -b android-6.0 local_manifests
cd ../
repo sync -j16
```
Download and extract vendor tarball from the link below:
http://builds.96boards.org/snapshots/hikey/linaro/binaries/20150706/vendor.tar.bz2

**NOTE**: Please download this from a browser. You need to accept "END USER LICENCE AGREEMENT FOR THE MALI GPU USERSPACE DRIVER (“MALI GPU DRIVER”)" EULA license.   
**NOTE**: Currently it includes Mali library.

```
source build/envsetup.sh
lunch hikey-userdebug
make droidcore -j32
```
, from Vishal Bhoj.