package com.swipememo.swipememo.viewer.fragments.todo.controller;

import com.swipememo.swipememo.viewer.fragments.todo.adapter.RegisteredAdapter;
import com.swipememo.swipememo.viewer.fragments.todo.adapter.UnRegisterAdapter;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Dabin on 2016-11-27.
 */

public interface TodoController {
    void register_todo(int no);

    void stoppingMoveDate();

    void changeBelongDate(int no);

    void register_cancle(int pickedIndex);

    UnRegisterAdapter getUnregisterAdapter();

    RegisteredAdapter getRegisterAdapter();

    void changeDate(Calendar cal);

    void changeOneDay(int day);

    void draggingMoveDate(int id);

    void deleteTodo(String type,int no);
}
