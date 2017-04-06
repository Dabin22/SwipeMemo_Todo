package com.swipememo.swipememo.viewer.fragments.todo.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Lee on 2017-03-28.
 */

public interface TodoView {
    View init(ViewGroup container);
    void setCalendar(Calendar cal);
}
