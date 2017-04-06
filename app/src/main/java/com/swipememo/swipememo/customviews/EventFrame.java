package com.swipememo.swipememo.customviews;

import android.content.Context;
import android.gesture.GestureOverlayView;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by DoDo on 2017-03-01.
 */

public class EventFrame extends GestureOverlayView {

    private KeyEventListener keyEventListener = null;

    public EventFrame(Context context) {
        super(context);
    }
    public EventFrame(Context context, AttributeSet attrs) {
        super(context, attrs);
     }
    public EventFrame(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public EventFrame(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void setOnKeyEventListener(KeyEventListener listener){
        this.keyEventListener = listener;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(keyEventListener != null){
            return keyEventListener.onKeyEvent(event,EventFrame.this);
        }
        return super.dispatchKeyEvent(event);
    }

    public interface KeyEventListener {
        boolean onKeyEvent(KeyEvent event, View v);
    }
}