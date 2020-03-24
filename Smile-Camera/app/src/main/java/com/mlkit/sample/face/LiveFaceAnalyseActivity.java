/*
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

package com.mlkit.sample.face;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.huawei.hms.mlsdk.MLAnalyzerFactory;
import com.huawei.hms.mlsdk.common.LensEngine;
import com.huawei.hms.mlsdk.common.MLAnalyzer;
import com.huawei.hms.mlsdk.face.MLFace;
import com.huawei.hms.mlsdk.face.MLFaceAnalyzer;
import com.huawei.hms.mlsdk.face.MLFaceAnalyzerSetting;
import com.huawei.hms.mlsdk.face.MLFaceEmotion;
import com.mlkit.sample.R;
import com.mlkit.sample.camera.LensEnginePreview;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class LiveFaceAnalyseActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LiveFaceAnalyse";

    private static final int CAMERA_PERMISSION_CODE = 2;

    private MLFaceAnalyzer analyzer;

    private LensEngine mLensEngine;

    private LensEnginePreview mPreview;

    private int lensType = LensEngine.BACK_LENS;

    private boolean isFront = false;

    private final float smilingRate = 0.8f;

    private final float smilingPossibility = 0.85f;

    private final static int STOP_PREVIEW = 1;

    private final static int TAKE_PHOTO = 2;

    private boolean safeToTakePicture = false;

    private String storePath = "/storage/emulated/0/DCIM/Camera";

    private Button restart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_live_face_analyse);
        if (savedInstanceState != null) {
            this.lensType = savedInstanceState.getInt("lensType");
        }
        this.mPreview = this.findViewById(R.id.preview);
        this.createFaceAnalyzer();
        this.findViewById(R.id.facingSwitch).setOnClickListener(this);
        this.restart = findViewById(R.id.restart);
        // Checking Camera Permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            this.createLensEngine();
        } else {
            this.requestCameraPermission();
        }
    }

    private void requestCameraPermission() {
        final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, LiveFaceAnalyseActivity.CAMERA_PERMISSION_CODE);
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.startLensEngine();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.mPreview.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.mLensEngine != null) {
            this.mLensEngine.release();
        }
        if (this.analyzer != null) {
            try {
                this.analyzer.stop();
            } catch (IOException e) {
                Log.e(LiveFaceAnalyseActivity.TAG, "Stop failed: " + e.getMessage());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LiveFaceAnalyseActivity.CAMERA_PERMISSION_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }
        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            this.createLensEngine();
            return;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("lensType", this.lensType);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        this.isFront = !this.isFront;
        if (this.isFront) {
            this.lensType = LensEngine.FRONT_LENS;
        } else {
            this.lensType = LensEngine.BACK_LENS;
        }
        if (this.mLensEngine != null) {
            this.mLensEngine.close();
        }
        this.createLensEngine();
        this.startLensEngine();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case STOP_PREVIEW:
                    stopPreview();
                    break;
                case TAKE_PHOTO:
                    takePhoto();
                    break;
                default:
                    break;
            }
        }
    };

    private void createFaceAnalyzer() {
        // Create a face analyzer. You can create an analyzer using the provided customized face detection parameter
        // MLFaceAnalyzerSetting
        MLFaceAnalyzerSetting setting =
                new MLFaceAnalyzerSetting.Factory()
                        .setFeatureType(MLFaceAnalyzerSetting.TYPE_FEATURES)
                        .setKeyPointType(MLFaceAnalyzerSetting.TYPE_UNSUPPORT_KEYPOINTS)
                        .setMinFaceProportion(0.1f)
                        .setTracingAllowed(true)
                        .create();
        this.analyzer = MLAnalyzerFactory.getInstance().getFaceAnalyzer(setting);
        this.analyzer.setTransactor(new MLAnalyzer.MLTransactor<MLFace>() {
            @Override
            public void destroy() {
            }

            @Override
            public void transactResult(MLAnalyzer.Result<MLFace> result) {
                SparseArray<MLFace> faceSparseArray = result.getAnalyseList();
                int flag = 0;
                for (int i = 0; i < faceSparseArray.size(); i++) {
                    MLFaceEmotion emotion = faceSparseArray.valueAt(i).getEmotions();
                    if (emotion.getSmilingProbability() > smilingPossibility) {
                        flag++;
                    }
                }
                if (flag > faceSparseArray.size() * smilingRate && safeToTakePicture) {
                    safeToTakePicture = false;
                    mHandler.sendEmptyMessage(TAKE_PHOTO);
                }
            }
        });
    }

    private void createLensEngine() {
        Context context = this.getApplicationContext();
        // Create LensEngine
        this.mLensEngine = new LensEngine.Creator(context, this.analyzer).setLensType(this.lensType)
                .applyDisplayDimension(640, 480)
                .applyFps(25.0f)
                .enableAutomaticFocus(true)
                .create();
    }

    private void startLensEngine() {
        this.restart.setVisibility(View.GONE);
        if (this.mLensEngine != null) {
            try {
                this.mPreview.start(this.mLensEngine);
                this.safeToTakePicture = true;
            } catch (IOException e) {
                Log.e(LiveFaceAnalyseActivity.TAG, "Failed to start lens engine.", e);
                this.mLensEngine.release();
                this.mLensEngine = null;
            }
        }
    }

    private void takePhoto() {
        this.mLensEngine.photograph(null,
                new LensEngine.PhotographListener() {
                    @Override
                    public void takenPhotograph(byte[] bytes) {
                        mHandler.sendEmptyMessage(STOP_PREVIEW);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        saveBitmapToDisk(bitmap);
                    }
                });
    }

    public void startPreview(View view) {
        createFaceAnalyzer();
        mPreview.release();
        createLensEngine();
        startLensEngine();
    }

    private void stopPreview() {
        this.restart.setVisibility(View.VISIBLE);
        if (mLensEngine != null) {
            mLensEngine.release();
            this.safeToTakePicture = false;
        }
        if (analyzer != null) {
            try {
                this.analyzer.stop();
            } catch (IOException e) {
                Log.e(LiveFaceAnalyseActivity.TAG, "Stop failed: " + e.getMessage());
            }
        }
    }

    private String saveBitmapToDisk(Bitmap bitmap) {
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            boolean res = appDir.mkdir();
            if (!res) {
                Log.e(TAG, "saveBitmapToDisk failed");
                return "";
            }
        }

        String fileName = "SmileDemo" + System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }
}
