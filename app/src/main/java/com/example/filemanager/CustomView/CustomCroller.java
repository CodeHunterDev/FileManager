package com.example.filemanager.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.sdsmdg.harjot.crollerTest.Croller;

public class CustomCroller extends Croller {

    public CustomCroller(Context context) {
        super(context);
    }

    public CustomCroller(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCroller(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /// just set to true or false
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return true;
    }
}
