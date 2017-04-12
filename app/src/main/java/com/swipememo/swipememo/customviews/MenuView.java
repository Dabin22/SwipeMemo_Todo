package com.swipememo.swipememo.customviews;

import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.widget.Button;

import java.util.Calendar;

/**
 * Created by DoDo on 2017-04-09.
 */

public class MenuView extends android.support.v7.widget.AppCompatButton {

    private final int ANIM_DURATION = 300;
    private VisibilityHandler mVHandler = new VisibilityHandler();

    public MenuView(Context context) {
        this(context, null);
    }

    public MenuView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setVisibility(int visibility) {
        long now = Calendar.getInstance().getTimeInMillis();
        switch (visibility) {
            case VISIBLE:
                Log.e("MenuView", "VISIBLE ANIM START");
                if (getVisibility() == GONE)
                    super.setVisibility(INVISIBLE);
                setScaleX(0.0f);
                setScaleY(0.0f);
                super.setVisibility(visibility);
                animate().scaleX(1.0f).setDuration(ANIM_DURATION).start();
                animate().scaleY(1.0f).setDuration(ANIM_DURATION).start();
                break;

            case INVISIBLE:
                Log.e("MenuView", "INVISIBLE ANIM START");
                animate().scaleX(0.0f).setDuration(ANIM_DURATION).start();
                animate().scaleY(0.0f).setDuration(ANIM_DURATION).start();
                mVHandler.sendEmptyMessageAtTime(INVISIBLE, now + ANIM_DURATION);
                break;
            case GONE:
                Log.e("MenuView", "GONE ANIM START");
                animate().scaleX(0.0f).setDuration(ANIM_DURATION).start();
                animate().scaleY(0.0f).setDuration(ANIM_DURATION).start();
                mVHandler.sendEmptyMessageAtTime(GONE,now + ANIM_DURATION);
                break;
        }
    }
    class VisibilityHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("MenuView", "MESSAGE RECEIVED: " + msg.what);
            MenuView.super.setVisibility(msg.what);
        }
    }
}
