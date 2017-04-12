package com.swipememo.swipememo.customviews;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;

import java.util.ArrayList;


/**
 * Created by DoDo on 2017-04-09.
 */

public class FlingCardView extends CardView {

    private int duration = 300;
    ArrayList<FlingCardViewListener> listeners = new ArrayList<>();

    private boolean open = false;
    private boolean initial = true;
    private boolean clickable = true;
    private float x  = 0;
    private float xBy = -300;
    private float y = 0;
    private float yBy = 300;
    private final int LONG_PRESS = 0x00300;
    private boolean mInLongPress = false;


    public FlingCardView(Context context) {
        this(context, null);
    }
    public FlingCardView(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }
    public FlingCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(initial){
            x = getX();
            y = getY();
            initial = false;
        }
    }
    public void setxBy(float xBy){
        this.xBy = xBy;
    }
    public void setyBy(float yBy){
        this.yBy = yBy;
    }
    public boolean isOpen(){return open;}
    public void open(){
        for(FlingCardViewListener listener : listeners) {
            listener.onOpen(FlingCardView.this);
        }
        x = getX();
        animate().x(x+xBy).setDuration(duration).start();
        open= true;
        requestLayout();
    }

    public void close(){
        for(FlingCardViewListener listener : listeners) {
            listener.onClose(FlingCardView.this);
        }
        animate().x(x).setDuration(duration).start();
        open = false;
        requestLayout();
    }
    public void addFlingCardViewListener(FlingCardViewListener flingCardViewListener){
        listeners.add(flingCardViewListener);
    }

    public void setClickable(boolean clickable){
        this.clickable = clickable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        verifyFling(event);
        if(event.getAction() == MotionEvent.ACTION_DOWN)
            return true;
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        verifyFling(ev);
        return super.onInterceptTouchEvent(ev);
    }

    private MotionEvent mCurrentDownEvent;
    private VelocityTracker mVelocityTracker = null;
    private int mMaximumFlingVelocity = 1000;
    private int mMinimumFlingVelocity = 50;

    private void verifyFling(MotionEvent event){
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        MotionEvent mCurrentDownEvent = null;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (mCurrentDownEvent != null) {
                    mCurrentDownEvent.recycle();
                }
                mCurrentDownEvent = MotionEvent.obtain(event);
                break;
            case MotionEvent.ACTION_MOVE:
                final VelocityTracker velocityTracker = mVelocityTracker;
                final int pointerId = event.getPointerId(0);
                velocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
                final float velocityY = velocityTracker.getYVelocity(pointerId);
                final float velocityX = velocityTracker.getXVelocity(pointerId);
                if ((Math.abs(velocityY) > mMinimumFlingVelocity)
                        || (Math.abs(velocityX) > mMinimumFlingVelocity)){
                    onFling(mCurrentDownEvent, event, velocityX, velocityY);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
    }

    public void onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){

        for(FlingCardViewListener listener : listeners) {
            listener.onFling(FlingCardView.this, velocityX, velocityY);
        }
        if(velocityX>50 && velocityY < 10){
            if(!open)
                return;
            close();
        }else{
            if(open)
                return;
            open();
        }
        for(FlingCardViewListener listener : listeners) {
            listener.afterFling(FlingCardView.this, velocityX, velocityY);
        }
    }

    public interface FlingCardViewListener {
        void onFling(FlingCardView view, float velocityX, float velocityY);
        void afterFling(FlingCardView view, float velocityX, float velocityY);
        void onOpen(FlingCardView view);
        void onClose(FlingCardView view);
    }
}
