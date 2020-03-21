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

import android.graphics.Color;

import com.mlkit.sample.R;

public class Constant {

    public static final String POSITION_KEY = "positionkey";

    public static final String POSITION_CN = "0";

    public static final String POSITION_EN = "1";

    public static final String POSITION_JA = "2";

    public static final String POSITION_KO = "3";

    public static final String POSITION_LA = "4";

    public static final int GET_DATA_SUCCESS = 100;

    public static final int GET_DATA_FAILED = 101;

    public static final int SHOW_TAKE_PHOTO_BUTTON = 102;

    public static final int HIDE_TAKE_PHOTO_BUTTON = 103;

    public static final String CAMERA_FACING = "facing";

    public static final String CLOUD_IMAGE_CLASSIFICATION = "Cloud Classification";
    public static final String CLOUD_LANDMARK_DETECTION = "Landmark";
    public static final String CLOUD_TEXT_DETECTION = "Cloud Text";
    public static final String CLOUD_DOCUMENT_TEXT_DETECTION = "Doc Text";
    public static final String MODEL_TYPE = "model_type";

    public static final String ADD_PICTURE_TYPE = "picture_type";
    public static final String TYPE_TAKE_PHOTO = "take photo";
    public static final String TYPE_SELECT_IMAGE = "select image";

    public static final String DEFAULT_VERSION = "1.0.3.300";
    public static final boolean IS_CHINESE = false;

    public static int[] IMAGES = {R.mipmap.img_001, R.mipmap.img_002, R.mipmap.img_003, R.mipmap.img_004,
            R.mipmap.img_005, R.mipmap.img_006, R.mipmap.img_007, R.mipmap.img_008, R.mipmap.img_009};

    public static int[] COLOR_TABLE = {
            Color.rgb(255, 0, 0),
            Color.rgb(255, 255, 0),
            Color.rgb(0, 255, 0),
            Color.rgb(0, 255, 255),
            Color.rgb(0, 0, 255),
            Color.rgb(255, 0, 255),
            Color.rgb(255, 0, 0)
    };

    /**
     * Number of the background image used in the background replacement.
     */
    public static final String VALUE_KEY = "index_value";

    public interface TYPE {
        int TYPE_BACKGOURND = 0;
        int TYPE_HUMAN = 1;
        int TYPE_SKY = 2;
        int TYPE_GRASS = 3;
        int TYPE_FOOD = 4;
        int TYPE_CAT = 5;
        int TYPE_BUILD = 6;
        int TYPE_FLOWER = 7;
        int TYPE_WATER = 8;
        int TYPE_SAND = 9;
        int TYPE_MOUNTAIN = 10;
        int TYPE_CATEGORY = -1;
    }
}
