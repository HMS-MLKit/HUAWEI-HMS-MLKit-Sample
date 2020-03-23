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

/*package com.huawei.hms.mlkit.sample.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;

import com.huawei.hms.mlplugin.card.bcr.MLBcrCaptureConfig;

import java.util.HashMap;
import java.util.Map;

*/
/**
 *
 * @since 2020-03-16
 *//*

public class MyBlankBlockDrawer implements MLBcrCaptureConfig.IBlankBlockDrawer {
    private static final String TAG = "MyBlankBlockDrawer";

    private static final int ID_SUPPORT_BANK = 1;

    private static final int ID_INPUT_MANUAL = 2;

    @Override
    public boolean onClick(Context context, int id) {
        Log.i(TAG, "onClick = " + id);

        if (id == ID_INPUT_MANUAL) {
            return true;
        }

        return false;
    }

    @Override
    public Map<Integer, Rect> draw(Context context, Canvas canvas, Rect rect) {
        Map<Integer, Rect> controlsMap = new HashMap<Integer, Rect>();
        drawWhatYouWant(context, canvas, rect);
        controlsMap.putAll(drawSupportBankButton(context, canvas, rect));
        controlsMap.putAll(drawInputManual(context, canvas, rect));

        return controlsMap;
    }

    private void drawWhatYouWant(Context context, Canvas canvas, Rect rect) {
        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        canvas.save();
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.argb(96, 255, 0, 0));

        Rect transfer = new Rect(0, 0, rect.width(), rect.height());
        canvas.translate(0, rect.top);
        canvas.drawRect(transfer, paint);

        final String text = "自定义绘制区域";
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.argb(160, 255, 0, 0));
        paint.setTextSize(40);

        final Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        canvas.translate(textRect.width() / 2, textRect.height());
        canvas.drawText(text, 0, 0, paint);
        canvas.restore();
    }

    private Map<Integer, Rect> drawSupportBankButton(Context context, Canvas canvas, Rect rect) {
        canvas.save();
        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        final float scale = metrics.density / 1.5F;
        final String text = "点点试，反正没用（自定义）";
        final int textSize = Math.round(22 * scale);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.argb(160, 255, 0, 0));
        paint.setTextSize(textSize);

        final int dx = (rect.left + rect.width()) / 2;
        final int dy = rect.top + rect.height() / 4;

        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        canvas.translate(dx, dy);
        canvas.drawText(text, 0.0F, 0.0F, paint);
        canvas.restore();

        final int left = dx - textRect.width() / 2;
        final int top = dy - textRect.height() / 2;
        final int right = dx + textRect.width() / 2;
        final int bottom = dy + textRect.height() / 2;

        Map<Integer, Rect> controlMap = new HashMap<Integer, Rect>();
        controlMap.put(ID_SUPPORT_BANK, new Rect(left, top, right, bottom));
        return controlMap;
    }

    private Map<Integer, Rect> drawInputManual(Context context, Canvas canvas, Rect rect) {
        canvas.save();
        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        final float scale = metrics.density / 1.5F;
        final String text = "点我返回（自定义）";
        final int textSize = Math.round(22 * scale);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.argb(160, 255, 0, 0));
        paint.setTextSize(textSize);

        final int dx = (rect.left + rect.width()) / 2;
        final int dy = rect.top + rect.height() * 3 / 4;

        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        final int left = dx - textRect.width() / 2;
        final int top = dy - textRect.height() / 2;
        final int right = dx + textRect.width() / 2;
        final int bottom = dy + textRect.height() / 2;

        canvas.translate(dx, dy);
        canvas.drawText(text, 0.0F, 0.0F, paint);
        canvas.restore();

        Map<Integer, Rect> controlMap = new HashMap<Integer, Rect>();
        controlMap.put(ID_INPUT_MANUAL, new Rect(left, top, right, bottom));
        return controlMap;
    }
}
*/
