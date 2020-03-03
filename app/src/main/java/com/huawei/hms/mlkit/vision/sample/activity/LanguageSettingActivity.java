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

package com.huawei.hms.mlkit.vision.sample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.huawei.hms.mlkit.vision.sample.R;
import com.huawei.hms.mlkit.vision.sample.util.Constant;
import com.huawei.hms.mlkit.vision.sample.util.SharedPreferencesUtil;

public class LanguageSettingActivity extends BaseActivity implements View.OnClickListener {
    private ImageView simpleCnImg;
    private ImageView englishImg;
    private ImageView japaneseImg;
    private ImageView koImg;
    private ImageView latinImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_language_setting);
        this.simpleCnImg = this.findViewById(R.id.simple_cn_img);
        this.englishImg = this.findViewById(R.id.english_img);
        this.japaneseImg = this.findViewById(R.id.japanese_img);
        this.koImg = this.findViewById(R.id.korean_img);
        this.latinImg = this.findViewById(R.id.latin_img);
        this.findViewById(R.id.simple_cn).setOnClickListener(this);
        this.findViewById(R.id.english).setOnClickListener(this);
        this.findViewById(R.id.japanese).setOnClickListener(this);
        this.findViewById(R.id.korean).setOnClickListener(this);
        this.findViewById(R.id.latin).setOnClickListener(this);
        this.findViewById(R.id.back).setOnClickListener(this);
        this.initData();
        //setStatusBar();
    }

    private void initData() {
        String spPostion = SharedPreferencesUtil.getInstance(this).
                getStringValue(Constant.POSITION_KEY);
        switch (spPostion){
            case Constant.POSITION_CN:
                this.simpleCnImg.setImageResource(R.mipmap.selected);
                break;
            case Constant.POSITION_EN:
                this.englishImg.setImageResource(R.mipmap.selected);
                break;
            case Constant.POSITION_JA:
                this.japaneseImg.setImageResource(R.mipmap.selected);
                break;
            case Constant.POSITION_KO:
                this.koImg.setImageResource(R.mipmap.selected);
                break;
            case Constant.POSITION_LA:
                this.latinImg.setImageResource(R.mipmap.selected);
                break;
            default:
                if (Constant.IS_CHINESE) {
                    this.simpleCnImg.setImageResource(R.mipmap.selected);
                } else {
                    this.englishImg.setImageResource(R.mipmap.selected);
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.simple_cn:
                SharedPreferencesUtil.getInstance(this).
                        putStringValue(Constant.POSITION_KEY, Constant.POSITION_CN);
                intent.putExtra("language_result", this.getString(R.string.simplified_chinese));
                break;
            case R.id.english:
                SharedPreferencesUtil.getInstance(this).
                        putStringValue(Constant.POSITION_KEY, Constant.POSITION_EN);
                intent.putExtra("language_result", this.getString(R.string.english_choose));
                break;
            case R.id.japanese:
                SharedPreferencesUtil.getInstance(this).
                        putStringValue(Constant.POSITION_KEY, Constant.POSITION_JA);
                intent.putExtra("language_result", this.getString(R.string.japanese_choose));
                break;
            case R.id.korean:
                SharedPreferencesUtil.getInstance(this).
                        putStringValue(Constant.POSITION_KEY, Constant.POSITION_KO);
                intent.putExtra("language_result", this.getString(R.string.korean_choose));
                break;
            case R.id.latin:
                SharedPreferencesUtil.getInstance(this).
                        putStringValue(Constant.POSITION_KEY, Constant.POSITION_LA);
                intent.putExtra("language_result", this.getString(R.string.latin_choose));
                break;
            case R.id.back:
                this.finish();
                break;
            default:
                break;
        }
        this.setResult(1001, intent);
        this.finish();
    }
}
