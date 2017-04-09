package com.swipememo.swipememo.viewer.fragments.todo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.swipememo.swipememo.R;
import com.swipememo.swipememo.customviews.SlideWithMenu;
import com.swipememo.swipememo.model.types.SelectedTodo;
import com.swipememo.swipememo.model.types.TodoViewType;
import com.swipememo.swipememo.viewer.fragments.todo.controller.TodoController;
import com.swipememo.swipememo.viewer.fragments.todo.listener.TodoDragListener;
import com.swipememo.swipememo.viewer.fragments.todo.listener.TodoLongClickListener;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.Sort;

/*
*
 * Created by Dabin on 2016-11-27.
 */

public class RegisteredAdapter extends RealmRecyclerViewAdapter<SelectedTodo, RegisteredAdapter.ViewHolder> {


    private TodoDragListener dragListener;
    private TodoLongClickListener longClickListener;
    private Context context;

    private TodoController controller;
    private OrderedRealmCollection<SelectedTodo> datas = null;

    public RegisteredAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<SelectedTodo> data, boolean autoUpdate, TodoDragListener dragListener, TodoController controller) {
        super(context, data, autoUpdate);
        this.context = context;
        this.dragListener = dragListener;
        longClickListener = new TodoLongClickListener();
        this.controller = controller;
        datas = data.sort("no", Sort.DESCENDING);
    }



    @Override
    public RegisteredAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SlideWithMenu view = (SlideWithMenu)LayoutInflater.from(context).inflate(R.layout.item_todo_registered, parent, false);
        return new ViewHolder(view);
    }

    private View itemView = null;
    @Override
    public void onBindViewHolder(RegisteredAdapter.ViewHolder holder, int position) {
        final SelectedTodo todo = datas.get(position);
        Log.e("tag","position = "+position+" data no = " + datas.get(position)+" contents = " + todo.getContent());
        holder.tv_unput_todo.setText(todo.getContent());



        TodoViewType viewType = new TodoViewType();
        this.itemView = holder.itemView;
        viewType.setType(TodoViewType.SELECTED_TODO);
        viewType.setNo(todo.getNo());
        holder.itemView.setTag(viewType);
    }

    @Override
    public int getItemCount() {
        return getData().size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_unput_todo;
        CheckBox ck_todo_done;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_unput_todo = (TextView) itemView.findViewById(R.id.todoItem_registered_text);
            ck_todo_done = (CheckBox) itemView.findViewById(R.id.todoItem_registered_checkbox);
            itemView.setOnLongClickListener(longClickListener);
            itemView.setOnDragListener(dragListener);
        }
    }


}
