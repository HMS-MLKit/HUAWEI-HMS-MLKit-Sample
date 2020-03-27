/**
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mlkit.sample.phototranslate;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.huawei.hms.mlsdk.common.internal.client.SmartLog;
import com.mlkit.sample.phototranslate.util.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final String[] SOURCE_LANGUAGE_CODE = new String[]{"Auto", "ZH", "EN", "FR", "ES", "AR", "TH", "TR",};
    private static final String[] DEST_LANGUAGE_CODE = new String[]{"ZH", "EN", "FR", "ES", "AR", "TH", "TR"};
    private static final List<String> spSourceList = new ArrayList<>(Arrays.asList("自动检测", "中文", "英文", "法语", "西班牙语", "阿拉伯语", "泰语", "土耳其语"));
    private static final List<String> spSourceList_en = new ArrayList<>(Arrays.asList("Auto", "Chinese", "English", "French", "Spanish", "Arabic", "Thai", "Turkish"));
    private static final List<String> spDestList = new ArrayList<>(Arrays.asList("中文", "英文", "法语", "西班牙语", "阿拉伯语", "泰语", "土耳其语"));
    private static final List<String> spDestList_en = new ArrayList<>(Arrays.asList("Chinese", "English", "French", "Spanish", "Arabic", "Thai", "Turkish"));
    private static final List<String> codeList = new ArrayList<>(Arrays.asList("ar", "de", "en", "es", "fr", "it", "ja", "pt", "ru", "th", "tr", "zh", "ro"));
    private static final List<String> languageList = new ArrayList<>(Arrays.asList("Arabic", "German", "English", "Spanish", "French", "Italian",
            "Japanese", "Portuguese", "Russian", "Thai", "Turkish", "Chinese", "Romanian"));

    private static final int PERMISSION_REQUESTS = 1;

    private Spinner spSourceType;
    private Spinner spDestType;
    private ImageButton btrSwitchLang;
    private String srcLanguage = "Auto";
    private String dstLanguage = "EN";
    public static final String EN = "en";
    private ArrayAdapter<String> spSourceAdapter;
    private ArrayAdapter<String> spDestAdapter;

    private void createSpinner() {
        if (this.isEngLanguage()) {
            this.spSourceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, MainActivity.spSourceList_en);
            this.spDestAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, MainActivity.spDestList_en);
        } else {
            this.spSourceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, MainActivity.spSourceList);
            this.spDestAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, MainActivity.spDestList);
        }

        this.spSourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spSourceType.setAdapter(this.spSourceAdapter);

        this.spDestAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spDestType.setAdapter(this.spDestAdapter);

        this.spSourceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.this.srcLanguage = MainActivity.SOURCE_LANGUAGE_CODE[position];
                SmartLog.i(MainActivity.TAG, "srcLanguage: " + MainActivity.this.srcLanguage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        this.spDestType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.this.dstLanguage = MainActivity.DEST_LANGUAGE_CODE[position];
                SmartLog.i(MainActivity.TAG, "dstLanguage: " + MainActivity.this.dstLanguage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public boolean isEngLanguage() {
        Locale locale = Locale.getDefault();
        if (locale != null) {
            String strLan = locale.getLanguage();
            return strLan != null && MainActivity.EN.equals(strLan);
        }
        return false;
    }


    private void updateSourceLanguage(String code) {
        int count = this.spSourceAdapter.getCount();
        for (int i = 0; i < count; i++) {
            if (this.getLanguageName(code).equalsIgnoreCase(this.spSourceAdapter.getItem(i))) {
                this.spSourceType.setSelection(i, true);
                return;
            }
        }
        this.spSourceType.setSelection(0, true);
    }

    private void updateDestLanguage(String code) {
        if (code.equalsIgnoreCase(MainActivity.SOURCE_LANGUAGE_CODE[0]) || code.equalsIgnoreCase(MainActivity.spSourceList.get(0))) {
            this.dstLanguage = MainActivity.DEST_LANGUAGE_CODE[0];
            return;
        }
        int count = this.spDestAdapter.getCount();
        for (int i = 0; i < count; i++) {
            if (this.getLanguageName(code).equalsIgnoreCase(this.spDestAdapter.getItem(i))) {
                this.spDestType.setSelection(i, true);
                return;
            }
        }
        this.spDestType.setSelection(0, true);
    }

    private String getLanguageName(String code) {
        int index = 0;
        for (int i = 0; i < MainActivity.SOURCE_LANGUAGE_CODE.length; i++) {
            if (code.equalsIgnoreCase(MainActivity.SOURCE_LANGUAGE_CODE[i])) {
                index = i;
                break;
            }
        }
        return this.spSourceAdapter.getItem(index);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.spSourceType = this.findViewById(R.id.spSourceType);
        this.spDestType = this.findViewById(R.id.spDestType);
        this.btrSwitchLang = this.findViewById(R.id.buttonSwitchLang);
        this.createSpinner();
        this.findViewById(R.id.select_photo).setOnClickListener(this);
        this.btrSwitchLang.setOnClickListener(this);
        if (!allPermissionsGranted()) {
            getRuntimePermissions();
        }
    }

    private void getRuntimePermissions() {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                allNeededPermissions.add(permission);
            }
        }
        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this, allNeededPermissions.toArray(new String[0]), PERMISSION_REQUESTS);
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isPermissionGranted(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            SmartLog.i(TAG, "Permission granted: " + permission);
            return true;
        }
        SmartLog.i(TAG, "Permission NOT granted: " + permission);
        return false;
    }

    private String[] getRequiredPermissions() {
        try {
            PackageInfo info =
                    this.getPackageManager()
                            .getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] ps = info.requestedPermissions;
            if (ps != null && ps.length > 0) {
                return ps;
            } else {
                return new String[0];
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            return new String[0];
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != PERMISSION_REQUESTS) {
            return;
        }
        boolean isNeedShowDiag = false;
        for (int i = 0; i < permissions.length; i++) {
            if ((permissions[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE) && grantResults[i] != PackageManager.PERMISSION_GRANTED)
                    || (permissions[i].equals(Manifest.permission.CAMERA) && grantResults[i] != PackageManager.PERMISSION_GRANTED)) {
                isNeedShowDiag = true;
            }
        }
        if (isNeedShowDiag && !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.camera_permission_rationale))
                    .setPositiveButton(getString(R.string.settings), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            startActivityForResult(intent, 200);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).create();
            dialog.show();
        }
    }

    private void translatePhoto() {
        Intent intent = new Intent(MainActivity.this, RemoteTranslateActivity.class);
        intent.putExtra(Constant.SOURCE_VALUE, this.srcLanguage);
        intent.putExtra(Constant.DEST_VALUE, this.dstLanguage);
        startActivity(intent);
    }

    private void doLanguageSwitch() {
        String str = this.srcLanguage;
        this.srcLanguage = this.dstLanguage;
        this.dstLanguage = str;
        this.updateSourceLanguage(this.srcLanguage);
        this.updateDestLanguage(this.dstLanguage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSwitchLang:
                MainActivity.this.doLanguageSwitch();
                break;
            case R.id.select_photo:
                MainActivity.this.translatePhoto();
                break;
            default:
                break;
        }
    }
}
