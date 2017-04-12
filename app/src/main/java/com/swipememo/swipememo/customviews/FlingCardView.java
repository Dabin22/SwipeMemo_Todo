package com.swipememo.swipememo.customviews;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;


/**
 * Created by DoDo on 2017-04-09.
 */

public class FlingCardView extends CardView {

    private final int ANIM_DURATION = 300;
    private final int FLINGABLE = 0x000001;

    ArrayList<FlingCardViewListener> listeners = new ArrayList<>();
    MyHandler myHandler = new MyHandler();
    private boolean open = false;
    private boolean initial = true;
    private boolean clickable = true;
    private boolean unflingable = false;
    private float xBy = -300, yBy = -300;
    private float x=0 ,y = 0;
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
        if(getX()< 0)
            return;
        x = getX();
        y = getY();
        animate().xBy(xBy).setDuration(ANIM_DURATION).start();
        myHandler.sendEmptyMessageAtTime(FLINGABLE, Calendar.getInstance().getTimeInMillis()+ANIM_DURATION);
        open= true;
    }

    public void close(){
        for(FlingCardViewListener listener : listeners) {
            listener.onClose(FlingCardView.this);
        }
        if(getX()>0)
            return;
        animate().x(x).setDuration(ANIM_DURATION).start();
        open = false;
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
        if(Math.abs(velocityY) > 5)
            return;
        for(FlingCardViewListener listener : listeners) {
            listener.onFling(FlingCardView.this, velocityX, velocityY);
        }
        if(velocityX>50){
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

    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == FLINGABLE){
                unflingable = false;
            }
        }
    }

}
