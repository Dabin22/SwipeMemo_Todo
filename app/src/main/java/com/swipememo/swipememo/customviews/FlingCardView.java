package com.swipememo.swipememo.customviews;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Created by DoDo on 2017-04-09.
 */

public class FlingCardView extends CardView {

    private int duration = 300;
    ArrayList<FlingCardViewListener> listeners = new ArrayList<>();
    FlingCardViewListener flingCardViewListener = null;
    GestureDetector gd = null;

    private boolean open = false;
    private boolean initial = true;
    private float x  = 0;
    private float xBy = 0;
    private float y = 0;
    private float yBy = 0;
    public FlingCardView(Context context) {
        this(context, null);
    }
    public FlingCardView(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }
    public FlingCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        gd = new GestureDetector(getContext(),new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                for(FlingCardViewListener listener : listeners) {
                    listener.onFling(FlingCardView.this, velocityX, velocityY);
                }
                if(velocityX>0){
                    if(!open)
                        return true;
                    close();
                }else{
                    if(open)
                        return true;
                    open();
                }
                for(FlingCardViewListener listener : listeners) {
                    listener.afterFling(FlingCardView.this, velocityX, velocityY);
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(initial){
            x = getX();
            y = getY();
            initial = false;
            xBy = getWidth()/2;
            yBy = getHeight()/2;
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
        animate().xBy(xBy).setDuration(duration).start();
    }

    public void close(){
        for(FlingCardViewListener listener : listeners) {
            listener.onClose(FlingCardView.this);
        }
        animate().x(x).setDuration(duration).start();
    }
    public void addFlingCardViewListener(FlingCardViewListener flingCardViewListener){
        listeners.add(flingCardViewListener);
    }
    public interface FlingCardViewListener {
        void onFling(FlingCardView view, float velocityX, float velocityY);
        void afterFling(FlingCardView view, float velocityX, float velocityY);
        void onOpen(FlingCardView view);
        void onClose(FlingCardView view);
    }
}
