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

package com.huawei.hms.mlkit.vision.sample.views.graphic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.huawei.hms.mlkit.vision.sample.views.overlay.GraphicOverlay;

import java.util.List;

public class RemoteImageClassificationGraphic extends BaseGraphic {
    private final Paint textPaint;
    private final GraphicOverlay overlay;

    private List<String> classifications;

    public RemoteImageClassificationGraphic(GraphicOverlay overlay, List<String> classifications) {
        super(overlay);
        this.overlay = overlay;
        this.classifications = classifications;
        this.textPaint = new Paint();
        this.textPaint.setColor(Color.WHITE);
        this.textPaint.setTextSize(45.0f);
    }

    @Override
    public synchronized void draw(Canvas canvas) {
        float x = 0f;
        int index = 0;
        float y = this.overlay.getHeight() - 150;

        for (String classification : this.classifications) {
            if (classification.length() > 30) {
                canvas.drawText(classification.substring(0, 30), x, y, this.textPaint);
                y = y - 50.0f;
                canvas.drawText(classification.substring(30), x, y, this.textPaint);
            } else {
                index++;
                if (index == 1) {
                    x = 60f;
                } else if (index == 2) {
                    x = 600f;
                }
                canvas.drawText(classification, x, y, this.textPaint);
                if (index == 2) {
                    y = y - 50.0f;
                    index = 0;
                }
            }
        }
    }
}