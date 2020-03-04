/**
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.huawei.hms.mlkit.vision.sample.transactor;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huawei.hmf.tasks.Task;
import com.huawei.hms.mlkit.vision.sample.views.graphic.LocalFaceGraphic;
import com.huawei.hms.mlkit.vision.sample.views.graphic.CameraImageGraphic;
import com.huawei.hms.mlkit.vision.sample.camera.FrameMetadata;
import com.huawei.hms.mlkit.vision.sample.views.overlay.GraphicOverlay;
import com.huawei.hms.mlsdk.MLAnalyzerFactory;
import com.huawei.hms.mlsdk.common.MLFrame;
import com.huawei.hms.mlsdk.face.MLFace;
import com.huawei.hms.mlsdk.face.MLFaceAnalyzer;
import com.huawei.hms.mlsdk.face.MLFaceAnalyzerSetting;


import java.io.IOException;
import java.util.List;

public class LocalFaceTransactor extends BaseTransactor<List<MLFace>> {
    private static final String TAG = "LocalFaceTransactor";

    private final MLFaceAnalyzer detector;
    private boolean isLandScape;
    private boolean isOpenFeatures;

    public LocalFaceTransactor(MLFaceAnalyzerSetting options, boolean isLandScape, boolean isOpenFeatures) {
        this.detector = MLAnalyzerFactory.getInstance().getFaceAnalyzer(options);
        this.isLandScape = isLandScape;
        this.isOpenFeatures = isOpenFeatures;
    }

    @Override
    public void stop() {
        try {
            this.detector.stop();
        } catch (IOException e) {
            Log.w(LocalFaceTransactor.TAG, "LocalFaceTransactor stop detector exception.");
        }
    }

    @Override
    protected Task<List<MLFace>> detectInImage(MLFrame image) {
        return this.detector.asyncAnalyseFrame(image);
    }

    @Override
    protected void onSuccess(
            @Nullable Bitmap originalCameraImage,
            @NonNull List<MLFace> faces,
            @NonNull FrameMetadata frameMetadata,
            @NonNull GraphicOverlay graphicOverlay) {

        graphicOverlay.clear();
        if (originalCameraImage != null) {
            CameraImageGraphic imageGraphic = new CameraImageGraphic(graphicOverlay, originalCameraImage);
            graphicOverlay.addGraphic(imageGraphic);
        }
        LocalFaceGraphic hmsMLLocalFaceGraphic = new LocalFaceGraphic(graphicOverlay, faces, this.isLandScape, this.isOpenFeatures);
        graphicOverlay.addGraphic(hmsMLLocalFaceGraphic);
        graphicOverlay.postInvalidate();

    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        Log.e(LocalFaceTransactor.TAG, "Face detection failed " + e);
    }

    @Override
    public boolean isFaceDetection() {
        return true;
    }
}
