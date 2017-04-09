package com.swipememo.swipememo.viewer.fragments.todo.listener;

import android.util.Log;
import android.view.DragEvent;
import android.view.View;

import com.swipememo.swipememo.R;
import com.swipememo.swipememo.model.types.TodoViewType;
import com.swipememo.swipememo.viewer.fragments.todo.controller.TodoController;

import java.util.ConcurrentModificationException;

/**
 * Created by Dabin on 2016-11-27.
 */

public class TodoDragListener implements View.OnDragListener {

    private TodoController controller;
    private long pickedIndex = -1;
    private String pickedType = "";
    private String targetType = "";
    private View view_dragging = null;

    public TodoDragListener(TodoController controller) {
        this.controller = controller;
    }


    @Override
    public boolean onDrag(View view, DragEvent dragEvent) throws ConcurrentModificationException {

        view_dragging = (View) dragEvent.getLocalState();
        pickedType = ((TodoViewType) view_dragging.getTag()).getType();
        pickedIndex = ((TodoViewType) view_dragging.getTag()).getNo();
        switch (dragEvent.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:

                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                if (view.getId() == R.id.btn_todo_before || view.getId() == R.id.btn_todo_next) {
                    controller.draggingMoveDate(view.getId());
                } else {
                    if (view.getTag() != null) {
                        targetType = ((TodoViewType) view.getTag()).getType();
                    }

                }
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                if (view.getId() == R.id.ib_before || view.getId() == R.id.ib_next) {
                    controller.stoppingMoveDate();
                }
                break;
            case DragEvent.ACTION_DROP:
                controller.stoppingMoveDate();
                if (pickedType.equals("BeforeTodo")) {

                    if (targetType.equals("Today") || targetType.equals("Today_list")) {
                        Log.e("tag", "type is = " + pickedType + ", indext = " + pickedIndex);
                        controller.register_todo(pickedIndex);
                    }

                } else if (pickedType.equals("Today")) {
                    if (targetType.equals("Today") || targetType.equals("Today_list")) {
                        controller.changeBelongDate(pickedIndex);
                    } else if (targetType.equals("Bottom")) {
                        controller.register_cancle(pickedIndex);
                    }
                }
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                controller.stoppingMoveDate();
                clear_tag();
                view_dragging.post(new Runnable() {
                    @Override
                    public void run() {
                        view_dragging.setVisibility(View.VISIBLE);
                    }
                });

                break;
            default:
                break;

        }

        return true;
    }

    private void clear_tag() {
        targetType = "";
        pickedIndex = -1;
        pickedType = "";
    }
}
