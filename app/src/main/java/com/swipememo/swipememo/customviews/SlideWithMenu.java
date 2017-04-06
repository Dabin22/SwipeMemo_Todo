package com.swipememo.swipememo.customviews;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by DoDo on 2017-03-20.
 */

public class SlideWithMenu extends FrameLayout {
    public SlideWithMenu(Context context) {
        this(context,null);
    }

    public SlideWithMenu(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SlideWithMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SlideWithMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
    private float dx = 0.0f;
    private final int BASIC_ANIMATION_DURATION = 300;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch(ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                dx = getSlideableView().getX() - ev.getRawX();
                return true;
            case MotionEvent.ACTION_MOVE:
                getSlideableView().animate().x(ev.getRawX()+dx).setDuration(0).start();
                getMenu().animate().x(getSlideableView().getX()+getSlideableView().getWidth()).setDuration(0).start();
                return true;
            case MotionEvent.ACTION_UP:
                if(getSlideableView().getTranslationX()<-1*getMenu().getWidth()){
                    getSlideableView().animate().translationX(-1*getSlideableView().getWidth()).setDuration(BASIC_ANIMATION_DURATION).start();
                    getMenu().animate().alpha(0).x(getSlideableView().getX()+getSlideableView().getWidth()).setDuration(0).start();
                    getMenu().animate().alpha(1.0f).setDuration(200).start();

                }else{
                    getSlideableView().animate().translationX(0.0f).setDuration(BASIC_ANIMATION_DURATION).start();
                    getMenu().animate().translationX(0.0f).setDuration(0).start();
                }
                return true;
            case MotionEvent.ACTION_CANCEL:
                return true;
        }
        return super.dispatchTouchEvent(ev);
    }


    public void setOnClickMenuButton(View.OnClickListener listener){
        getMenu().setOnClickListener(listener);
    }
    private View getSlideableView(){
        return getChildAt(1);
    }
    private View getMenu(){
        return getChildAt(0);
    }
}
