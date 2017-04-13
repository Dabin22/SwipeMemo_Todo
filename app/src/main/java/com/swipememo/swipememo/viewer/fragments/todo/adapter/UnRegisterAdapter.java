package com.swipememo.swipememo.viewer.fragments.todo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.swipememo.swipememo.R;
import com.swipememo.swipememo.customviews.ItemContainer;
import com.swipememo.swipememo.customviews.MenuView;
import com.swipememo.swipememo.model.types.Todo;
import com.swipememo.swipememo.model.types.TodoViewTag;
import com.swipememo.swipememo.viewer.fragments.todo.controller.TodoController;
import com.swipememo.swipememo.viewer.fragments.todo.listener.TodoDragListener;
import com.swipememo.swipememo.viewer.fragments.todo.listener.TodoLongClickListener;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Dabin on 2016-11-27.
 */

public class UnRegisterAdapter extends RealmRecyclerViewAdapter<Todo, UnRegisterAdapter.ViewHolder> implements View.OnClickListener {


    private Context context;
    private TodoDragListener dragListener;
    private OrderedRealmCollection<Todo> datas = null;
    private TodoController controller;

    public UnRegisterAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Todo> data, boolean autoUpdate, TodoDragListener dragListener, TodoController controller) {
        super(context, data, autoUpdate);
        this.context = context;
        datas = data;
        this.dragListener = dragListener;
        this.controller = controller;
    }

    @Override
    public UnRegisterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemContainer view = (ItemContainer)LayoutInflater.from(context).inflate(R.layout.item_todo_before_register,parent,false);
        view.reset();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UnRegisterAdapter.ViewHolder holder, int position) {
        Todo todo = datas.get(position);
        Log.e("tag",todo.getContent()+"");
        holder.tv_before_todoItem.setText(todo.getContent());

        holder.btn_befroe_flingable.setTag(todo.getNo());
        holder.btn_befroe_flingable.setOnClickListener(this);

        holder.before_register_delete.setTag(todo.getNo());
        holder.before_register_delete.setOnClickListener(this);

        TodoViewTag viewType = new TodoViewTag();
        viewType.setType(TodoViewTag.TODO);
        viewType.setNo(todo.getNo());
        holder.itemView.setTag(viewType);

    }



    @Override
    public int getItemCount() {
        return getData().size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.todoItem_before_flingable:
                controller.register_todo((long)v.getTag());
                break;
            case R.id.todoItem_before_registered_delete:
                controller.checkDelete(TodoViewTag.TODO,(long)v.getTag());
                break;
            default:
                break;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_before_todoItem;
        Button btn_befroe_flingable;
        MenuView before_register_delete;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_before_todoItem = (TextView) itemView.findViewById(R.id.todoItem_before_text);
            btn_befroe_flingable = (Button) itemView.findViewById(R.id.todoItem_before_flingable);
            before_register_delete = (MenuView)itemView.findViewById(R.id.todoItem_before_registered_delete);
            itemView.setOnDragListener(dragListener);

        }

    }
}
