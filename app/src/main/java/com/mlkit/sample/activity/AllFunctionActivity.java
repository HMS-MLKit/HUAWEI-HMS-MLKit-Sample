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

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import android.widget.TextView;

import com.mlkit.sample.R;
import com.mlkit.sample.activity.adapter.TabFragmentAdapter;
import com.mlkit.sample.activity.fragment.LanguageCategoryFragment;
import com.mlkit.sample.activity.fragment.OtherCategoryFragment;
import com.mlkit.sample.activity.fragment.PictureCategoryFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class AllFunctionActivity extends BaseActivity implements View.OnClickListener {

    private List<Fragment> mFragmentList;
    private TextView mBgChangeTv;
    private TextView mCaptureImgTv;
    private TextView mSliceTv;
    private ViewPager mViewPager;
    private View mBgChangeLine;
    private View mCaptureImgLine;
    private View mSliceLine;
    private TabFragmentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_all_function);
        this.initView();
        this.setStatusBar();
        this.setStatusBarFontColor();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.mBgChangeTv = this.findViewById(R.id.fragment_one);
        this.mCaptureImgTv = this.findViewById(R.id.fragment_two);
        this.mSliceTv = this.findViewById(R.id.fragment_three);
        this.mBgChangeLine = this.findViewById(R.id.line_one);
        this.mCaptureImgLine = this.findViewById(R.id.line_two);
        this.mSliceLine = this.findViewById(R.id.line_three);
        this.mBgChangeTv.setOnClickListener(this);
        this.mCaptureImgTv.setOnClickListener(this);
        this.mSliceTv.setOnClickListener(this);
        this.mViewPager = this.findViewById(R.id.view_pager);
        // Bind click event.
        this.mViewPager.setOnPageChangeListener(new PagerChangeListener());
        // Add fragment to the collection list.
        this.mFragmentList = new ArrayList<>();
        this.mFragmentList.add(new PictureCategoryFragment());
        this.mFragmentList.add(new LanguageCategoryFragment());
        this.mFragmentList.add(new OtherCategoryFragment());

        this.mAdapter = new TabFragmentAdapter(this.getSupportFragmentManager(), this.mFragmentList);
        this.mViewPager.setAdapter(this.mAdapter);
        this.mViewPager.setCurrentItem(0);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_one:
                this.mViewPager.setCurrentItem(0);
                this.setBgChangeView();
                break;
            case R.id.fragment_two:
                this.mViewPager.setCurrentItem(1);
                this.setCaptureImageView();
                break;
            case R.id.fragment_three:
                this.mViewPager.setCurrentItem(2);
                this.setSliceImageView();
                break;
            case R.id.back:
                this.finish();
                break;
            default:
                break;
        }
    }

    private void setBgChangeView() {
        this.mBgChangeTv.setTextColor(this.getResources().getColor(R.color.button_background));
        this.mCaptureImgTv.setTextColor(Color.BLACK);
        this.mSliceTv.setTextColor(Color.BLACK);
        this.mBgChangeLine.setVisibility(View.VISIBLE);
        this.mCaptureImgLine.setVisibility(View.GONE);
        this.mSliceLine.setVisibility(View.GONE);
    }

    private void setCaptureImageView() {
        this.mBgChangeTv.setTextColor(Color.BLACK);
        this.mCaptureImgTv.setTextColor(this.getResources().getColor(R.color.button_background));
        this.mSliceTv.setTextColor(Color.BLACK);
        this.mBgChangeLine.setVisibility(View.GONE);
        this.mCaptureImgLine.setVisibility(View.VISIBLE);
        this.mSliceLine.setVisibility(View.GONE);
    }

    private void setSliceImageView() {
        this.mBgChangeTv.setTextColor(Color.BLACK);
        this.mCaptureImgTv.setTextColor(Color.BLACK);
        this.mSliceTv.setTextColor(this.getResources().getColor(R.color.button_background));
        this.mBgChangeLine.setVisibility(View.GONE);
        this.mCaptureImgLine.setVisibility(View.GONE);
        this.mSliceLine.setVisibility(View.VISIBLE);
    }

    /**
     * Set a ViewPager listening event. When the ViewPager is swiped left or right, the menu bar selected state changes accordingly.
     */
    public class PagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    AllFunctionActivity.this.setBgChangeView();
                    break;
                case 1:
                    AllFunctionActivity.this.setCaptureImageView();
                    break;
                case 2:
                    AllFunctionActivity.this.setSliceImageView();
                    break;
                default:
                    break;
            }
        }
    }

    public void onBackPressed(View view) {
        this.finish();
    }
}
