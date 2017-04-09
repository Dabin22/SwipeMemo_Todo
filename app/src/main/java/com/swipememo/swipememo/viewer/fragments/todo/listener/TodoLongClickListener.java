package com.swipememo.swipememo.viewer.fragments.todo.listener;

import android.util.Log;
import android.view.View;

/**
 * Created by Dabin on 2016-11-27.
 */

public class TodoLongClickListener implements View.OnLongClickListener {
    private boolean clickable = true;
    @Override
    public boolean onLongClick(View view) {
        Log.e("tag","long click");
        if(clickable) {
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                    view);
//            if(Build.VERSION.SDK_INT >23){
//                view.startDragAndDrop(null,shadowBuilder,view,0);
//            } else{
                view.startDrag(null, shadowBuilder, view, 0);
//            }
            view.setVisibility(View.INVISIBLE);
            return true;
        }else{
            return false;
        }

    }

    public void change_clikable(boolean clickable){
        this.clickable = clickable;
    }
}
