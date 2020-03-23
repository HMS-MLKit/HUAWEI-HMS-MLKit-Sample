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
 *//*


package com.mlkit.sample.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;

import com.huawei.hms.mlplugin.card.bcr.MLBcrCapture;
import com.huawei.hms.mlplugin.card.bcr.MLBcrCaptureConfig;
import com.huawei.hms.mlplugin.card.bcr.MLBcrCaptureFactory;
import com.huawei.hms.mlplugin.card.bcr.MLBcrCaptureResult;
import com.huawei.hms.mlsdk.card.MLCardAnalyzerFactory;
import com.huawei.hms.mlsdk.card.bcr.MLBankCard;
import com.huawei.hms.mlsdk.card.bcr.MLBcrAnalyzer;
import com.huawei.hms.mlsdk.common.MLFrame;
import com.huawei.hms.mlsdk.common.internal.client.SmartLog;
import com.mlkit.sample.R;
import com.mlkit.sample.activity.dialog.AddPictureDialog;
import com.mlkit.sample.util.BitmapUtils;

import androidx.appcompat.app.AppCompatActivity;

public class BankCardRecognitionActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "BCRActivity";
    private static final int REQUEST_IMAGE_SELECT_FROM_ALBUM = 1000;
    private static final int REQUEST_IMAGE_CAPTURE = 1001;
    private ImageView frontImg;
    private ImageView frontSimpleImg;
    private ImageView frontDeleteImg;
    private LinearLayout frontAddView;
    private TextView showResult;
    private String lastFrontResult = "";
    private String lastBackResult = "";
    private Uri imageUri;
    private Bitmap currentImage;
    private boolean isRemote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card_recognition);
        initComponent();
    }

    private void initComponent() {
        frontImg = findViewById(R.id.avatar_img);
        frontSimpleImg = findViewById(R.id.avatar_sample_img);
        frontDeleteImg = findViewById(R.id.avatar_delete);
        frontAddView = findViewById(R.id.avatar_add);
        showResult = findViewById(R.id.show_result);

        frontAddView.setOnClickListener(this);
        frontDeleteImg.setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.avatar_add:
                showChoosePicDialog();
                break;
            case R.id.avatar_delete:
                showFrontDeleteImage();
                lastFrontResult = "";
                break;
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (currentImage != null && !currentImage.isRecycled()) {
            currentImage.recycle();
            currentImage = null;
        }
    }

    private void startCaptureActivity(MLBcrCapture.CallBack callback, boolean isLocal) {
        MLBcrCaptureConfig config = new MLBcrCaptureConfig.Factory()
                .setOrientation(MLBcrCaptureConfig.ORIENTATION_AUTO)
                //.setBlankBlockDrawer(new MyBlankBlockDrawer())
                .create();
        MLBcrCapture bcrCapture = MLBcrCaptureFactory.getInstance().getBcrCapture(config);

        bcrCapture.captureFrame(this, callback);
    }

    private String formatIdCardResult(MLBcrCaptureResult result) {
        Log.i(TAG, "formatIdCardResult");
        StringBuilder resultBuilder = new StringBuilder();

        resultBuilder.append("Number：");
        resultBuilder.append(result.getNumber());
        resultBuilder.append("\r\n");

        resultBuilder.append("Issuer：");
        resultBuilder.append(result.getIssuer());
        resultBuilder.append("\r\n");

        resultBuilder.append("Expire: ");
        resultBuilder.append(result.getExpire());
        resultBuilder.append("\r\n");

        resultBuilder.append("Owner: ");
        resultBuilder.append(result.getOwner());
        resultBuilder.append("\r\n");

        resultBuilder.append("Type: ");
        resultBuilder.append(result.getType());
        resultBuilder.append("\r\n");

        Log.i(TAG, "front result: " + resultBuilder.toString());
        return resultBuilder.toString();
    }

    private MLBcrCapture.CallBack callback = new MLBcrCapture.CallBack() {
        @Override
        public void onSuccess(MLBcrCaptureResult idCardResult) {
            Log.i(TAG, "BCR IdCallBack onSuccess ----");
            if (idCardResult == null) {
                Log.i(TAG, "IdCallBack onRecSuccess idCardResult is null");
                return;
            }
            Bitmap bitmap = idCardResult.getOriginalBitmap();
            showSuccessResult(bitmap, idCardResult);
        }

        @Override
        public void onCanceled() {
            Log.i(TAG, "IdCallBack onRecCanceled ----");
        }

        @Override
        public void onFailure(int recCode, Bitmap bitmap) {
            Log.i(TAG, "IdCallBack onRecFailed ----");
            showResult.setText(" RecFailed ");
        }

        @Override
        public void onDenied() {
            Log.i(TAG, "IdCallBack onCameraDenied ----");
        }
    };

    private void showSuccessResult(Bitmap bitmap, MLBcrCaptureResult idCardResult) {
        showFrontImage(bitmap);
        lastFrontResult = formatIdCardResult(idCardResult);
        showResult.setText(lastFrontResult);
        showResult.append(lastBackResult);
        ((ImageView) findViewById(R.id.number)).setImageBitmap(idCardResult.getNumberBitmap());
    }
    private void showFrontImage(Bitmap bitmap) {
        frontImg.setVisibility(View.VISIBLE);
        frontImg.setImageBitmap(bitmap);
        frontSimpleImg.setVisibility(View.GONE);
        frontAddView.setVisibility(View.GONE);
        frontDeleteImg.setVisibility(View.VISIBLE);
    }

    private void showFrontDeleteImage() {
        frontImg.setVisibility(View.GONE);
        frontSimpleImg.setVisibility(View.VISIBLE);
        frontAddView.setVisibility(View.VISIBLE);
        frontDeleteImg.setVisibility(View.GONE);
    }

    private void showChoosePicDialog() {
        AddPictureDialog addPictureDialog = new AddPictureDialog(this, AddPictureDialog.TYPE_CUSTOM);
        addPictureDialog.setClickListener(new AddPictureDialog.ClickListener() {
            @Override
            public void takePicture() {
                startCameraIntentForResult();
            }

            @Override
            public void selectImage() {
                startChooseImageIntentForResult();
            }

            @Override
            public void doExtend() {
                startCaptureActivity(callback, isRemote);
            }
        });
        addPictureDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.i(TAG, "onActivityResult requestCode " + requestCode + ", resultCode " + resultCode);
        if (requestCode == REQUEST_IMAGE_SELECT_FROM_ALBUM && resultCode == RESULT_OK) {
            imageUri = intent.getData();
            tryReloadAndDetectInImage();
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            tryReloadAndDetectInImage();
        }
    }

    */
/**
     * Post processing of local pictures
     *//*

    private void tryReloadAndDetectInImage() {
        currentImage = BitmapUtils.loadFromPath(this, imageUri, getWidth(), getHeight());

        final long startTime = System.currentTimeMillis();
        MLFrame frame = MLFrame.fromBitmap(currentImage);
        MLBcrAnalyzer detector = MLCardAnalyzerFactory.getInstance().getBcrAnalyzer();
        Task<MLBankCard> task = detector.asyncAnalyseFrame(frame);
        task.addOnSuccessListener(new OnSuccessListener<MLBankCard>() {

            public void onSuccess(MLBankCard bankCard) {
                long endTime = System.currentTimeMillis();
                SmartLog.i(TAG, "localAnalyzer detect success Time: " + (endTime - startTime));
                SmartLog.i(TAG, "analyze success! bankCard.getRetCode()=" + bankCard.getRetCode());
                if (bankCard.getRetCode() == MLBankCard.STATUS_SUCCESS) {
                    displayImageSuccess(bankCard);
                } else {
                    displayImageFailure();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(Exception e) {
                long endTime = System.currentTimeMillis();
                SmartLog.i(TAG, "localAnalyzer detect failed Time :" + (endTime - startTime));
                // Recognition failure.
                displayImageFailure();
            }
        });
    }

    private int getWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        int width;
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            width = display.getHeight();
        } else {
            width = display.getWidth();
        }
        return width;
    }

    private int getHeight() {
        Display display = getWindowManager().getDefaultDisplay();
        int height;
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            height = display.getWidth();
        } else {
            height = display.getHeight();
        }
        return height;
    }

    private void displayImageSuccess(MLBankCard bankCard) {
        MLBcrCaptureResult result = new MLBcrCaptureResult();
        result.setNumber(bankCard.getNumber());
        result.setExpire(bankCard.getExpire());
        result.setOwner(bankCard.getOwner());
        result.setOriginalBitmap(bankCard.getOriginalBitmap());
        result.setNumberBitmap(bankCard.getNumberBitmap());

        showSuccessResult(currentImage, result);
    }

    private void displayImageFailure() {
        this.showResult.setText("Failure");
    }

    private void startCameraIntentForResult() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void startChooseImageIntentForResult() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_IMAGE_SELECT_FROM_ALBUM);
    }
}*/
