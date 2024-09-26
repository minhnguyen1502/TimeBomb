package com.example.timebomb.base;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class CustomTextView extends AppCompatTextView {
    public CustomTextView(Context context) {
        super(context);    }
    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);    }
    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);    }
    public void setColors(int textColor, int accentColor, int backgroundColor) {
        setTextColor(textColor);        setLinkTextColor(accentColor);
        setBackgroundColor(backgroundColor);    }
}
