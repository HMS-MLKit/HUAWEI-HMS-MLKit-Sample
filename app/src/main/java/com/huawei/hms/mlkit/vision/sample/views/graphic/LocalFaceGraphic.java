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
import android.graphics.Typeface;

import com.huawei.hms.mlkit.vision.sample.views.overlay.GraphicOverlay;
import com.huawei.hms.mlsdk.common.MLPosition;
import com.huawei.hms.mlsdk.face.MLFace;
import com.huawei.hms.mlsdk.face.MLFaceShape;
import com.huawei.hms.mlsdk.face.MLFaceKeyPoint;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LocalFaceGraphic extends BaseGraphic {
    private static final float BOX_STROKE_WIDTH = 6.0f;
    private static final float LINE_WIDTH = 5.0f;

    private final GraphicOverlay overlay;

    private final Paint facePositionPaint;
    private final Paint landmarkPaint;
    private final Paint boxPaint;

    private final Paint facePaint;
    private final Paint eyePaint;
    private final Paint eyebrowPaint;
    private final Paint lipPaint;
    private final Paint nosePaint;
    private final Paint noseBasePaint;
    private final Paint textPaint;
    private final Paint probabilityPaint;
    private boolean isLandScape;
    private boolean isOpenFeatures;

    private volatile MLFace face;

    public LocalFaceGraphic(GraphicOverlay overlay, MLFace face, boolean isLandScape, boolean isOpenFeatures) {
        super(overlay);

        this.isLandScape = isLandScape;
        this.face = face;
        this.overlay = overlay;
        this.isOpenFeatures = isOpenFeatures;
        final int selectedColor = Color.WHITE;

        facePositionPaint = new Paint();
        facePositionPaint.setColor(selectedColor);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(24);//设置字体大小
        textPaint.setTypeface(Typeface.DEFAULT);//设置字体类型

        probabilityPaint = new Paint();
        probabilityPaint.setColor(Color.WHITE);
        probabilityPaint.setTextSize(35);//设置字体大小
        probabilityPaint.setTypeface(Typeface.DEFAULT);//设置字体类型

        landmarkPaint = new Paint();
        landmarkPaint.setColor(Color.RED);
        landmarkPaint.setStyle(Paint.Style.FILL);
        landmarkPaint.setStrokeWidth(10f);

        boxPaint = new Paint();
        boxPaint.setColor(Color.WHITE);
        boxPaint.setStyle(Paint.Style.STROKE);
        boxPaint.setStrokeWidth(BOX_STROKE_WIDTH);

        facePaint = new Paint();
        facePaint.setColor(Color.parseColor("#ffcc66"));
        facePaint.setStyle(Paint.Style.STROKE);
        facePaint.setStrokeWidth(LINE_WIDTH);

        eyePaint = new Paint();
        eyePaint.setColor(Color.parseColor("#00ccff"));
        eyePaint.setStyle(Paint.Style.STROKE);
        eyePaint.setStrokeWidth(LINE_WIDTH);

        eyebrowPaint = new Paint();
        eyebrowPaint.setColor(Color.parseColor("#006666"));
        eyebrowPaint.setStyle(Paint.Style.STROKE);
        eyebrowPaint.setStrokeWidth(LINE_WIDTH);

        nosePaint = new Paint();
        nosePaint.setColor(Color.parseColor("#ffff00"));
        nosePaint.setStyle(Paint.Style.STROKE);
        nosePaint.setStrokeWidth(LINE_WIDTH);

        noseBasePaint = new Paint();
        noseBasePaint.setColor(Color.parseColor("#ff6699"));
        noseBasePaint.setStyle(Paint.Style.STROKE);
        noseBasePaint.setStrokeWidth(LINE_WIDTH);

        lipPaint = new Paint();
        lipPaint.setColor(Color.parseColor("#990000"));
        lipPaint.setStyle(Paint.Style.STROKE);
        lipPaint.setStrokeWidth(LINE_WIDTH);
    }

    public List<String> sortHashMap(HashMap<String, Float> map) {

        Set<Map.Entry<String, Float>> entey = map.entrySet();
        List<Map.Entry<String, Float>> list = new ArrayList<Map.Entry<String, Float>>(entey);
        Collections.sort(list, new Comparator<Map.Entry<String, Float>>() {
            @Override
            public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                if (o2.getValue() - o1.getValue() >= 0) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        List<String> emotions = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            emotions.add(list.get(i).getKey());
        }
        return emotions;
    }

    @Override
    public void draw(Canvas canvas) {
        if (face == null) {
            return;
        }
        float start = 270f;
        float x = start;
        float width = overlay.getWidth() / 2.0f - start;
        float y;
        if (isLandScape) {
            y = overlay.getHeight() - 420.0f;
        } else {
            y = overlay.getHeight() - 180.0f;
        }
        HashMap<String, Float> emotions = new HashMap<>();
        emotions.put("Smiling", face.possibilityOfSmiling());
        emotions.put("Neutral", face.getEmotions().getNeutralProbability());
        emotions.put("Angry", face.getEmotions().getAngryProbability());
        emotions.put("Fear", face.getEmotions().getFearProbability());
        emotions.put("Sad", face.getEmotions().getSadProbability());
        emotions.put("Disgust", face.getEmotions().getDisgustProbability());
        emotions.put("Surprise", face.getEmotions().getSurpriseProbability());
        List<String> result = sortHashMap(emotions);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        if (isOpenFeatures) {
            canvas.drawText("Hat Probability: " + decimalFormat.format(face.getFeatures().getHatProbability()), x, y, probabilityPaint);
            x = x + width;
            String sex = (face.getFeatures().getSexProbability() > 0.5f) ? "Female" : "Male";
            canvas.drawText("Gender: " + sex, x, y, probabilityPaint);
            y = y - 40.0f;
            x = start;
            canvas.drawText("Age: " + face.getFeatures().getAge(), x, y, probabilityPaint);
            x = x + width;
            canvas.drawText("Glass Probability: " + decimalFormat.format(face.getFeatures().getSunGlassProbability()), x, y, probabilityPaint);
            y = y - 40.0f;
            x = start;
            canvas.drawText("Left eye open Probability: " + decimalFormat.format(face.opennessOfLeftEye()), x, y, probabilityPaint);
            x = x + width;
            canvas.drawText("Right eye open Probability: " + decimalFormat.format(face.opennessOfRightEye()), x, y, probabilityPaint);
            x = start;
            y = y - 40.0f;
            canvas.drawText("Moustache Probability: " + decimalFormat.format(face.getFeatures().getMoustacheProbability()), x, y, probabilityPaint);
        }
        x = x + width;
        canvas.drawText("EulerAngleY: " + decimalFormat.format(face.getRotationAngleY()), x, y, probabilityPaint);
        y = y - 40.0f;
        x = start;
        canvas.drawText("EulerAngleZ: " + decimalFormat.format(face.getRotationAngleZ()), x, y, probabilityPaint);
        x = x + width;
        canvas.drawText("EulerAngleX: " + decimalFormat.format(face.getRotationAngleX()), x, y, probabilityPaint);
        y = y - 40.0f;
        x = start;
        if (isOpenFeatures) {
            canvas.drawText(result.get(0), x, y, probabilityPaint);
        }
        if (face.getFaceShapeList() == null) {
            return;
        }
        
        if(isOpenFeatures) {
            List<MLPosition> points = face.getAllPoints();
            for (int i = 0; i < points.size(); i++) {
                MLPosition point = points.get(i);
                if (point == null) {
                    continue;
                }
                canvas.drawPoint(translateX(point.getX().floatValue()), translateY(point.getY().floatValue()), boxPaint);
            }
            return;
        }

        for (MLFaceShape contour : face.getFaceShapeList()) {
            if (contour == null) {
                continue;
            }
            List<MLPosition> points = contour.getPoints();
            for (int i = 0; i < points.size(); i++) {
                MLPosition point = points.get(i);
                if(point == null){
                    continue;
                }
                canvas.drawPoint(translateX(point.getX().floatValue()), translateY(point.getY().floatValue()), boxPaint);
                if (i != (points.size() - 1)) {
                    MLPosition next = points.get(i + 1);
                    if (point.getX() != null && point.getY() != null) {
                        if (i % 3 == 0) {
                            canvas.drawText(i + 1 + "", translateX(point.getX().floatValue()), translateY(point.getY().floatValue()), textPaint);
                        }
                        canvas.drawLines(new float[]{translateX(point.getX().floatValue()), translateY(point.getY().floatValue()),
                                translateX(next.getX().floatValue()), translateY(next.getY().floatValue())}, getPaint(contour));
                    }
                }
            }
        }
        for (MLFaceKeyPoint landmark : face.getFaceKeyPoints()) {
            if (landmark != null) {
                MLPosition point = landmark.getPoint();
                canvas.drawCircle(
                        translateX(point.getX()),
                        translateY(point.getY()),
                        10f, landmarkPaint);
            }
        }
        // draw landmarks
       /* drawLandmarkPosition(canvas, face, MLFaceKeyPoint.MOUTH_BOTTOM);
        drawLandmarkPosition(canvas, face, MLFaceKeyPoint.LEFT_CHEEK);
        drawLandmarkPosition(canvas, face, MLFaceKeyPoint.LEFT_EAR);
        drawLandmarkPosition(canvas, face, MLFaceKeyPoint.MOUTH_LEFT);
        drawLandmarkPosition(canvas, face, MLFaceKeyPoint.LEFT_EYE);
        drawBitmapOverLandmarkPosition(canvas, face, MLFaceKeyPoint.NOSE_BASE);
        drawLandmarkPosition(canvas, face, MLFaceKeyPoint.RIGHT_CHEEK);
        drawLandmarkPosition(canvas, face, MLFaceKeyPoint.RIGHT_EAR);
        drawLandmarkPosition(canvas, face, MLFaceKeyPoint.RIGHT_EYE);
        drawLandmarkPosition(canvas, face, MLFaceKeyPoint.MOUTH_RIGHT);*/
    }

    private Paint getPaint(MLFaceShape contour) {
        switch (contour.getFaceShapeType()) {
            case MLFaceShape.TYPE_LEFT_EYE:
            case MLFaceShape.TYPE_RIGHT_EYE:
                return eyePaint;
            case MLFaceShape.TYPE_BOTTOM_OF_LEFT_EYEBROW:

            case MLFaceShape.TYPE_BOTTOM_OF_RIGHT_EYEBROW:
            case MLFaceShape.TYPE_TOP_OF_LEFT_EYEBROW:
            case MLFaceShape.TYPE_TOP_OF_RIGHT_EYEBROW:
                return eyebrowPaint;
            case MLFaceShape.TYPE_BOTTOM_OF_LOWER_LIP:
            case MLFaceShape.TYPE_TOP_OF_LOWER_LIP:
            case MLFaceShape.TYPE_BOTTOM_OF_UPPER_LIP:
            case MLFaceShape.TYPE_TOP_OF_UPPER_LIP:
                return lipPaint;
            case MLFaceShape.TYPE_BOTTOM_OF_NOSE:
                return noseBasePaint;
            case MLFaceShape.TYPE_BRIDGE_OF_NOSE:
                return nosePaint;
            default:
                return facePaint;
        }
    }
}