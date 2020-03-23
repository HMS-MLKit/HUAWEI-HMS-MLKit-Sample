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

import com.mlkit.sample.activity.IDCardRecognitionActivity;
import com.mlkit.sample.activity.RemoteDetectionActivity;
import com.mlkit.sample.activity.TextRecognitionActivity;
import com.mlkit.sample.activity.entity.GridViewItem;
import com.mlkit.sample.util.Constant;

import java.util.ArrayList;

public class TextCategoryFragment extends BaseFragment {
    private static final int[] icons = {R.drawable.icon_text, R.drawable.icon_icr, R.drawable.icon_bcr, R.drawable.icon_ucr, R.drawable.icon_document};

    private static final int[] titles = {R.string.text_detection, R.string.icr, R.string.bcr, R.string.ucr,R.string.document_recognition};

    @Override
    protected void initClickEvent(View view) {
        if (this.mGridView == null) {
            return;
        }
        this.mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // Text recognition
                        startActivity(new Intent(getActivity(), TextRecognitionActivity.class));
                        break;
                    case 1:
                        // ICR
                        startActivity(new Intent(getActivity(), IDCardRecognitionActivity.class));
                        break;
                    case 2:
                        // BCR
                        //startActivity(new Intent(getActivity(), BankCardRecognitionActivity.class));
                        break;
                    case 3:
                        // UCR
                        //startActivity(new Intent(getActivity(), GeneralCardRecognitionActivity.class));
                        break;
                    case 4:
                        // Document recognition
                        final Intent intent = new Intent(getActivity(), RemoteDetectionActivity.class);
                        intent.putExtra(Constant.MODEL_TYPE, Constant.CLOUD_DOCUMENT_TEXT_DETECTION);
                        startActivity(intent);
                        break;
                    default:
                        Toast.makeText(getActivity(), R.string.coming_soon, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Override
    protected void initData() {
        this.mDataList = new ArrayList<GridViewItem>();
        GridViewItem item;
        for (int i = 0; i < icons.length; i++) {
            item = new GridViewItem(icons[i], titles[i]);
            this.mDataList.add(item);
        }
    }
}
