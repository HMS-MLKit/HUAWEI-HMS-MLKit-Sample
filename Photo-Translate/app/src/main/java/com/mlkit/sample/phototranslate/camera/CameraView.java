/**
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mlkit.sample.phototranslate.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback {
    private Camera mCamera;
    private AutoFocusManager autoFocusManager;
    private boolean released;

    public CameraView(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        getHolder().addCallback(this);
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        initCamera(mCamera);
    }

    private void initCamera(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        for (Camera.Size size : previewSizes) {
            if (size.width / 16 == size.height / 9) {
                parameters.setPreviewSize(size.width, size.height);
                break;
            }
        }

        int maxWidth = 0;
        int maxHeight = 0;
        List<Camera.Size> pictureSizes = parameters.getSupportedPictureSizes();
        for (Camera.Size size : pictureSizes) {
            Log.i("wfp", "setPictureSize size.width: " + size.width + " height: " + size.height);
            if (size.width / 16 == size.height / 9) {
                if (size.width > maxWidth && size.height > maxHeight) {
                    maxWidth = size.width;
                    maxHeight = size.height;
                }
            }
        }
        parameters.setPictureSize(maxWidth, maxHeight);

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(CameraUtils.findCameraId(false), info);
        int rotation = info.orientation % 360;
        parameters.setRotation(rotation);
        camera.setDisplayOrientation(90);
        parameters.setJpegQuality(100);
        camera.setParameters(parameters);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isReleased() {
        return released;
    }

    public void setReleased(boolean released) {
        this.released = released;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            if (autoFocusManager == null) {
                autoFocusManager = new AutoFocusManager(mCamera);
            }
        } catch (IOException e) {
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("wfp", "surface width: " + width + " height: " + height);
        if (getHolder().getSurface() == null) {
            return;
        }
        mCamera.stopPreview();
        if (autoFocusManager != null) {
            autoFocusManager.stop(released);
            autoFocusManager = null;
        }
        initCamera(mCamera);

        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            if (autoFocusManager == null) {
                autoFocusManager = new AutoFocusManager(mCamera);
            }
        } catch (IOException e) {
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (autoFocusManager != null) {
            autoFocusManager.stop(released);
            autoFocusManager = null;
        }
    }
}
