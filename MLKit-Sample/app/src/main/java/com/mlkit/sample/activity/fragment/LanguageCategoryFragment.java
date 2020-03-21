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

import com.mlkit.sample.R;
import com.mlkit.sample.activity.TranslatorActivity;
import com.mlkit.sample.activity.entity.GridViewItem;

import java.util.ArrayList;

public class LanguageCategoryFragment extends BaseFragment {

    private static final int[] language_icons = {R.drawable.icon_translate};

    private static final int[] language_titles = {R.string.translate};

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
                        LanguageCategoryFragment.this.startActivity(new Intent(LanguageCategoryFragment.this.getActivity(), TranslatorActivity.class));
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
        for (int i = 0; i < LanguageCategoryFragment.language_icons.length; i++) {
            item = new GridViewItem(LanguageCategoryFragment.language_icons[i], LanguageCategoryFragment.language_titles[i]);
            this.mDataList.add(item);
        }
    }
}

