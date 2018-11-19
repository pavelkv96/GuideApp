package com.grsu.guideapp.views.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import com.grsu.guideapp.R;

public class CustomProgressBar extends View {

    private Paint mArcPaintBackground;
    private Paint mArcPaintPrimary;
    private final Rect mTextBounds = new Rect();
    private RectF rect = new RectF();
    private int mProgress;
    private Paint mTextPaint;
    private int centerX;
    private int centerY;
    private int mWidthArcBG;
    private int mWidthAcrPrimary;
    private int mTextSizeProgress;

    public CustomProgressBar(Context context) {
        super(context);
        init();
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int viewWidthHeight = MeasureSpec
                .getSize(getResources().getDimensionPixelSize(R.dimen.width_height_progress));
        centerX = viewWidthHeight / 2;
        centerY = viewWidthHeight / 2;
        setMeasuredDimension(viewWidthHeight, viewWidthHeight);
    }

    private void initPixelSize() {
        mWidthArcBG = getResources().getDimensionPixelSize(R.dimen.width_arc_background);
        mWidthAcrPrimary = getResources().getDimensionPixelSize(R.dimen.width_arc_primary);
        mTextSizeProgress = getResources().getDimensionPixelSize(R.dimen.text_size_progress);

    }

    private void init() {
        initPixelSize();
        mArcPaintBackground = new Paint();
        mArcPaintBackground.setDither(true);
        mArcPaintBackground.setStyle(Paint.Style.STROKE);
        mArcPaintBackground.setColor(ContextCompat.getColor(getContext(), R.color.colorArcBG));
        mArcPaintBackground.setStrokeWidth(mWidthArcBG);
        mArcPaintBackground.setAntiAlias(true);

        mArcPaintPrimary = new Paint();
        mArcPaintPrimary.setDither(true);
        mArcPaintPrimary.setStyle(Paint.Style.STROKE);
        mArcPaintPrimary.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        mArcPaintPrimary.setStrokeWidth(mWidthAcrPrimary);
        mArcPaintPrimary.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSizeProgress);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(ContextCompat.getColor(getContext(), android.R.color.black));
        mTextPaint.setStrokeWidth(2);
    }

    public void setProgress(int progress) {
        mProgress = progress;
        invalidate();
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rect.set(20, 20, getWidth() - 20, getHeight() - 20);
        canvas.drawArc(rect, 270, 360, false, mArcPaintBackground);
        canvas.drawArc(rect, 270, /*-*/(3.6f * mProgress), false, mArcPaintPrimary);
        drawTextCentred(canvas);
    }

    public void drawTextCentred(Canvas canvas) {
        String text = mProgress + "%";
        mTextPaint.getTextBounds(text, 0, text.length(), mTextBounds);
        canvas.drawText(text, centerX - mTextBounds.exactCenterX(),
                centerY - mTextBounds.exactCenterY(), mTextPaint);
    }
}
