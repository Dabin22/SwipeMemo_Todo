package com.swipememo.swipememo.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by DoDo on 2017-03-01.
 */

public class SlideOutTextView extends LinearLayout {

    private OnCheckedListener checkListener= null;

    public SlideOutTextView(Context context) {
        super(context);
    }

    public SlideOutTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideOutTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SlideOutTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getCheckedBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lineText(isChecked);
                if(checkListener != null)
                    checkListener.onChecked(SlideOutTextView.this);
            }
        });
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.e("dispatch","dispatchTouchEvent");
        gd.onTouchEvent(event);
        return true;
    }

    public void setChecked(boolean checked){
        getCheckedBox().setChecked(checked);
    }
    public void setOnCheckedListener(OnCheckedListener listener){
        this.checkListener = listener;
    }

    private void lineText(boolean isStriked){
        if(isStriked){
            getTextView().setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            getTextView().setPaintFlags(Paint.ANTI_ALIAS_FLAG);
        }
    }

    private TextView getTextView(){
        return (TextView) getChildAt(1);
    }
    private CompoundButton getCheckedBox(){
        return (CompoundButton) getChildAt(0);
    }

    GestureDetector gd = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener()
    {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.e("tag","velocityX = " + velocityX+",y = " + velocityY);
            if(velocityX<0){
                 Log.e("tag","check");
                lineText(true);
                getCheckedBox().setChecked(true);
            }
            if(velocityX>0){
                lineText(false);
                getCheckedBox().setChecked(false);
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }
    });

    public interface OnCheckedListener{
        void onChecked(SlideOutTextView view);
    }
}
