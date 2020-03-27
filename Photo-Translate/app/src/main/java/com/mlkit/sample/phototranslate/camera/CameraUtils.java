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
import android.content.pm.PackageManager;
import android.hardware.Camera;

import com.huawei.hms.mlsdk.common.internal.client.SmartLog;

public class CameraUtils {
    private static final String TAG = "CameraUtils";

    /**
     * Check for cameras
     *
     * @param context
     * @return boolean
     */
    public static boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * Check for flash
     *
     * @param context
     * @return boolean
     */
    public static boolean hasFlash(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    /**
     * Find camera
     *
     * @param front Whether front camera
     * @return Camera ID
     */
    public static int findCameraId(boolean front) {
        final Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        final int cameraCount = Camera.getNumberOfCameras();
        for (int index = 0; index < cameraCount; index++) {
            Camera.getCameraInfo(index, cameraInfo);
            if ((front && cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
                    || (!front && cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK)) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Open camera
     *
     * @return Camera
     */
    public static Camera open() {
        Camera camera = null;
        int numCameras = Camera.getNumberOfCameras();
        if (numCameras == 0) {
            SmartLog.e(CameraUtils.TAG, "No cameras!");
            return camera;
        }
        int cameraId = findCameraId(false);
        if (cameraId < numCameras && cameraId >= 0) {
            SmartLog.d(CameraUtils.TAG, "Opening camera #" + cameraId);
            camera = Camera.open(cameraId);
        } else {
            camera = Camera.open(0);
        }
        return camera;
    }
}
