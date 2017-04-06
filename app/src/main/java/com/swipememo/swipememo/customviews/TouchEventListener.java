package com.swipememo.swipememo.customviews;

import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by DoDo on 2017-03-01.
 */

public interface TouchEventListener {
    boolean onTouch(MotionEvent e, @Nullable GestureListener listener, View view);
}
