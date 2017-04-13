package com.swipememo.swipememo.viewer.fragments.todo.view;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.swipememo.swipememo.R;
import com.swipememo.swipememo.model.types.TodoViewTag;
import com.swipememo.swipememo.viewer.fragments.todo.adapter.RegisteredAdapter;
import com.swipememo.swipememo.viewer.fragments.todo.adapter.UnRegisterAdapter;
import com.swipememo.swipememo.viewer.fragments.todo.controller.TodoControllerFragment;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by Lee on 2017-03-28.
 */

public class TodoViewImpl implements TodoView, View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private static final int BEFORE = -1;
    private static final int AFTER = 1;
    private TodoControllerFragment controller;

      /*
        -------------------------laout component-----------------------------
        ImageButton ib_before : 달력의 날짜 하루 전으로 이동
                    ib_next : 달력의 날짜 하루 후로 이동
        RecyclerView unRegister_todoList : TodoMemo가 어느 날짜에도 속하지 않은 상태의 리스트뷰
                     registered_todoList : TodoMemo가 어느 날짜에 속했을 상태의 리스트 뷰
        Spinner sp_todoType : 등록되지 않은 리스트 뷰를 Once,Repeat,Old 타입별로 분류하기 위한 Spinner
     */

    private TextView tv_todo_calendar, bg_date;
    private Button btn_todo_before, btn_todo_next;
    private RecyclerView unRegister_todoList, registered_todoList;
    private Spinner sp_todoType;
    private UnRegisterAdapter unRegisterAdapter;
    private RegisteredAdapter registeredAdapter;
    private ViewGroup container;

    private DateFormat df;


    public TodoViewImpl(TodoControllerFragment controller) {
        this.controller = controller;
    }


    @Override
    public View init(ViewGroup container) {
        View view = ((LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.fragment_todoviewer, container, false);
        this.container = container;
        TodoViewTag rgTodoTag = new TodoViewTag();
        rgTodoTag.setType(TodoViewTag.REGISTERLIST);
        TodoViewTag urgTodoTag = new TodoViewTag();
        urgTodoTag.setType(TodoViewTag.BEFORE_REGISTERLIST);
        btn_todo_before = (Button) view.findViewById(R.id.btn_todo_before);
        btn_todo_before.setOnDragListener(controller.getDragListener());
        btn_todo_next = (Button) view.findViewById(R.id.btn_todo_next);
        btn_todo_next.setOnDragListener(controller.getDragListener());
        unRegister_todoList = (RecyclerView) view.findViewById(R.id.unRegister_todoList);
        registered_todoList = (RecyclerView) view.findViewById(R.id.registered_todoList);
        unRegister_todoList.setTag(urgTodoTag);
        registered_todoList.setTag(rgTodoTag);
        unRegister_todoList.setOnDragListener(controller.getDragListener());
        registered_todoList.setOnDragListener(controller.getDragListener());
        sp_todoType = (Spinner) view.findViewById(R.id.sp_todoType);
        bg_date = (TextView) view.findViewById(R.id.bg_date);
        tv_todo_calendar = (TextView) view.findViewById(R.id.tv_todo_calendar);

        unRegisterAdapter = controller.getUnregisterAdapter();
        unRegister_todoList.setAdapter(unRegisterAdapter);
        unRegister_todoList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        registered_todoList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        btn_todo_before.setOnClickListener(this);
        btn_todo_next.setOnClickListener(this);
        tv_todo_calendar.setOnClickListener(this);

        df = new SimpleDateFormat("yyyy.MM.dd");
        return view;
    }

    private Calendar cal;

    @Override
    public void setCalendar(Calendar cal) {
        if (this.cal == null || !this.cal.equals(cal)) {
            this.cal = cal;
        }

        registeredAdapter = controller.getRegisterAdapter();
        registered_todoList.setAdapter(registeredAdapter);
        tv_todo_calendar.setText(df.format(cal.getTime()));
        bg_date.setText(tv_todo_calendar.getText().toString());
    }

    private Context getContext() {
        return container.getContext();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_todo_before:
                controller.changeOneDay(BEFORE);

                break;
            case R.id.btn_todo_next:
                controller.changeOneDay(AFTER);

                break;
            case R.id.tv_todo_calendar:
                showDatePick();
                break;
            default:
                break;
        }
    }

    //달력 다이얼로그 창을 통해서 날짜를 선택하게 합니다.
    private void showDatePick() {

        DatePickerDialog dpd = DatePickerDialog.newInstance(this,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

        if (Build.VERSION.SDK_INT >= 23) {
            dpd.setAccentColor(getContext().getResources().getColor(R.color.todoKeyColor, null));
        } else {
            dpd.setAccentColor(getContext().getResources().getColor(R.color.todoKeyColor));
        }

        dpd.setThemeDark(true);

        dpd.show(controller.getActivity().getFragmentManager(), "");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar select_cal = Calendar.getInstance();
        select_cal.set(year, monthOfYear, dayOfMonth);
        controller.changeDate(select_cal);
    }

    public void showDeleteDialog(final String type,final long no){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(getContext());
        alt_bld.setMessage("Do you want to delete Todo?").setCancelable(
                false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        controller.deleteTodo(type,no);
                    }
                }).setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        alert.setTitle("SwipeMemo");
        // Icon for AlertDialog
       // alert.setIcon(R.drawable.icon);
        alert.show();

    }
}
