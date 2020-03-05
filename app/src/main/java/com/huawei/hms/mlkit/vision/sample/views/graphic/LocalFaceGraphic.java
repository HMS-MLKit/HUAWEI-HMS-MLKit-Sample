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

package com.huawei.hms.mlkit.vision.sample.views.graphic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.huawei.hms.mlkit.vision.sample.views.overlay.GraphicOverlay;
import com.huawei.hms.mlsdk.common.MLPosition;
import com.huawei.hms.mlsdk.face.MLFace;
import com.huawei.hms.mlsdk.face.MLFaceKeyPoint;
import com.huawei.hms.mlsdk.face.MLFaceShape;

public class LocalFaceGraphic extends BaseGraphic {
    private static final float BOX_STROKE_WIDTH = 6.0f;
    private static final float LINE_WIDTH = 5.0f;

    private final GraphicOverlay overlay;

    private final Paint eyePaint;
    private final Paint eyebrowPaint;
    private final Paint lipPaint;
    private final Paint nosePaint;
    private final Paint noseBasePaint;
    private final Paint textPaint;
    private final Paint facePaint;
    private final Paint faceFeaturePaintText;
    private final Paint faceFeaturePaint;
    private final Paint landmarkPaint;
    private boolean isLandScape;
    private boolean isOpenFeatures;

    private volatile List<MLFace> faces;
    private volatile MLFace facePaintEmotion;

    public LocalFaceGraphic(GraphicOverlay overlay, List<MLFace> faces, boolean isLandScape, boolean isOpenFeatures) {
        super(overlay);

        this.isLandScape = isLandScape;
        this.faces = faces;
        this.overlay = overlay;
        this.isOpenFeatures = isOpenFeatures;

        this.textPaint = new Paint();
        this.textPaint.setColor(Color.WHITE);
        this.textPaint.setTextSize(24);
        this.textPaint.setTypeface(Typeface.DEFAULT);

        this.faceFeaturePaintText = new Paint();
        this.faceFeaturePaintText.setColor(Color.WHITE);
        this.faceFeaturePaintText.setTextSize(35);
        this.faceFeaturePaintText.setTypeface(Typeface.DEFAULT);

        this.faceFeaturePaint = new Paint();
        this.faceFeaturePaint.setColor(this.faceFeaturePaintText.getColor());
        this.faceFeaturePaint.setStyle(Paint.Style.STROKE);
        this.faceFeaturePaint.setStrokeWidth(LocalFaceGraphic.BOX_STROKE_WIDTH);

        this.facePaint = new Paint();
        this.facePaint.setColor(Color.parseColor("#ffcc66"));
        this.facePaint.setStyle(Paint.Style.STROKE);
        this.facePaint.setStrokeWidth(LocalFaceGraphic.LINE_WIDTH);

        this.landmarkPaint = new Paint();
        this.landmarkPaint.setColor(Color.RED);
        this.landmarkPaint.setStyle(Paint.Style.FILL);
        this.landmarkPaint.setStrokeWidth(10f);

        this.eyePaint = new Paint();
        this.eyePaint.setColor(Color.parseColor("#00ccff"));
        this.eyePaint.setStyle(Paint.Style.STROKE);
        this.eyePaint.setStrokeWidth(LocalFaceGraphic.LINE_WIDTH);

        this.eyebrowPaint = new Paint();
        this.eyebrowPaint.setColor(Color.parseColor("#006666"));
        this.eyebrowPaint.setStyle(Paint.Style.STROKE);
        this.eyebrowPaint.setStrokeWidth(LocalFaceGraphic.LINE_WIDTH);

        this.nosePaint = new Paint();
        this.nosePaint.setColor(Color.parseColor("#ffff00"));
        this.nosePaint.setStyle(Paint.Style.STROKE);
        this.nosePaint.setStrokeWidth(LocalFaceGraphic.LINE_WIDTH);

        this.noseBasePaint = new Paint();
        this.noseBasePaint.setColor(Color.parseColor("#ff6699"));
        this.noseBasePaint.setStyle(Paint.Style.STROKE);
        this.noseBasePaint.setStrokeWidth(LocalFaceGraphic.LINE_WIDTH);

        this.lipPaint = new Paint();
        this.lipPaint.setColor(Color.parseColor("#990000"));
        this.lipPaint.setStyle(Paint.Style.STROKE);
        this.lipPaint.setStrokeWidth(LocalFaceGraphic.LINE_WIDTH);
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
        if (this.faces == null) {
            return;
        }
        float start = 270f;
        float x = start;
        float width = this.overlay.getWidth() / 2.0f - start;
        float y;
        if (this.isLandScape) {
            y = this.overlay.getHeight() - 420.0f;
        } else {
            y = this.overlay.getHeight() - 180.0f;
        }
        // Show all features mode.
        if (this.isOpenFeatures) {
            if (this.faces.size() > 0) {
                List<MLFace> faceFeatures = new ArrayList<MLFace>();
                List<MLFace> faceOthers = new ArrayList<MLFace>();
                faceFeatures.add(this.faces.get(0));
                this.paintFeature(faceFeatures, canvas, x, y, width);
                for (int i = 1; i < this.faces.size(); i++) {
                    // Paint all points in face.
                    List<MLPosition> points = this.faces.get(i).getAllPoints();
                    for (int j = 0; j < points.size(); j++) {
                        MLPosition point = points.get(j);
                        if (point == null) {
                            continue;
                        }
                        canvas.drawPoint(this.translateX(point.getX().floatValue()), this.translateY(point.getY().floatValue()), this.facePaint);
                    }
                }
            }
            return;
        } else {
            this.paintKeyPoint(this.faces, canvas);
        }
    }

    /**
     * Draw all the information of the input faces.
     * @param faces Face information you want to draw.
     * @param canvas
     * @param x X-coordinate of rectangular frame showing face feature information.
     * @param y Y-coordinate of rectangular frame showing face feature information.
     * @param width The width of the rectangular frame displaying the facial feature information.
     */
    private void paintFeature(List<MLFace> faces, Canvas canvas, float x, float y, float width) {
        float start = x;
        for (MLFace face : faces) {
            HashMap<String, Float> emotions = new HashMap<>();
            emotions.put("Smiling", face.possibilityOfSmiling());
            emotions.put("Neutral", face.getEmotions().getNeutralProbability());
            emotions.put("Angry", face.getEmotions().getAngryProbability());
            emotions.put("Fear", face.getEmotions().getFearProbability());
            emotions.put("Sad", face.getEmotions().getSadProbability());
            emotions.put("Disgust", face.getEmotions().getDisgustProbability());
            emotions.put("Surprise", face.getEmotions().getSurpriseProbability());
            List<String> result = this.sortHashMap(emotions);
            DecimalFormat decimalFormat = new DecimalFormat("0.00");

            canvas.drawText("Hat Probability: " + decimalFormat.format(face.getFeatures().getHatProbability()), x, y, this.faceFeaturePaintText);
            x = x + width;
            String sex = (face.getFeatures().getSexProbability() > 0.5f) ? "Female" : "Male";
            canvas.drawText("Gender: " + sex, x, y, this.faceFeaturePaintText);
            y = y - 40.0f;
            x = start;
            canvas.drawText("Age: " + face.getFeatures().getAge(), x, y, this.faceFeaturePaintText);
            x = x + width;
            canvas.drawText("Glass Probability: " + decimalFormat.format(face.getFeatures().getSunGlassProbability()), x, y, this.faceFeaturePaintText);
            y = y - 40.0f;
            x = start;
            canvas.drawText("Left eye open Probability: " + decimalFormat.format(face.opennessOfLeftEye()), x, y, this.faceFeaturePaintText);
            x = x + width;
            canvas.drawText("Right eye open Probability: " + decimalFormat.format(face.opennessOfRightEye()), x, y, this.faceFeaturePaintText);
            x = start;
            y = y - 40.0f;
            canvas.drawText("Moustache Probability: " + decimalFormat.format(face.getFeatures().getMoustacheProbability()), x, y, this.faceFeaturePaintText);
            x = x + width;
            canvas.drawText("EulerAngleY: " + decimalFormat.format(face.getRotationAngleY()), x, y, this.faceFeaturePaintText);
            y = y - 40.0f;
            x = start;
            canvas.drawText("EulerAngleZ: " + decimalFormat.format(face.getRotationAngleZ()), x, y, this.faceFeaturePaintText);
            x = x + width;
            canvas.drawText("EulerAngleX: " + decimalFormat.format(face.getRotationAngleX()), x, y, this.faceFeaturePaintText);
            y = y - 40.0f;
            x = start;
            canvas.drawText(result.get(0), x, y, this.faceFeaturePaintText);
            // Paint all points in face.
            List<MLPosition> points = face.getAllPoints();
            for (int i = 0; i < points.size(); i++) {
                MLPosition point = points.get(i);
                if (point == null) {
                    continue;
                }
                canvas.drawPoint(this.translateX(point.getX().floatValue()), this.translateY(point.getY().floatValue()), this.faceFeaturePaint);
            }
        }
    }

    /**
     * Draw landmarks of the input faces.
     * @param faces Face information you want to draw.
     * @param canvas
     */
    private void paintKeyPoint(List<MLFace> faces, Canvas canvas) {
        for (MLFace face : faces) {
            for (MLFaceShape contour : face.getFaceShapeList()) {
                if (contour == null) {
                    continue;
                }
                List<MLPosition> points = contour.getPoints();
                for (int i = 0; i < points.size(); i++) {
                    MLPosition point = points.get(i);
                    if (point == null) {
                        continue;
                    }
                    canvas.drawPoint(this.translateX(point.getX().floatValue()), this.translateY(point.getY().floatValue()), this.faceFeaturePaint);
                    if (i != (points.size() - 1)) {
                        MLPosition next = points.get(i + 1);
                        if (point.getX() != null && point.getY() != null) {
                            if (i % 3 == 0) {
                                canvas.drawText(i + 1 + "", this.translateX(point.getX().floatValue()), this.translateY(point.getY().floatValue()), this.textPaint);
                            }
                            canvas.drawLines(new float[]{this.translateX(point.getX().floatValue()), this.translateY(point.getY().floatValue()),
                                    this.translateX(next.getX().floatValue()), this.translateY(next.getY().floatValue())}, this.getPaint(contour));
                        }
                    }
                }
            }
            for (MLFaceKeyPoint landmark : face.getFaceKeyPoints()) {
                if (landmark != null) {
                    MLPosition point = landmark.getPoint();
                    canvas.drawCircle(
                            this.translateX(point.getX()),
                            this.translateY(point.getY()),
                            10f, this.landmarkPaint);
                }
            }
        }
    }

    private Paint getPaint(MLFaceShape contour) {
        switch (contour.getFaceShapeType()) {
            case MLFaceShape.TYPE_LEFT_EYE:
            case MLFaceShape.TYPE_RIGHT_EYE:
                return this.eyePaint;

            case MLFaceShape.TYPE_BOTTOM_OF_LEFT_EYEBROW:
            case MLFaceShape.TYPE_BOTTOM_OF_RIGHT_EYEBROW:
            case MLFaceShape.TYPE_TOP_OF_LEFT_EYEBROW:
            case MLFaceShape.TYPE_TOP_OF_RIGHT_EYEBROW:
                return this.eyebrowPaint;

            case MLFaceShape.TYPE_BOTTOM_OF_LOWER_LIP:
            case MLFaceShape.TYPE_TOP_OF_LOWER_LIP:
            case MLFaceShape.TYPE_BOTTOM_OF_UPPER_LIP:
            case MLFaceShape.TYPE_TOP_OF_UPPER_LIP:
                return this.lipPaint;

            case MLFaceShape.TYPE_BOTTOM_OF_NOSE:
                return this.noseBasePaint;

            case MLFaceShape.TYPE_BRIDGE_OF_NOSE:
                return this.nosePaint;

            default:
                return this.facePaint;
        }
    }
}