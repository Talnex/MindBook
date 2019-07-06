package com.talnex.wrongsbook.Components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.talnex.wrongsbook.R;

public class myTextView extends android.support.v7.widget.AppCompatEditText {
    private static final int STROKE_WIDTH = 2;
    private int borderCol;

    private Paint borderPaint;

    public myTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.myTextView, 0, 0);
        try {
            borderCol = a.getInteger(R.styleable.myTextView_borderColor, 0);//0 is default
        } finally {
            a.recycle();
        }

        borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(STROKE_WIDTH);
        borderPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (0 == this.getText().toString().length())
            return;

        borderPaint.setColor(borderCol);
        borderPaint.setStrokeWidth(10);

        int w = this.getMeasuredWidth();
        int h = this.getMeasuredHeight();

        RectF r = new RectF(2, 2, w - 2, h - 2);
        canvas.drawRoundRect(r, 10, 10, borderPaint);
        super.onDraw(canvas);
    }

    public int getBordderColor() {
        return borderCol;
    }

    public void setBorderColor(int newColor) {
        borderCol = newColor;
        //重绘颜色内容
        invalidate();
        //请求父布局重新测量重绘
        requestLayout();
    }



}