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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.mlkit.sample.R;
import com.mlkit.sample.activity.adapter.GridViewAdapter;
import com.mlkit.sample.activity.entity.GridViewItem;


import java.util.ArrayList;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    protected GridView mGridView;
    private GridViewAdapter mAdapter;
    protected ArrayList<GridViewItem> mDataList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture, container, false);
        this.initData();
        this.mGridView = view.findViewById(R.id.gridview);
        this.mAdapter = new GridViewAdapter(this.mDataList, this.getContext());
        this.mGridView.setAdapter(this.mAdapter);
        this.initClickEvent(view);
        return view;
    }

    protected abstract void initClickEvent(View view);

    protected abstract void initData();
}
