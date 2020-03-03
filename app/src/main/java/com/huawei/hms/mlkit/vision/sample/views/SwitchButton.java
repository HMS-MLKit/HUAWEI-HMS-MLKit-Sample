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

package com.huawei.hms.mlkit.vision.sample.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.huawei.hms.mlkit.vision.sample.R;

/**
 * Custom switch controls
 */

public class SwitchButton extends View {
    private Bitmap mSwitchIcon;

    private int mSwitchIconWidth;

    private int mSwitchIconXPision;

    private boolean mSwitchButtonCurrentState = false;

    private Paint mPaint;

    private OnSwitchButtonStateChangeListener mListener;

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void setCurrentState(boolean currentState) {
        this.mSwitchButtonCurrentState = currentState;
    }

    private void initView() {
        mSwitchIcon = BitmapFactory.decodeResource(getResources(), R.drawable.swich_slider_new);
        mSwitchIconWidth = mSwitchIcon.getWidth();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#E4E4E4"));
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2);
        // init value
        mSwitchIconXPision = 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mSwitchIconWidth * 2, mSwitchIconWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF re3 = new RectF(0, 0, mSwitchIconWidth * 2, mSwitchIconWidth);
        if (mSwitchButtonCurrentState) {
            mPaint.setColor(getResources().getColor(R.color.button_background));
            mSwitchIconXPision = mSwitchIconWidth - 1;
        } else {
            mPaint.setColor(Color.parseColor("#D3D1D1"));
            mSwitchIconXPision = 0;
        }
        canvas.drawRoundRect(re3, mSwitchIconWidth / 2.0f, mSwitchIconWidth / 2.0f, mPaint);
        canvas.drawBitmap(mSwitchIcon, mSwitchIconXPision, 1.5f, null);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            mSwitchButtonCurrentState = !mSwitchButtonCurrentState;
            mListener.onSwitchButtonStateChange(mSwitchButtonCurrentState);
        }
        invalidate();
        return true;

    }

    /**
     * Set up listener
     *
     * @param listener
     */
    public void setOnSwitchButtonStateChangeListener(OnSwitchButtonStateChangeListener listener) {
        this.mListener = listener;
    }

    public interface OnSwitchButtonStateChangeListener {
        /**
         * Switch state change callback method
         */
        void onSwitchButtonStateChange(boolean state);
    }
}
