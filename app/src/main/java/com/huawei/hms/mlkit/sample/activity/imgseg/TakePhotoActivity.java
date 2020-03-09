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

package com.huawei.hms.mlkit.sample.activity.imgseg;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.core.app.ActivityCompat;

import com.huawei.hms.mlkit.sample.R;
import com.huawei.hms.mlkit.sample.activity.BaseActivity;
import com.huawei.hms.mlkit.sample.callback.ImageSegmentationResultCallBack;
import com.huawei.hms.mlkit.sample.camera.CameraConfiguration;
import com.huawei.hms.mlkit.sample.camera.LensEngine;
import com.huawei.hms.mlkit.sample.camera.LensEnginePreview;
import com.huawei.hms.mlkit.sample.transactor.ImageSliceDetectorTransactor;
import com.huawei.hms.mlkit.sample.util.Constant;
import com.huawei.hms.mlkit.sample.util.ImageUtils;
import com.huawei.hms.mlkit.sample.views.overlay.GraphicOverlay;
import com.huawei.hms.mlsdk.common.internal.client.SmartLog;
import com.huawei.hms.mlsdk.imgseg.MLImageSegmentationSetting;

import java.io.IOException;
import java.io.InputStream;

/**
 * It is applied to the image segmentation function. The application scenario is: open the camera,
 * if there is a human body in the picture, then cut out the human body and replace the background
 * to achieve the real-time human detection effect.
 */
public class TakePhotoActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, ImageSegmentationResultCallBack {
    private LensEngine lensEngine = null;
    private LensEnginePreview preview;
    private GraphicOverlay graphicOverlay;
    private ToggleButton facingSwitch;
    private ImageButton img_takePhoto, img_pic;

    private CameraConfiguration cameraConfiguration = null;
    private static String TAG = "TakePhotoActivity";
    private int index;
    private int facing = CameraConfiguration.CAMERA_FACING_FRONT;
    private Bitmap background, processImage;
    private ImageSliceDetectorTransactor transactor;
    private MLImageSegmentationSetting setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_take_photo);
        if (savedInstanceState != null) {
            this.facing = savedInstanceState.getInt(Constant.CAMERA_FACING);
        }
        Intent intent = this.getIntent();
        try {
            this.index = intent.getIntExtra(Constant.VALUE_KEY, -1);
        } catch (RuntimeException e) {
            Log.e(TakePhotoActivity.TAG, "Get intent value failed:" + e.getMessage());
        }
        if (this.index < 0) {
            Toast.makeText(this.getApplicationContext(), R.string.please_select_picture, Toast.LENGTH_SHORT).show();
            this.finish();
        } else {
            // Decode background image.
            int id = Constant.IMAGES[this.index];
            InputStream is = this.getResources().openRawResource(id);
            this.background = BitmapFactory.decodeStream(is);
        }
        this.initView();
        this.initAction();
        this.cameraConfiguration = new CameraConfiguration();
        this.cameraConfiguration.setCameraFacing(this.facing);
        this.cameraConfiguration.setFps(6.0f);
        this.cameraConfiguration.setPreviewWidth(CameraConfiguration.DEFAULT_WIDTH);
        this.cameraConfiguration.setPreviewHeight(CameraConfiguration.DEFAULT_HEIGHT);
        this.createLensEngine();
        this.startLensEngine();
    }

    private void initView() {
        this.preview = this.findViewById(R.id.firePreview);
        this.graphicOverlay = this.findViewById(R.id.fireFaceOverlay);
        this.facingSwitch = this.findViewById(R.id.facingSwitch);
        if (Camera.getNumberOfCameras() == 1) {
            this.facingSwitch.setVisibility(View.GONE);
        }
        this.img_takePhoto = this.findViewById(R.id.img_takePhoto);
        this.img_pic = this.findViewById(R.id.img_pic);
    }

    private void initAction() {
        this.facingSwitch.setOnCheckedChangeListener(this);
        // Set the display effect when the takePhoto button is clicked.
        this.img_takePhoto.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    TakePhotoActivity.this.img_takePhoto.setColorFilter(Color.GRAY);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    TakePhotoActivity.this.img_takePhoto.setColorFilter(Color.WHITE);
                }
                return false;
            }
        });
        this.img_takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save Picture.
                if (TakePhotoActivity.this.processImage == null) {
                    SmartLog.e(TakePhotoActivity.TAG, "The image is null, unable to save.");
                } else {
                    ImageUtils imageUtils = new ImageUtils(TakePhotoActivity.this.getApplicationContext());
                    imageUtils.saveToAlbum(TakePhotoActivity.this.processImage);
                }
            }
        });

        this.img_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Back to background page.
                TakePhotoActivity.this.finish();
            }
        });
    }

    private void createLensEngine() {
        // If there's no existing lensEngine, create one.
        if (this.lensEngine == null) {
            this.lensEngine = new LensEngine(this, this.cameraConfiguration, this.graphicOverlay);
        }
        try {
            this.setting = new MLImageSegmentationSetting.Factory().setAnalyzerType(MLImageSegmentationSetting.BODY_SEG).setExact(false).create();
            this.transactor = new ImageSliceDetectorTransactor(this.getApplicationContext(), this.setting, true, this.background, this.facing);
            this.transactor.setImageSegmentationResultCallBack(this);
            this.lensEngine.setMachineLearningFrameTransactor(this.transactor);
        } catch (Exception e) {
            Log.e(TakePhotoActivity.TAG, "Can not create image transactor: " + e);
            Toast.makeText(
                    this.getApplicationContext(),
                    "Can not create image transactor: " + e.getMessage(),
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void startLensEngine() {
        if (this.lensEngine != null) {
            try {
                if (this.preview == null) {
                    Log.d(TakePhotoActivity.TAG, "resume: Preview is null");
                }
                if (this.graphicOverlay == null) {
                    Log.d(TakePhotoActivity.TAG, "resume: graphOverlay is null");
                }
                if (null != this.preview) {
                    this.preview.start(this.lensEngine, true);
                }
            } catch (IOException e) {
                Log.e(TakePhotoActivity.TAG, "Unable to start lensEngine.", e);
                this.lensEngine.release();
                this.lensEngine = null;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(Constant.CAMERA_FACING, this.facing);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        this.preview.stop();
        this.createLensEngine();
        this.startLensEngine();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d(TakePhotoActivity.TAG, "Set facing");
        if (this.lensEngine != null) {
            if (isChecked) {
                this.facing = CameraConfiguration.CAMERA_FACING_FRONT;
            } else {
                this.facing = CameraConfiguration.CAMERA_FACING_BACK;
                this.transactor.setImageSegmentationResultCallBack(this);
                this.lensEngine.setMachineLearningFrameTransactor(this.transactor);
            }
            this.cameraConfiguration.setCameraFacing(this.facing);
            this.setting = new MLImageSegmentationSetting.Factory().setAnalyzerType(0).create();
            this.transactor = new ImageSliceDetectorTransactor(this.getApplicationContext(), this.setting, true, this.background, this.facing);
            this.transactor.setImageSegmentationResultCallBack(this);
            this.lensEngine.setMachineLearningFrameTransactor(this.transactor);
        }
        this.preview.stop();
        this.startLensEngine();
    }

    public void onBackPressed(View view) {
        this.finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TakePhotoActivity.TAG, "onResume");
        this.startLensEngine();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.preview.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.lensEngine != null) {
            this.lensEngine.release();
        }
        if (this.transactor != null) {
            this.transactor.stop();
        }
        this.facing = CameraConfiguration.CAMERA_FACING_BACK;
        this.cameraConfiguration.setCameraFacing(this.facing);
    }

    @Override
    public void callResultBitmap(Bitmap bitmap) {
        this.processImage = bitmap;
    }

}
