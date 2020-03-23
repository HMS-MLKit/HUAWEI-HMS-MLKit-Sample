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

package com.mlkit.sample.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import com.huawei.hmf.tasks.Task;
import com.huawei.hms.mlsdk.MLAnalyzerFactory;
import com.huawei.hms.mlsdk.common.MLFrame;

import com.huawei.hms.mlsdk.productvisionsearch.MLProductVisionSearch;
import com.huawei.hms.mlsdk.productvisionsearch.MLVisionSearchProduct;
import com.huawei.hms.mlsdk.productvisionsearch.MLVisionSearchProductImage;
import com.huawei.hms.mlsdk.productvisionsearch.cloud.MLRemoteProductVisionSearchAnalyzer;
import com.huawei.hms.mlsdk.productvisionsearch.cloud.MLRemoteProductVisionSearchAnalyzerSetting;
import com.mlkit.sample.R;

import com.mlkit.sample.activity.adapter.ProductVisionSearchAdapter;
import com.mlkit.sample.activity.dialog.AddPictureDialog;
import com.mlkit.sample.camera.FrameMetadata;
import com.mlkit.sample.transactor.BaseTransactor;
import com.mlkit.sample.transactor.ImageTransactor;
import com.mlkit.sample.util.BitmapUtils;
import com.mlkit.sample.views.overlay.GraphicOverlay;
import com.mlkit.sample.views.overlay.ZoomImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public final class ProductVisionSearchActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = "ProductVisionSearch";

    private static final String KEY_IMAGE_URI = "KEY_IMAGE_URI";
    private static final String KEY_IMAGE_MAX_WIDTH =
            "KEY_IMAGE_MAX_WIDTH";
    private static final String KEY_IMAGE_MAX_HEIGHT =
            "KEY_IMAGE_MAX_HEIGHT";

    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_SELECT_IMAGE = 2;
    private static final int DELAY_TIME = 600;
    private Button getImageButton;
    private ImageView preview;
    private TextView title;
    private GraphicOverlay graphicOverlay;
    private ZoomImageView changeImageView;

    boolean isLandScape;

    private Uri imageUri;
    private Integer maxWidthOfImage;
    private Integer maxHeightOfImage;

    private Dialog progressDialog;

    private Runnable detectRunnable = new Runnable() {
        @Override
        public void run() {
            ProductVisionSearchActivity.this.loadImage();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.imageUri = savedInstanceState.getParcelable(ProductVisionSearchActivity.KEY_IMAGE_URI);
            if (this.imageUri != null) {
                this.maxWidthOfImage = savedInstanceState.getInt(ProductVisionSearchActivity.KEY_IMAGE_MAX_WIDTH);
                this.maxHeightOfImage = savedInstanceState.getInt(ProductVisionSearchActivity.KEY_IMAGE_MAX_HEIGHT);
            }
        }

        this.setContentView(R.layout.activity_remote_detection);

        this.initView();

        this.createImageTransactor();
        this.isLandScape = (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
        this.setStatusBar();
        this.slectLocalImage();
    }

    private void initView() {
        this.title = this.findViewById(R.id.page_title);
        this.title.setText(R.string.photographed_shopping_s);

        this.findViewById(R.id.back).setOnClickListener(this);
        this.getImageButton = this.findViewById(R.id.getImageButton);
        this.preview = this.findViewById(R.id.still_preview);
        this.graphicOverlay = this.findViewById(R.id.still_overlay);
        this.changeImageView = this.findViewById(R.id.changeOverlay);
        this.getImageButton.setOnClickListener(this);
    }

    private Bitmap getBorderThumbnail(Bitmap source, Rect border) {
        int x = border.left > 0 ? border.left : 1;
        int y = border.top > 0 ? border.top : 1;
        int width = border.width() > 0 ? border.width() : 1;
        int height = border.height() > 0 ? border.height() : 1;

        return Bitmap.createBitmap(source, x, y, width, height, null, false);
    }

    private void showProductVisionSearchDialog(String type, Rect border, List<MLVisionSearchProductImage> productImageList) {
        View view = View.inflate(this, R.layout.activity_product_vision_search_dialog, null);

        Drawable drawable = this.preview.getDrawable();
        if (drawable != null) {
            ((ImageView) view.findViewById(R.id.picture)).setImageBitmap(this.getBorderThumbnail(((BitmapDrawable) drawable).getBitmap(), border));
        }

        if (productImageList == null || productImageList.isEmpty()) {
            view.findViewById(R.id.empty).setVisibility(View.VISIBLE);
        } else {
            RecyclerView recyclerView = view.findViewById(R.id.recycler);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new GridLayoutManager(this.getApplicationContext(), 2));
            recyclerView.setAdapter(new ProductVisionSearchAdapter(productImageList));
        }

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

        // Set transparent background color for layout.
        bottomSheetDialog.getDelegate().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void reloadAndDetectImage() {
        if (this.preview == null || this.maxHeightOfImage == null || (this.maxHeightOfImage == 0
                && ((View) this.preview.getParent()).getHeight() == 0)) {
            this.getWindow().getDecorView().postDelayed(this.detectRunnable, ProductVisionSearchActivity.DELAY_TIME);
        } else {
            this.loadImage();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.getImageButton) {
            showChoosePicDialog();
        } else if (view.getId() == R.id.back) {
            this.finish();
        }
    }

    private void showChoosePicDialog() {
        AddPictureDialog addPictureDialog = new AddPictureDialog(this, AddPictureDialog.TYPE_CUSTOM);
        addPictureDialog.setClickListener(new AddPictureDialog.ClickListener() {
            @Override
            public void takePicture() {
                startCamera();
            }

            @Override
            public void selectImage() {
                slectLocalImage();
            }

            @Override
            public void doExtend() {

            }
        });
        addPictureDialog.show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ProductVisionSearchActivity.KEY_IMAGE_URI, this.imageUri);
        if (this.maxWidthOfImage != null) {
            outState.putInt(ProductVisionSearchActivity.KEY_IMAGE_MAX_WIDTH, this.maxWidthOfImage);
        }
        if (this.maxHeightOfImage != null) {
            outState.putInt(ProductVisionSearchActivity.KEY_IMAGE_MAX_HEIGHT, this.maxHeightOfImage);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        this.preview.setVisibility(View.GONE);
        this.graphicOverlay.setVisibility(View.GONE);
        this.changeImageView.setVisibility(View.VISIBLE);
    }

    private void startCamera() {
        this.imageUri = null;
        this.preview.setImageBitmap(null);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
            this.imageUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, this.imageUri);
            this.startActivityForResult(takePictureIntent, ProductVisionSearchActivity.REQUEST_TAKE_PHOTO);
        }
    }

    private void slectLocalImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        this.startActivityForResult(intent, ProductVisionSearchActivity.REQUEST_SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ProductVisionSearchActivity.REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            this.reloadAndDetectImage();
        } else if (requestCode == ProductVisionSearchActivity.REQUEST_SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            this.imageUri = data.getData();
            this.reloadAndDetectImage();
        }
    }

    private void loadImage() {
        try {
            if (this.imageUri == null) {
                return;
            }
            this.showLoadingDialog();
            this.graphicOverlay.clear();
            Bitmap resizedBitmap = BitmapUtils.loadFromPath(ProductVisionSearchActivity.this, this.imageUri, this.getMaxWidthOfImage(), this.getMaxHeightOfImage());
            this.preview.setImageBitmap(resizedBitmap);
            if (resizedBitmap != null) {
                this.imageTransactor.process(resizedBitmap, this.graphicOverlay);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            Log.e(ProductVisionSearchActivity.TAG, "Error retrieving saved image");
        }
    }

    private Integer getMaxWidthOfImage() {
        if (this.maxWidthOfImage == null || this.maxWidthOfImage == 0) {
            if (this.isLandScape) {
                this.maxWidthOfImage = ((View) this.preview.getParent()).getHeight();
            } else {
                this.maxWidthOfImage = ((View) this.preview.getParent()).getWidth();
            }
        }
        return this.maxWidthOfImage;
    }

    private Integer getMaxHeightOfImage() {
        if (this.maxHeightOfImage == null || this.maxHeightOfImage == 0) {
            if (this.isLandScape) {
                this.maxHeightOfImage = ((View) this.preview.getParent()).getWidth();
            } else {
                this.maxHeightOfImage = ((View) this.preview.getParent()).getHeight();
            }
        }
        return this.maxHeightOfImage;
    }

    private void createImageTransactor() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.imageTransactor != null) {
            this.imageTransactor.stop();
            this.imageTransactor = null;
        }

        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
            this.progressDialog = null;
        }
    }

    private void showLoadingDialog() {
        if (this.progressDialog == null) {
            this.progressDialog = new Dialog(ProductVisionSearchActivity.this, R.style.progress_dialog);
            this.progressDialog.setContentView(R.layout.dialog);
            this.progressDialog.setCancelable(false);
            this.progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            TextView msg = this.progressDialog.findViewById(R.id.id_tv_loadingmsg);
            msg.setText(this.getString(R.string.loading_data));
        }

        this.progressDialog.show();
    }

    private void dismissLoadingDialog() {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
    }

    private MLRemoteProductVisionSearchAnalyzerSetting options = new MLRemoteProductVisionSearchAnalyzerSetting.Factory()
            .setLargestNumOfReturns(16)
            .create();

    private ImageTransactor imageTransactor = new ProductVisionSearchTransactor<List<MLProductVisionSearch>>(this.options) {

        @Override
        protected Task<List<MLProductVisionSearch>> detectInImage(MLFrame image) {
            return detector.asyncAnalyseFrame(image);
        }

        @Override
        protected void onSuccess(Bitmap originalCameraImage, List<MLProductVisionSearch> results, FrameMetadata hmsMLFrameMetadata, GraphicOverlay graphicOverlay) {
            ProductVisionSearchActivity.this.dismissLoadingDialog();

            String type = null;
            Rect border = new Rect();
            List<MLVisionSearchProductImage> productImageList = new ArrayList<MLVisionSearchProductImage>();
            if (results != null && !results.isEmpty()) {
                MLProductVisionSearch productVisionSearch = results.get(0);

                type = productVisionSearch.getType();
                border = productVisionSearch.getBorder();

                List<MLVisionSearchProduct> productList = productVisionSearch.getProductList();
                if (productList != null && !productList.isEmpty()) {
                    for (MLVisionSearchProduct product : productList) {
                        productImageList.addAll(product.getImageList());
                    }
                }
            }

            ProductVisionSearchActivity.this.showProductVisionSearchDialog(type, border, productImageList);
        }

        @Override
        protected void onFailure(Exception e) {
            ProductVisionSearchActivity.this.dismissLoadingDialog();
            Toast.makeText(ProductVisionSearchActivity.this.getApplicationContext(), ProductVisionSearchActivity.this.getString(R.string.get_data_failed), Toast.LENGTH_LONG).show();
        }

        @Override
        public void stop() {
            super.stop();

            if (this.detector != null) {
                this.detector.stop();
            }
        }
    };

    private abstract class ProductVisionSearchTransactor<T> extends BaseTransactor<T> {
        protected MLRemoteProductVisionSearchAnalyzer detector;

        public ProductVisionSearchTransactor() {
            this.detector = MLAnalyzerFactory.getInstance().getRemoteProductVisionSearchAnalyzer();
        }

        public ProductVisionSearchTransactor(MLRemoteProductVisionSearchAnalyzerSetting options) {
            this.detector = MLAnalyzerFactory.getInstance().getRemoteProductVisionSearchAnalyzer(options);
        }
    }
}
