package com.swipememo.swipememo.viewer.fragments.todo.controller;

import com.swipememo.swipememo.model.types.SelectedTodo;
import com.swipememo.swipememo.model.types.Todo;
import com.swipememo.swipememo.viewer.fragments.todo.adapter.RegisteredAdapter;
import com.swipememo.swipememo.viewer.fragments.todo.adapter.UnRegisterAdapter;
import com.swipememo.swipememo.viewer.fragments.todo.listener.TodoDragListener;

import java.util.Calendar;

/**
 * Created by Dabin on 2016-11-27.
 */

public interface TodoController {
    void register_todo(long no);

    void stoppingMoveDate();

    void changeBelongDate(long no);

    void register_cancle(long no);

    UnRegisterAdapter getUnregisterAdapter();

    RegisteredAdapter getRegisterAdapter();

    void changeDate(Calendar cal);

    void changeOneDay(int day);

    void draggingMoveDate(int id);

    void deleteTodo(String type,long no);

    void setDone(SelectedTodo todo, boolean done);

    TodoDragListener getDragListener();
}
