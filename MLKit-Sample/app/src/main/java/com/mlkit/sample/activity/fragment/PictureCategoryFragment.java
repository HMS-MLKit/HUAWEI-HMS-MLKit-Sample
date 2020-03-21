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
import com.mlkit.sample.activity.RemoteDetectionActivity;
import com.mlkit.sample.activity.FaceDetectionActivity;
import com.mlkit.sample.activity.IDCardRecognitionActivity;
import com.mlkit.sample.activity.ImageClassificationActivity;
import com.mlkit.sample.activity.ObjectDetectionActivity;
import com.mlkit.sample.activity.TextRecognitionActivity;
import com.mlkit.sample.activity.entity.GridViewItem;
import com.mlkit.sample.activity.imgseg.ImageSegmentationActivity;
import com.mlkit.sample.util.Constant;

import java.util.ArrayList;

public class PictureCategoryFragment extends BaseFragment {

    private static final int[] image_icons = {R.drawable.icon_segmentation, R.drawable.icon_face,
            R.drawable.icon_shopping, R.drawable.icon_object, R.drawable.icon_classification,
            R.drawable.icon_landmark, R.drawable.icon_text, R.drawable.icon_idcard};

    private static final int[] image_titles = {R.string.image_segmentation, R.string.face_detection,
            R.string.photographed_shopping, R.string.object_detection, R.string.image_classification,
            R.string.landmark, R.string.text_detection, R.string.idcard_recognition};

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
                        PictureCategoryFragment.this.startActivity(
                                new Intent(PictureCategoryFragment.this.getActivity(), ImageSegmentationActivity.class));
                        break;
                    case 1:
                        // Face detection
                        PictureCategoryFragment.this.startActivity(
                                new Intent(PictureCategoryFragment.this.getActivity(), FaceDetectionActivity.class));
                        break;
                    case 2:
                        // Product Visual Search
                        Toast.makeText(PictureCategoryFragment.this.getActivity(), PictureCategoryFragment.this.getText(R.string.coming_soon), Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        // Object detection and tracking
                        PictureCategoryFragment.this.startActivity(
                                new Intent(PictureCategoryFragment.this.getActivity(), ObjectDetectionActivity.class));
                        break;
                    case 4:
                        // Image classification
                        PictureCategoryFragment.this.startActivity(
                                new Intent(PictureCategoryFragment.this.getActivity(), ImageClassificationActivity.class));
                        break;
                    case 5:
                        // Landmark recognition
                        this.intent =
                                new Intent(PictureCategoryFragment.this.getActivity(), RemoteDetectionActivity.class);
                        this.intent.putExtra(Constant.MODEL_TYPE, Constant.CLOUD_LANDMARK_DETECTION);
                        PictureCategoryFragment.this.startActivity(this.intent);
                        break;
                    case 6:
                        // Text recognition
                        PictureCategoryFragment.this.startActivity(
                                new Intent(PictureCategoryFragment.this.getActivity(), TextRecognitionActivity.class));
                        break;
                    case 7:
                        // Card recognition
                        PictureCategoryFragment.this.startActivity(
                                new Intent(PictureCategoryFragment.this.getActivity(), IDCardRecognitionActivity.class));
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
        for (int i = 0; i < PictureCategoryFragment.image_icons.length; i++) {
            item = new GridViewItem(PictureCategoryFragment.image_icons[i], PictureCategoryFragment.image_titles[i]);
            this.mDataList.add(item);
        }
    }
}
