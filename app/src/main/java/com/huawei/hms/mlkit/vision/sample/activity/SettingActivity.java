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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.huawei.hms.mlkit.vision.sample.R;
import com.huawei.hms.mlkit.vision.sample.util.Constant;
import com.huawei.hms.mlkit.vision.sample.util.SharedPreferencesUtil;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private TextView mSelectLanguage;

    private TextView mVersion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mSelectLanguage = findViewById(R.id.select_language);
        mVersion = findViewById(R.id.version);
        mVersion.setText(getVersionName());
        findViewById(R.id.language_item).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == 1001) {
            String content = data.getStringExtra("language_result");
            mSelectLanguage.setText(content);
        }
    }

    private void initData() {
        String spPostion = SharedPreferencesUtil.getInstance(this).getStringValue(Constant.POSITION_KEY);
        switch (spPostion) {
            case Constant.POSITION_CN:
                mSelectLanguage.setText(getString(R.string.simplified_chinese));
                break;
            case Constant.POSITION_JA:
                mSelectLanguage.setText(getString(R.string.japanese_choose));
                break;
            case Constant.POSITION_KO:
                mSelectLanguage.setText(getString(R.string.korean_choose));
                break;
            case Constant.POSITION_LA:
                mSelectLanguage.setText(getString(R.string.latin_choose));
                break;
            case Constant.POSITION_EN:
                mSelectLanguage.setText(getString(R.string.english_choose));
                break;
            default:
                if (Constant.IS_CHINESE) {
                    mSelectLanguage.setText(getString(R.string.simplified_chinese));
                } else {
                    mSelectLanguage.setText(getString(R.string.english_choose));
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.language_item:
                Intent intent = new Intent(SettingActivity.this, LanguageSettingActivity.class);
                startActivityForResult(intent, 1000);
                break;
            default:
                break;
        }
    }

    /**
     * get App versionName
     *
     * @return
     */
    public String getVersionName() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String mVersionName = packageInfo.versionName;
            return mVersionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "1.0.3.300";

    }
}
