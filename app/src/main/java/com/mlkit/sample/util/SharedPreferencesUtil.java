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

package com.mlkit.sample.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
    public static final String TAG = "SharedPreferencesUtil";
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private static SharedPreferencesUtil mSharedPreferencesUtil;


    public SharedPreferencesUtil(Context context) {
        this.mPreferences = context.getSharedPreferences(SharedPreferencesUtil.TAG, Context.MODE_PRIVATE);
        this.mEditor = this.mPreferences.edit();
    }

    public static SharedPreferencesUtil getInstance(Context context) {
        if (SharedPreferencesUtil.mSharedPreferencesUtil == null) {
            synchronized (SharedPreferencesUtil.class) {
                if(SharedPreferencesUtil.mSharedPreferencesUtil == null) {
                    SharedPreferencesUtil.mSharedPreferencesUtil = new SharedPreferencesUtil(context);
                }
            }
        }
        return SharedPreferencesUtil.mSharedPreferencesUtil;
    }

    public void putStringValue(String key, String value) {
        this.mEditor.putString(key, value);
        this.mEditor.commit();
    }

    public String getStringValue(String key) {
        return this.mPreferences.getString(key, Constant.POSITION_EN);
    }

    public void putIntValue(String key, int value) {
        this.mEditor.putInt(key, value);
        this.mEditor.commit();
    }

    public int getIntValue(String key) {
        return this.mPreferences.getInt(key, -1);
    }
}
