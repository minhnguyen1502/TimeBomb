package com.example.timebomb.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class StrokeTextView extends AppCompatTextView {

    public StrokeTextView(Context context) {
        super(context);
    }

    public StrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StrokeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Save the original color
        int originalColor = getCurrentTextColor();

        // Set stroke color and width
        setTextColor(Color.BLACK);
        getPaint().setStyle(Paint.Style.STROKE);
        getPaint().setStrokeWidth(6); // 2dp stroke width
        super.onDraw(canvas);

        // Restore the original color and draw the text
        setTextColor(originalColor);
        getPaint().setStyle(Paint.Style.FILL);
        super.onDraw(canvas);
    }
}
