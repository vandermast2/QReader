package com.samapps.qreader.color;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.samapps.qreader.R;

/**
 * Created by breez_000 on 8/20/2017.
 */

public class SeekBarColor extends AppCompatSeekBar {
    private Paint textPaint;
    private Rect textRect;

    @ColorInt
    private int textColor;

    @Dimension(unit = 2)
    private float textSize;

    private String text;
//    public SeekBarColor(Context context) {
//        super(context);
//        init(null);
//    }

    public SeekBarColor(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SeekBarColor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        textPaint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        textRect = new Rect();

        if (attrs != null) {

            final TypedArray typedArray = getContext().obtainStyledAttributes(
                    attrs, R.styleable.SeekBarColor
            );

            try {

                textColor = typedArray.getColor(
                        R.styleable.SeekBarColor_android_textColor,
                        0xff000000
                );

                textSize = typedArray.getDimension(
                        R.styleable.SeekBarColor_android_textSize,
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                                18, getResources().getDisplayMetrics())
                );

                text = typedArray.getString(R.styleable
                        .SeekBarColor_android_text);

            } finally {

                typedArray.recycle();

            }

        }

        textPaint.setColor(textColor);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.getTextBounds("255", 0, 3, textRect);

        setPadding(getPaddingLeft(), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                (float) (0.6 * textRect.height()), getResources().getDisplayMetrics()),
                getPaddingRight(), getPaddingBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawText(
                (text == null) ? String.valueOf(getProgress()) : text,
                getThumb().getBounds().left + getPaddingLeft(),
                textRect.height() + (getPaddingTop() >> 2),
                textPaint
        );

    }
}
