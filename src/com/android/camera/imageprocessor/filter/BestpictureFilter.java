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
package com.android.camera.imageprocessor.filter;

import android.content.Intent;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.android.camera.BestpictureActivity;
import com.android.camera.CameraActivity;
import com.android.camera.CaptureModule;
import com.android.camera.MediaSaveService;
import com.android.camera.PhotoModule;
import com.android.camera.imageprocessor.PostProcessor;
import com.android.camera.util.CameraUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class BestpictureFilter implements ImageFilter {
    public static final int NUM_REQUIRED_IMAGE = 10;
    private int mWidth;
    private int mHeight;
    private int mStrideY;
    private int mStrideVU;
    private static String TAG = "BestpictureFilter";
    private static final boolean DEBUG = false;
    private static boolean mIsSupported = true;
    private CaptureModule mModule;
    private CameraActivity mActivity;
    private int mOrientation = 0;
    final String[] NAMES = {"00.jpg", "01.jpg", "02.jpg", "03.jpg",
            "04.jpg", "05.jpg", "06.jpg", "07.jpg", "08.jpg"
            ,"09.jpg"};

    private final static int TIME_DELAY = 50;
    private int mSavedCount = 0;
    private PhotoModule.NamedImages mNamedImages;
    private ByteBuffer mBY;
    private ByteBuffer mBVU;

    private static void Log(String msg) {
        if (DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public BestpictureFilter(CaptureModule module, CameraActivity activity) {
        mModule = module;
        mActivity = activity;
        mNamedImages = new PhotoModule.NamedImages();
    }

    @Override
    public List<CaptureRequest> setRequiredImages(CaptureRequest.Builder builder) {
        List<CaptureRequest> list = new ArrayList<CaptureRequest>();
        for(int i=0; i < NUM_REQUIRED_IMAGE; i++) {
            list.add(builder.build());
        }
        return list;
    }

    @Override
    public String getStringName() {
        return "BestpictureFilter";
    }

    @Override
    public int getNumRequiredImage() {
        return NUM_REQUIRED_IMAGE;
    }

    @Override
    public void init(int width, int height, int strideY, int strideVU) {
        Log("init");
        mWidth = width/2*2;
        mHeight = height/2*2;
        mStrideY = strideY/2*2;
        mStrideVU = strideVU / 2 * 2;
        Log("width: " + mWidth + " height: " + mHeight + " strideY: " + mStrideY + " strideVU: " + mStrideVU);
    }

    @Override
    public void deinit() {
        Log("deinit");
    }

    @Override
    public void addImage(final ByteBuffer bY, final ByteBuffer bVU, final int imageNum, Object param) {
        Log("addImage");
        if(imageNum == 0) {
            mOrientation = CameraUtil.getJpegRotation(mModule.getMainCameraId(), mModule.getDisplayOrientation());
            mSavedCount = 0;
            mBY = bY;
            mBVU = bVU;
        }
        new Thread() {
            public void run() {
                saveToPrivateFile(imageNum, nv21ToJpeg(bY, bVU, new Rect(0, 0, mWidth, mHeight), mOrientation));
                mSavedCount++;
            }
        }.start();
    }

    @Override
    public ResultImage processImage() {
        long captureStartTime = System.currentTimeMillis();
        mNamedImages.nameNewImage(captureStartTime);
        PhotoModule.NamedImages.NamedEntity name = mNamedImages.getNextNameEntity();
        String title = (name == null) ? null : name.title;
        long date = (name == null) ? -1 : name.date;
        mActivity.getMediaSaveService().addImage(
                nv21ToJpeg(mBY, mBVU, new Rect(0, 0, mWidth, mHeight), mOrientation), title, date, null, mWidth, mHeight,
                        mOrientation, null, new MediaSaveService.OnMediaSavedListener() {
                    @Override
                    public void onMediaSaved(Uri uri) {
                        if (uri != null) {
                            mActivity.notifyNewMedia(uri);
                        }
                        startBestpictureActivity(uri);
                    }
                }
                , mActivity.getContentResolver(), "jpeg");
        while(mSavedCount < NUM_REQUIRED_IMAGE) {
            try {
                Thread.sleep(1);
            } catch (Exception e) {
            }
        }
        return null;
    }

    private void startBestpictureActivity(Uri uri) {
        Intent intent = new Intent();
        intent.setData(uri);
        intent.setClass(mActivity, BestpictureActivity.class);
        mActivity.startActivityForResult(intent, BestpictureActivity.BESTPICTURE_ACTIVITY_CODE);
    }

    @Override
    public boolean isSupported() {
        return mIsSupported;
    }

    @Override
    public boolean isFrameListener() {
        return false;
    }

    @Override
    public boolean isManualMode() {
        return false;
    }

    @Override
    public void manualCapture(CaptureRequest.Builder builder, CameraCaptureSession captureSession,
                              CameraCaptureSession.CaptureCallback callback, Handler handler) throws CameraAccessException {
        for(int i=0; i < NUM_REQUIRED_IMAGE; i++) {
            captureSession.capture(builder.build(), callback, handler);
            try {
                Thread.sleep(TIME_DELAY);
            } catch (InterruptedException e) {
            }
        }
    }

    public static boolean isSupportedStatic() {
        return mIsSupported;
    }

    private byte[] nv21ToJpeg(ByteBuffer bY, ByteBuffer bVU, Rect roi, int orientation) {
        ByteBuffer buf =  ByteBuffer.allocate(mStrideY*mHeight*3/2);
        buf.put(bY);
        bY.rewind();
        if(bVU != null) {
            buf.put(bVU);
            bVU.rewind();
        }
        BitmapOutputStream bos = new BitmapOutputStream(1024);
        YuvImage im = new YuvImage(buf.array(), ImageFormat.NV21,
                mWidth, mHeight, new int[]{mStrideY, mStrideVU});
        im.compressToJpeg(roi, 50, bos);
        byte[] bytes = bos.getArray();
        bytes = PostProcessor.addExifTags(bytes, orientation);
        return bytes;
    }

    private class BitmapOutputStream extends ByteArrayOutputStream {
        public BitmapOutputStream(int size) {
            super(size);
        }

        public byte[] getArray() {
            return buf;
        }
    }

    private void saveToPrivateFile(final int index, final byte[] bytes) {
        String filesPath = mActivity.getFilesDir()+"/Bestpicture";
        File file = new File(filesPath);
        if(!file.exists()) {
            file.mkdir();
        }
        file = new File(filesPath+"/"+NAMES[index]);
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(bytes, 0, bytes.length);
            out.close();
        } catch (Exception e) {
        }
    }
}
