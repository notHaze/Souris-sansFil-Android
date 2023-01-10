package com.remotedesktopcontrol.application;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

public class myLinearLayout extends LinearLayout {

    public myLinearLayout(@NonNull Context c) {
        super(c);
    }

    public myLinearLayout(@NonNull Context c, AttributeSet attrs) {
        super(c,attrs);
    }

    public myLinearLayout(@NonNull Context c, AttributeSet attrs, int defStyleAttr) {
        super(c, attrs, defStyleAttr);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
