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

package com.mlkit.sample.activity.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.mlkit.sample.R;
import com.mlkit.sample.activity.ProductVisionSearchActivity;
import com.mlkit.sample.activity.RemoteDetectionActivity;
import com.mlkit.sample.activity.FaceDetectionActivity;
import com.mlkit.sample.activity.ImageClassificationActivity;
import com.mlkit.sample.activity.ObjectDetectionActivity;
import com.mlkit.sample.activity.entity.GridViewItem;
import com.mlkit.sample.activity.imgseg.ImageSegmentationActivity;
import com.mlkit.sample.util.Constant;

import java.util.ArrayList;

public class ImageCategoryFragment extends BaseFragment {

    private static final int[] image_icons = {R.drawable.icon_segmentation, R.drawable.icon_face,
            R.drawable.icon_shopping, R.drawable.icon_object, R.drawable.icon_classification,
            R.drawable.icon_landmark};

    private static final int[] image_titles = {R.string.image_segmentation, R.string.face_detection,
            R.string.photographed_shopping, R.string.object_detection, R.string.image_classification,
            R.string.landmark};

    @Override
    protected void initClickEvent(View view) {
        if(this.mGridView == null){
            return;
        }
        this.mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent = null;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // Image Segmentation
                        ImageCategoryFragment.this.startActivity(
                                new Intent(ImageCategoryFragment.this.getActivity(), ImageSegmentationActivity.class));
                        break;
                    case 1:
                        // Face detection
                        ImageCategoryFragment.this.startActivity(
                                new Intent(ImageCategoryFragment.this.getActivity(), FaceDetectionActivity.class));
                        break;
                    case 2:
                        // Product Visual Search
                        ImageCategoryFragment.this.startActivity(
                                new Intent(ImageCategoryFragment.this.getActivity(), ProductVisionSearchActivity.class));
                        break;
                    case 3:
                        // Object detection and tracking
                        ImageCategoryFragment.this.startActivity(
                                new Intent(ImageCategoryFragment.this.getActivity(), ObjectDetectionActivity.class));
                        break;
                    case 4:
                        // Image classification
                        ImageCategoryFragment.this.startActivity(
                                new Intent(ImageCategoryFragment.this.getActivity(), ImageClassificationActivity.class));
                        break;
                    case 5:
                        // Landmark recognition
                        this.intent =
                                new Intent(ImageCategoryFragment.this.getActivity(), RemoteDetectionActivity.class);
                        this.intent.putExtra(Constant.MODEL_TYPE, Constant.CLOUD_LANDMARK_DETECTION);
                        ImageCategoryFragment.this.startActivity(this.intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void initData() {
        this.mDataList = new ArrayList<GridViewItem>();
        GridViewItem item;
        for (int i = 0; i < ImageCategoryFragment.image_icons.length; i++) {
            item = new GridViewItem(ImageCategoryFragment.image_icons[i], ImageCategoryFragment.image_titles[i]);
            this.mDataList.add(item);
        }
    }
}
