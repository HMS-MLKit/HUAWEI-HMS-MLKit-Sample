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

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hms.mlsdk.common.internal.client.SmartLog;
import com.mlkit.sample.phototranslate.R;
import com.mlkit.sample.phototranslate.util.Constant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CapturePhotoActivity extends AppCompatActivity {
    private static final String TAG = "CapturePhotoActivity";
    private FrameLayout framelayout;
    private Camera camera;
    private CameraView cameraView;
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        setContentView(R.layout.activity_capture_photo);
        this.imageButton = findViewById(R.id.img_takePhoto);
        this.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toTakePhoto();
            }
        });
        framelayout = findViewById(R.id.camera);
        if (!CameraUtils.checkCameraHardware(this)) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x01);
            } else {
                resumeCamera();
            }
        } else {
            resumeCamera();
        }
    }

    private void toTakePhoto() {
        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                String filePath = saveBitmapToDisk(BitmapFactory.decodeByteArray(data, 0, data.length));
                Intent intent = new Intent();
                intent.putExtra(Constant.IMAGE_PATH_VALUE, filePath);
                setResult(Activity.RESULT_OK,intent);
                CapturePhotoActivity.this.finish();
            }
        });
    }

    private void resumeCamera() {
        camera = CameraUtils.open();
        if (camera == null) {
            return;
        }
        cameraView = new CameraView(this, camera);
        cameraView.setReleased(false);
        framelayout.removeAllViews();
        framelayout.addView(cameraView);
    }

    private void initWindow() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Android 5.0 +
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Android 4.4 +
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    private String saveBitmapToDisk(Bitmap bitmap) {
        String storePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "PhotoTranslate";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            boolean res = appDir.mkdir();
            if (!res) {
                SmartLog.e(TAG, "saveBitmapToDisk failed");
                return "";
            }
        }

        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            Uri uri = Uri.fromFile(file);
            this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file.getAbsolutePath();
    }
}
