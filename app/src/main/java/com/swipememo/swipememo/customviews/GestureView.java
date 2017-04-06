package com.swipememo.swipememo.customviews;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by DoDo on 2017-03-01.
 */
public class GestureView extends View {
    GestureDetectorCompat dt;
    PostGestureListener postGL=null;
    public GestureView(Context context) {
        super(context);
    }
    public void setGestureListener(final GestureListener listener){
        dt = new GestureDetectorCompat(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {return listener.onDown(e, GestureView.this);}
            @Override
            public void onShowPress(MotionEvent e) {listener.onShowPress(e,GestureView.this);}
            @Override
            public boolean onSingleTapUp(MotionEvent e) {return listener.onSingleTapUp(e, GestureView.this);}
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {return listener.onScroll(e1,e2,distanceX,distanceY,GestureView.this);}
            @Override
            public void onLongPress(MotionEvent e) {listener.onLongPress(e,GestureView.this);}
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {return listener.onFling(e1,e2,velocityX,velocityY,GestureView.this);}
            @Override
            public boolean onDoubleTap(MotionEvent e) {return listener.onDoubleTap(e,GestureView.this);}
            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {return listener.onDoubleTapEvent(e, GestureView.this);}
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {return listener.onSingleTapConfirmed(e, GestureView.this);}
            @Override
            public boolean onContextClick(MotionEvent e) {return listener.onContextClick(e, GestureView.this);}
        });
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(dt != null)
            dt.onTouchEvent(event);
        if(postGL != null)
            postGL.onPostGesture(GestureView.this);
        return true;
    }
}
