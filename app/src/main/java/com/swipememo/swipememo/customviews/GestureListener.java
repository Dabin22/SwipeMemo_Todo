package com.swipememo.swipememo.customviews;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by DoDo on 2017-03-01.
 */

public class GestureListener{
    public boolean onDown(MotionEvent e, View v){return false;}
    public void onShowPress(MotionEvent e, View v){}
    public boolean onSingleTapUp(MotionEvent e, View v){return false;}
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY, View v){return false;}
    public boolean onScroll(float distanceX, float distanceY, View v){return false;}
    public void onLongPress(MotionEvent e, View v){}
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY, View v){return false;}
    public boolean onFling(float velocityX, float velocityY, View v){return false;}
    public boolean onDoubleTap(MotionEvent e, View v) {return false;}
    public boolean onDoubleTapEvent(MotionEvent e, View v) {return false;}
    public boolean onSingleTapConfirmed(MotionEvent e, View v) {return  false;}
    public boolean onContextClick(MotionEvent e, View v) {return  false;}
}
