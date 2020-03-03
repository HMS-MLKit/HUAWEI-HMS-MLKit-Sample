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

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.mlkit.vision.sample.R;
import com.huawei.hms.mlsdk.langdetect.MLDetectedLang;
import com.huawei.hms.mlsdk.langdetect.MLLangDetectorFactory;
import com.huawei.hms.mlsdk.langdetect.cloud.MLRemoteLangDetector;
import com.huawei.hms.mlsdk.langdetect.cloud.MLRemoteLangDetectorSetting;
import com.huawei.hms.mlsdk.translate.MLTranslatorFactory;
import com.huawei.hms.mlsdk.translate.cloud.MLRemoteTranslateSetting;
import com.huawei.hms.mlsdk.translate.cloud.MLRemoteTranslator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class TranslatorActivity extends BaseActivity {
    private static final String TAG = "TranslatorActivity";
    private static final String[] SOURCE_LANGUAGE_CODE = new String[]{"Auto", "ZH", "EN", "FR", "ES", "AR", "TH", "TR",};
    private static final String[] DEST_LANGUAGE_CODE = new String[]{"ZH", "EN", "FR", "ES", "AR", "TH", "TR"};
    private static final List<String> spSourceList = new ArrayList<>(Arrays.asList("自动检测", "中文", "英文", "法语", "西班牙语", "阿拉伯语", "泰语", "土耳其语"));
    private static final List<String> spSourceList_en = new ArrayList<>(Arrays.asList("Auto", "Chinese", "English", "French", "Spanish", "Arabic", "Thai", "Turkish"));
    private static final List<String> spDestList = new ArrayList<>(Arrays.asList("中文", "英文", "法语", "西班牙语", "阿拉伯语", "泰语", "土耳其语"));
    private static final List<String> spDestList_en = new ArrayList<>(Arrays.asList("Chinese", "English", "French", "Spanish", "Arabic", "Thai", "Turkish"));
    private static final List<String> codeList = new ArrayList<>(Arrays.asList("ar", "de", "en", "es", "fr", "it", "ja", "pt", "ru", "th", "tr", "zh"));
    private static final List<String> languageList = new ArrayList<>(Arrays.asList("Arabic", "German", "English", "Spanish", "French", "Italian",
            "Japanese", "Portuguese", "Russian", "Thai", "Turkish", "Chinese"));


    private Spinner spSourceType;
    private Spinner spDestType;
    private EditText etInputString;
    private TextView tvOutputString;
    private Button btrTranslator;
    private Button btrIdentification;
    private ImageButton btrSwitchLang;
    private TextView tvTime;
    private TextView tvInputLen;
    private TextView tvOutputLen;

    private String srcLanguage = "Auto";
    private String dstLanguage = "EN";
    public static final String EN = "en";

    private View.OnClickListener listener;

    private ArrayAdapter<String> spSourceAdapter;
    private ArrayAdapter<String> spDestAdapter;

    private MLRemoteTranslateSetting mlRemoteTranslateSetting;
    private MLRemoteTranslator mlRemoteTranslator;
    private MLRemoteLangDetectorSetting mlRemoteLangDetectorSetting;
    private MLRemoteLangDetector mlRemoteLangDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translator);
        createComponent();
        createSpinner();
        bindEventListener();
    }

    private void createSpinner() {
        if (isEngLanguage()) {
            spSourceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spSourceList_en);
            spDestAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spDestList_en);
        } else {
            spSourceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spSourceList);
            spDestAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spDestList);
        }

        spSourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSourceType.setAdapter(spSourceAdapter);

        spDestAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDestType.setAdapter(spDestAdapter);

        spSourceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                srcLanguage = SOURCE_LANGUAGE_CODE[position];
                Log.i(TAG, "srcLanguage: " + srcLanguage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        spDestType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dstLanguage = DEST_LANGUAGE_CODE[position];
                Log.i(TAG, "dstLanguage: " + dstLanguage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void updateSourceLanguage(String code) {
        int count = spSourceAdapter.getCount();
        for (int i = 0; i < count; i++) {
            if (getLanguageName(code).equalsIgnoreCase(spSourceAdapter.getItem(i))) {
                spSourceType.setSelection(i, true);
                return;
            }
        }
        spSourceType.setSelection(0, true);
    }

    private void updateDestLanguage(String code) {
        if (code.equalsIgnoreCase(SOURCE_LANGUAGE_CODE[0]) || code.equalsIgnoreCase(spSourceList.get(0))) {
            dstLanguage = DEST_LANGUAGE_CODE[0];
            return;
        }
        int count = spDestAdapter.getCount();
        for (int i = 0; i < count; i++) {
            if (getLanguageName(code).equalsIgnoreCase(spDestAdapter.getItem(i))) {
                spDestType.setSelection(i, true);
                return;
            }
        }
        spDestType.setSelection(0, true);
    }

    private void createComponent() {
        etInputString = findViewById(R.id.et_input);
        tvOutputString = findViewById(R.id.tv_output);
        btrTranslator = findViewById(R.id.btn_translator);
        btrIdentification = findViewById(R.id.btn_identification);
        tvTime = findViewById(R.id.tv_time);
        tvInputLen = findViewById(R.id.tv_src_len);
        tvOutputLen = findViewById(R.id.tv_dst_len);
        spSourceType = findViewById(R.id.spSourceType);
        spDestType = findViewById(R.id.spDestType);
        btrSwitchLang = findViewById(R.id.buttonSwitchLang);
        updateLength(tvInputLen, etInputString.getText().length());
    }

    private void bindEventListener() {
        etInputString.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence str, int start, int before, int count) {
                updateLength(tvInputLen, str.length());
                autoUpdateSourceLanguage();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        listener = new MyListener();
        btrTranslator.setOnClickListener(listener);
        btrIdentification.setOnClickListener(listener);
        btrSwitchLang.setOnClickListener(listener);
        findViewById(R.id.back).setOnClickListener(listener);
    }

    public boolean isEngLanguage() {
        Locale locale = Locale.getDefault();
        if (locale != null) {
            String strLan = locale.getLanguage();
            return strLan != null && EN.equals(strLan);
        }
        return false;
    }

    private void updateLength(TextView view, int length) {
        view.setText(String.format(Locale.ENGLISH, "%d words", length));
    }

    /**
     * 更新输出文本内容
     *
     * @param text 待更新的内容
     */
    private void updateOutputText(final String text) {
        if (text == null || text.isEmpty()) {
            Log.w(TAG, "updateOutputText: text is empty");
            return;
        }

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvOutputString.setText(String.format(Locale.ENGLISH, text));
                updateLength(tvOutputLen, text.length());
            }
        });
    }

    private void updateInputText(final String text) {
        if (text == null || text.isEmpty()) {
            Log.w(TAG, "updateInputText: text is empty");
            return;
        }

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                etInputString.setText(String.format(Locale.ENGLISH, text));
                updateLength(tvInputLen, text.length());
            }
        });
    }

    /**
     * 获取输入文本框中内容
     *
     * @return string
     */
    private String getInputText() {
        return etInputString.getText().toString();
    }

    private String getSourceType() {
        return srcLanguage;
    }

    private String getDestType() {
        return dstLanguage;
    }

    public class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.btn_translator:
                    doTranslate();
                    break;
                case R.id.btn_identification:
                    doLanguageRecognition();
                    break;
                case R.id.buttonSwitchLang:
                    doLanguageSwitch();
                    break;
                default:
                    break;
            }
        }
    }

    private void updateTime(long time) {
        tvTime.setText(time + " ms");
    }


    private void doLanguageSwitch() {
        String str = srcLanguage;
        srcLanguage = dstLanguage;
        dstLanguage = str;
        updateSourceLanguage(srcLanguage);
        updateDestLanguage(dstLanguage);
        String inputStr = tvOutputString.getText().toString();
        String outputStr = etInputString.getText().toString();
        updateInputText(inputStr);
        updateOutputText(outputStr);

    }

    private void doTranslate() {
        // 执行翻译操作，获取数据，更新输出框
        // 1.创建文本翻译器。可以通过文本翻译器自定义参数类“MLRemoteTranslateSetting”创建翻译器。
        /**
         *setSourceLangId 设置源语言的id
         * setTargetLangId 设置目标语言的id
         * */
        String sourceText = getInputText();
        String sourceLang = getSourceType();
        String targetLang = getDestType();

        mlRemoteTranslateSetting = new MLRemoteTranslateSetting.Factory()
                .setSourceLangCode(sourceLang)
                .setTargetLangCode(targetLang)
                .create();
        mlRemoteTranslator = MLTranslatorFactory.getInstance().getRemoteTranslator(mlRemoteTranslateSetting);
        // 2.	进行文本翻译
        // sourceText是待翻译的文本
        final long startTime = System.currentTimeMillis();
        Task<String> task = mlRemoteTranslator.asyncTranslate(sourceText);
        task.addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String text) {
                long endTime = System.currentTimeMillis();
                // 识别成功的处理逻辑
                updateOutputText(text);
                updateTime(endTime - startTime);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // 识别失败的处理逻辑
                updateOutputText(e.getMessage());
            }
        });

        autoUpdateSourceLanguage();
    }

    private void autoUpdateSourceLanguage() {
        mlRemoteLangDetectorSetting = new MLRemoteLangDetectorSetting.Factory().setTrustedThreshold(0.01f).create();
        mlRemoteLangDetector = MLLangDetectorFactory.getInstance().getRemoteLangDetector(mlRemoteLangDetectorSetting);
        Task<List<MLDetectedLang>> probabilityDetectTask = mlRemoteLangDetector.probabilityDetect(getInputText());
        probabilityDetectTask.addOnSuccessListener(new OnSuccessListener<List<MLDetectedLang>>() {
            @Override
            public void onSuccess(List<MLDetectedLang> result) {
                // 设置源语言
                MLDetectedLang recognizedLang = result.get(0);
                String langCode = recognizedLang.getLangCode();
                updateSourceLanguage(langCode);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
            }
        });
    }

    private void doLanguageRecognition() {
        mlRemoteLangDetectorSetting = new MLRemoteLangDetectorSetting.Factory().setTrustedThreshold(0.01f).create();
        mlRemoteLangDetector = MLLangDetectorFactory.getInstance().getRemoteLangDetector(mlRemoteLangDetectorSetting);
        Task<List<MLDetectedLang>> probabilityDetectTask = mlRemoteLangDetector.probabilityDetect(getInputText());
        final long startTime = System.currentTimeMillis();
        probabilityDetectTask.addOnSuccessListener(new OnSuccessListener<List<MLDetectedLang>>() {
            @Override
            public void onSuccess(List<MLDetectedLang> result) {
                long endTime = System.currentTimeMillis();
                // 识别成功的处理逻辑
                StringBuilder sb = new StringBuilder();
                for (MLDetectedLang recognizedLang : result) {
                    String langCode = recognizedLang.getLangCode();
                    float probability = recognizedLang.getProbability();
                    sb.append("Language=" + getEnLanguageName(langCode) +"(" + langCode + "), score=" + probability);
                    sb.append(".");
                }
                updateOutputText(sb.toString());
                updateTime(endTime - startTime);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // 识别失败的处理逻辑
                updateOutputText(e.getMessage());
            }
        });
        // 3.释放资源
        mlRemoteLangDetector.stop();
    }

    private String getLanguageName(String code) {
        int index = 0;
        for (int i = 0; i < SOURCE_LANGUAGE_CODE.length; i++) {
            if (code.equalsIgnoreCase(SOURCE_LANGUAGE_CODE[i])) {
                index = i;
                break;
            }
        }
        return spSourceAdapter.getItem(index);
    }

    private String getEnLanguageName(String code) {
        int index = 0;
        for (int i = 0; i < codeList.size(); i++) {
            if (code.equalsIgnoreCase(codeList.get(i))) {
                index = i;
                return languageList.get(index);
            }
        }
        return code;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 3.释放资源
        if (mlRemoteTranslator != null) {
            mlRemoteTranslator.stop();
        }
        if (mlRemoteLangDetector != null) {
            mlRemoteLangDetector.stop();
        }
    }
}
