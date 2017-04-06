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
import com.swipememo.swipememo.model.types.Todo;
import com.swipememo.swipememo.model.types.TodoViewType;
import com.swipememo.swipememo.viewer.fragments.todo.listener.TodoDragListener;
import com.swipememo.swipememo.viewer.fragments.todo.listener.TodoLongClickListener;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Dabin on 2016-11-27.
 */

public class UnRegisterAdapter extends RealmRecyclerViewAdapter<Todo, UnRegisterAdapter.ViewHolder> {

    private static final String ONCE = "ONCE_TODO";
    private static final String REPEAT = "REPEAT_TODO";
    private static final String OLD = "OLD_TODO";
    private Context context;
    private TodoDragListener dragListener;
    private TodoLongClickListener longClickListener;
    private OrderedRealmCollection<Todo> datas = null;

    public UnRegisterAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Todo> data, boolean autoUpdate, TodoDragListener dragListener) {
        super(context, data, autoUpdate);
        this.context = context;
        //TODO Adapter에 NotifyItemChanged가 필요함 (또는 NotifyItemRemoved), 소스코드 http://stackoverflow.com/questions/28995380/best-practices-to-use-realm-with-a-recycler-view
        datas = data;
        this.dragListener = dragListener;
        longClickListener = new TodoLongClickListener();
    }

    @Override
    public UnRegisterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_todo_before_register,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UnRegisterAdapter.ViewHolder holder, int position) {
        Todo todo = datas.get(position);
        Log.e("tag",todo.getContent()+"");
        holder.tv_before_todoItem.setText(todo.getContent());

        TodoViewType viewType = new TodoViewType();
        viewType.setType(TodoViewType.TODO);
        viewType.setNo(todo.getNo());
        holder.itemView.setTag(viewType);

    }



    @Override
    public int getItemCount() {
        return getData().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_before_todoItem;
        Button btn_befroe_flingable;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_before_todoItem = (TextView) itemView.findViewById(R.id.todoItem_before_text);
            btn_befroe_flingable = (Button) itemView.findViewById(R.id.todoItem_before_flingable);
            itemView.setOnDragListener(dragListener);
            itemView.setOnLongClickListener(longClickListener);
        }
    }
}
