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

package com.huawei.hms.mlkit.vision.sample.views.color;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.huawei.hms.mlkit.vision.sample.R;
import com.huawei.hms.mlkit.vision.sample.util.Constant;

public class ColorSelector extends View {
    // By default, the ratio of long edge to short edge is 6:1
    private static final int mShortSize = 70;

    private static final int mLongSize = 420;

    /**
     * Color bar fillet rectangle border
     */
    private final Rect mRect = new Rect();

    /**
     * mBitmapForIndicator where to draw on the view
     */
    private final Rect mIndicatorRect = new Rect();
    /**
     * Indicator point color
     */
    private int mIndicatorColor;

    /**
     * Paint for view and mBitmapforcolor
     */
    private Paint mPaint = null;

    /**
     * Paint for indicator
     */
    private Paint mIndicatorPaint = null;

    private LinearGradient mLinearGradient;

    private int mTop, mLeft, mRight, mBottom;

    /**
     * 指示点半径
     */
    private int mRadius;

    private Bitmap mBitmapForColor;

    private Bitmap mBitmapForIndicator;

    private boolean mIsNeedReDrawColorTable = true;

    private boolean mIsNeedReDrawIndicator = true;

    private int mCurrentX, mCurrentY;

    private int[] mColors = null;

    private int mCurrentColor;


    private OnColorSelectorChangeListener mColorSelectorChangeListener;

    public ColorSelector(Context context) {
        super(context);
    }

    public ColorSelector(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorSelector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBitmapForColor = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        mBitmapForIndicator = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);

        // setShadowLayer is invalid when hardware acceleration is turned on. Hardware acceleration needs to be turned off
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mIndicatorPaint = new Paint();
        mIndicatorPaint.setAntiAlias(true);

        mCurrentX = mCurrentY = Integer.MAX_VALUE;

        final TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ColorSelector, defStyleAttr, 0);
        mIndicatorColor = array.getColor(R.styleable.ColorSelector_indicatorColor, Color.WHITE);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;

        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        } else {// Set the width is warp_content in XML
            width = getSuggestedMinimumWidth() + getPaddingLeft() + getPaddingRight();
        }

        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            height = getSuggestedMinimumHeight() + getPaddingTop() + getPaddingBottom();
        }

        width = Math.max(width, mLongSize);
        height = Math.max(height, mShortSize);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mTop = getPaddingTop();
        mLeft = getPaddingLeft();
        mBottom = getMeasuredHeight() - getPaddingBottom();
        mRight = getMeasuredWidth() - getPaddingRight();
        if (mCurrentX == mCurrentY || mCurrentY == Integer.MAX_VALUE) {
            mCurrentX = getWidth() / 2;
            mCurrentY = getHeight() / 2;
        }

        calculateBounds();
        if (mColors == null) {
            setColors(Constant.COLOR_TABLE);
        } else {
            setColors(mColors);
        }
        createBitmap();

        mIsNeedReDrawIndicator = true;

    }

    private void createBitmap() {
        if (mBitmapForColor != null) {
            if (!mBitmapForColor.isRecycled()) {
                mBitmapForColor.recycle();
                mBitmapForColor = null;
            }
        }

        if (mBitmapForIndicator != null) {
            if (!mBitmapForIndicator.isRecycled()) {
                mBitmapForIndicator.recycle();
                mBitmapForIndicator = null;
            }
        }

        mBitmapForColor = Bitmap.createBitmap(mRect.width(), mRect.height(), Bitmap.Config.ARGB_8888);
        mBitmapForIndicator = Bitmap.createBitmap(mRadius * 2, mRadius * 2, Bitmap.Config.ARGB_8888);
    }

    /**
     * Calculate color bar boundaries
     */
    private void calculateBounds() {
        int average = 9;

        int height = mBottom - mTop;
        int width = mRight - mLeft;
        int size = Math.min(width, height);

        if (width <= height) { // Width is smaller than height, recalculate height in the way of 6:1
            size = width / 6;
        }

        int each = size / average;
        mRadius = each * 7 / 2;

        int top, left, bottom, right;
        int offset = each * 3 / 2;

        left = mLeft + mRadius;
        right = mRight - mRadius;

        top = (getHeight() / 2) - offset;
        bottom = (getHeight() / 2) + offset;

        mRect.set(left, top, right, bottom);
    }

    /**
     * Set the gradient color of the color bar
     *
     * @param colors color value
     */
    public void setColors(int... colors) {
        mLinearGradient = null;
        this.mColors = colors;

        mLinearGradient = new LinearGradient(
                mRect.left, mRect.top,
                mRect.right, mRect.top,
                colors,
                null,
                Shader.TileMode.CLAMP
        );
        mIsNeedReDrawColorTable = true;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mIsNeedReDrawColorTable) {
            createColorTableBitmap();
        }
        canvas.drawBitmap(mBitmapForColor, null, mRect, mPaint);

        if (mIsNeedReDrawIndicator) {
            createIndicatorBitmap();
        }
        // Draw indicator points
        mIndicatorRect.set(mCurrentX - mRadius, mCurrentY - mRadius, mCurrentX + mRadius, mCurrentY + mRadius);
        canvas.drawBitmap(mBitmapForIndicator, null, mIndicatorRect, mPaint);
    }

    private void createIndicatorBitmap() {
        mIndicatorPaint.setColor(mIndicatorColor);
        int radius = 3;
        mIndicatorPaint.setShadowLayer(radius, 0, 0, Color.GRAY);

        Canvas canvas = new Canvas(mBitmapForIndicator);
        canvas.drawCircle(mRadius, mRadius, mRadius - radius, mIndicatorPaint);
        mIsNeedReDrawIndicator = false;
    }

    private void createColorTableBitmap() {
        Canvas canvas = new Canvas(mBitmapForColor);
        RectF rf = new RectF(0, 0, mBitmapForColor.getWidth(), mBitmapForColor.getHeight());

        int radius;
        radius = mBitmapForColor.getHeight() / 2;

        mPaint.setColor(Color.BLACK);
        canvas.drawRoundRect(rf, radius, radius, mPaint);

        mPaint.setShader(mLinearGradient);
        canvas.drawRoundRect(rf, radius, radius, mPaint);
        mPaint.setShader(null);

        mIsNeedReDrawColorTable = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int xValue = (int) event.getX();

        if (!inBoundOfColorTable(xValue)) {
            return true;
        }

        mCurrentX = xValue;
        mCurrentY = getHeight() / 2;

        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            if (mColorSelectorChangeListener != null) {
                mColorSelectorChangeListener.onStartColorSelect(this);
                calculateColor();
                mColorSelectorChangeListener.onColorChanged(this, mCurrentColor);
            }

        } else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            if (mColorSelectorChangeListener != null) {
                mColorSelectorChangeListener.onStopColorSelect(this);
                calculateColor();
                mColorSelectorChangeListener.onColorChanged(this, mCurrentColor);
            }

        } else {
            if (mColorSelectorChangeListener != null) {
                calculateColor();
                mColorSelectorChangeListener.onColorChanged(this, mCurrentColor);
            }
        }

        invalidate();
        return true;
    }

    /**
     * Get the color of the current indicator
     *
     * @return color value
     */
    public int getColor() {
        return calculateColor();
    }

    private boolean inBoundOfColorTable(int xValue) {
        return xValue > mLeft + mRadius && xValue < mRight - mRadius;
    }

    private int calculateColor() {
        int x, y;
        y = (mRect.bottom - mRect.top) / 2;
        if (mCurrentX < mRect.left) {
            x = 1;
        } else if (mCurrentX > mRect.right) {
            x = mBitmapForColor.getWidth() - 1;
        } else {
            x = mCurrentX - mRect.left;
        }
        int pixel = mBitmapForColor.getPixel(x, y);
        mCurrentColor = pixelToColor(pixel);
        return mCurrentColor;
    }

    private int pixelToColor(int pixel) {
        return Color.argb(Color.alpha(pixel), Color.red(pixel), Color.green(pixel), Color.blue(pixel));
    }


    public void setOnColorSelectorChangeListener(OnColorSelectorChangeListener listener) {
        this.mColorSelectorChangeListener = listener;
    }

    public interface OnColorSelectorChangeListener {

        /**
         * Callback when the selected color value changes
         *
         * @param picker ColorSelector
         * @param color  color value
         */
        void onColorChanged(ColorSelector picker, int color);

        /**
         * Start color selection
         *
         * @param picker ColorSelector
         */
        void onStartColorSelect(ColorSelector picker);

        /**
         * Stop color selection
         *
         * @param picker ColorSelector
         */
        void onStopColorSelect(ColorSelector picker);
    }


    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        MySavedState saveState = new MySavedState(parcelable);
        saveState.xValue = mCurrentX;
        saveState.yValue = mCurrentY;
        saveState.colors = mColors;
        saveState.bitmapColorView = mBitmapForColor;
        saveState.bitmapIndicatorView = mBitmapForIndicator;
        return saveState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof MySavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        MySavedState ss = (MySavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        mCurrentX = ss.xValue;
        mCurrentY = ss.yValue;
        mColors = ss.colors;

        mBitmapForColor = ss.bitmapColorView;

        mBitmapForIndicator = ss.bitmapIndicatorView;
        mIsNeedReDrawColorTable = true;
    }

    private class MySavedState extends BaseSavedState {
        int xValue, yValue;
        int[] colors;
        Bitmap bitmapColorView;
        Bitmap bitmapIndicatorView = null;

        MySavedState(Parcelable source) {
            super(source);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(xValue);
            out.writeInt(yValue);
            out.writeParcelable(bitmapColorView, flags);
            out.writeIntArray(colors);
            if (bitmapIndicatorView != null) {
                out.writeParcelable(bitmapIndicatorView, flags);
            }
        }
    }

    public void initData() {
        mIsNeedReDrawIndicator = true;
        mIsNeedReDrawColorTable = true;
        requestLayout();
    }
}
