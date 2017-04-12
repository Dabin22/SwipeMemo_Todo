package com.swipememo.swipememo.viewer.fragments.todo.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.swipememo.swipememo.R;
import com.swipememo.swipememo.customviews.ItemContainer;
import com.swipememo.swipememo.model.types.SelectedTodo;
import com.swipememo.swipememo.model.types.TodoViewTag;
import com.swipememo.swipememo.viewer.fragments.todo.controller.TodoController;
import com.swipememo.swipememo.viewer.fragments.todo.listener.TodoDragListener;
import com.swipememo.swipememo.viewer.fragments.todo.listener.TodoLongClickListener;


import java.util.HashMap;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.Sort;

/*
*
 * Created by Dabin on 2016-11-27.
 */

public class RegisteredAdapter extends RealmRecyclerViewAdapter<SelectedTodo, RegisteredAdapter.ViewHolder> implements CompoundButton.OnCheckedChangeListener {


    private TodoDragListener dragListener;
    private TodoLongClickListener longClickListener;
    private Context context;
    private HashMap<Long,ItemContainer> viewList;
    private HashMap<Long,TextView> tvList;

    private TodoController controller;
    private OrderedRealmCollection<SelectedTodo> datas = null;

    public RegisteredAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<SelectedTodo> data, boolean autoUpdate, TodoDragListener dragListener, TodoController controller) {
        super(context, data, autoUpdate);
        this.context = context;
        this.dragListener = dragListener;
        longClickListener = new TodoLongClickListener();
        this.controller = controller;
        datas = data.sort("no", Sort.DESCENDING);
        viewList = new HashMap<>();
        tvList = new HashMap<>();
    }



    @Override
    public RegisteredAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemContainer view = (ItemContainer)LayoutInflater.from(context).inflate(R.layout.item_todo_registered, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RegisteredAdapter.ViewHolder holder, int position) {
        final SelectedTodo todo = datas.get(position);
        viewList.put(todo.getNo(),(ItemContainer)holder.itemView);
        tvList.put(todo.getNo(),holder.tv_unput_todo);
        holder.tv_unput_todo.setText(todo.getContent());
        holder.ck_todo_done.setTag(todo);
        if(todo.isDone()){
            holder.ck_todo_done.setChecked(true);
            setDragListener(false,todo.getNo());
            lineText(todo.isDone(),holder.tv_unput_todo);
        }
        holder.ck_todo_done.setOnCheckedChangeListener(null);
        holder.ck_todo_done.setOnCheckedChangeListener(this);


        TodoViewTag viewType = new TodoViewTag();

        viewType.setType(TodoViewTag.SELECTED_TODO);
        viewType.setNo(todo.getNo());
        holder.itemView.setTag(viewType);
    }

    @Override
    public int getItemCount() {
        return getData().size();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
       controller.setDone((SelectedTodo)buttonView.getTag(),isChecked);
        long no = ((SelectedTodo) buttonView.getTag()).getNo();
        setDragListener(!isChecked,no);
        lineText(isChecked,tvList.get(no));
    }

    public void setDragListener(boolean done,long no){
        viewList.get(no).setClickable(done);
    }

    private void lineText(boolean isStriked,TextView textView){
        if(isStriked){
            textView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            textView.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_unput_todo;
        CheckBox ck_todo_done;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_unput_todo = (TextView) itemView.findViewById(R.id.todoItem_registered_text);
            ck_todo_done = (CheckBox) itemView.findViewById(R.id.todoItem_registered_checkbox);
            itemView.setOnDragListener(dragListener);
        }
    }


}
