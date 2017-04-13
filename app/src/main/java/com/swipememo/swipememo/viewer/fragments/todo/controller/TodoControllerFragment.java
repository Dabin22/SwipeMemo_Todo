package com.swipememo.swipememo.viewer.fragments.todo.controller;

import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.swipememo.swipememo.R;
import com.swipememo.swipememo.model.database.DBHelper;
import com.swipememo.swipememo.model.database.RealmHelper;
import com.swipememo.swipememo.model.types.SelectedTodo;
import com.swipememo.swipememo.model.types.Todo;
import com.swipememo.swipememo.model.types.TodoViewTag;
import com.swipememo.swipememo.viewer.fragments.todo.adapter.RegisteredAdapter;
import com.swipememo.swipememo.viewer.fragments.todo.adapter.UnRegisterAdapter;
import com.swipememo.swipememo.viewer.fragments.todo.listener.TodoDragListener;
import com.swipememo.swipememo.viewer.fragments.todo.view.TodoViewImpl;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.Sort;


public class TodoControllerFragment extends Fragment implements TodoController {

    private DBHelper dbHelper;
    private TodoViewImpl todoView;
    private boolean alive = true;

    public TodoControllerFragment() {
        // Required empty public constructor
    }


    private static final int POPUPMAX = 1;
    private int popup_count;

    public static TodoControllerFragment newInstance() {
        TodoControllerFragment fragment = new TodoControllerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /*
        today : 오늘 날짜
        selected_day : 선택된 날짜
        cal : calendar 변수
     */
    private Date today;
    private Calendar cal;
    private int day = 0;

    private TodoDragListener dragListener;
    private HashMap<Date, OrderedRealmCollection<SelectedTodo>> map_seletedTodo_datas;
    private UnRegisterAdapter once_type_adapter, repeat_type_adapter, old_type_adapter;
    private HashMap<Calendar, RegisteredAdapter> registeredAdapters;
    private static final String ONCE = com.swipememo.swipememo.model.types.Todo.ONCE;
    private static final String REPEAT = com.swipememo.swipememo.model.types.Todo.REPEAT;
    private static final String OLD = com.swipememo.swipememo.model.types.Todo.OLD;

    private HashMap<Date, RegisteredAdapter> map_register_adapters;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Realm.init(getContext());
        dbHelper = RealmHelper.getInstance((Application) getActivity().getApplicationContext());
        dbHelper.dbInit(this);

        dragListener = new TodoDragListener(this);
        todoView = new TodoViewImpl(this);


        Log.e("tag", "onCreateView");
        View view = todoView.init(container);
        init();
        return view;
    }

    //초기화하는 함수입니다.
    private void init() {
        registeredAdapters = new HashMap<>();
        init_dateData();
        if (dbHelper.readAllTodo().size() == 0 && dbHelper.readSelectedTodoByBelongDate(cal.getTime()).size() == 0) {
            for (int i = 0; i < 3; i++) {
                dbHelper.writeSelectedTodo(Todo.REPEAT, "나의 " + i, cal.getTime(), cal.getTime());
                dbHelper.createTodo(Todo.REPEAT, i + "번째", cal.getTime());
            }

        }

        Log.e("time", cal.getTime().toString());

    }

    //오늘 날짜를 저장하고 날짜 표시하는 함수 setDate()를 호출합니다.
    private void init_dateData() {
        today = modifi_customDate(Calendar.getInstance(Locale.KOREA).getTime());
        cal = Calendar.getInstance(Locale.KOREA);
        cal.setTime(modifi_customDate(cal.getTime()));
        changeDate(cal);
    }


    private Todo modifi_selectedTodo(SelectedTodo todo) {
        Todo temp_todo = new Todo();
        temp_todo.setCreateDate(today);
        temp_todo.setType(ONCE);
        temp_todo.setContent(todo.getContent());
        temp_todo.setDone(false);
        return temp_todo;
    }

    //시,분,초를 모두 0으로 초기화 해주는 함수입니다.
    public static Date modifi_customDate(Date date) {
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-d");
        ParsePosition pos = new ParsePosition(0);
        return date_format.parse(date_format.format(date), pos);
    }


    @Override
    public UnRegisterAdapter getUnregisterAdapter() {
        return new UnRegisterAdapter(getContext(), dbHelper.readAllTodo().sort("no", Sort.DESCENDING), true, dragListener,this);
    }

    @Override
    public RegisteredAdapter getRegisterAdapter() {
        if (registeredAdapters.containsKey(cal)) {
            return registeredAdapters.get(cal);
        } else {
            registeredAdapters.put(cal, new RegisteredAdapter(getContext(), dbHelper.readSelectedTodoByBelongDate(cal.getTime()).sort("no", Sort.DESCENDING), true, dragListener, this));
        }
        return registeredAdapters.get(cal);
    }

    //받은 날짜를 각각 텍스트뷰에 값을 세팅하는 함수입니다.
    @Override
    public void changeDate(Calendar cal) {
        this.cal = cal;
        cal.setTime(modifi_customDate(cal.getTime()));
        todoView.setCalendar(cal);
    }

    @Override
    public TodoDragListener getDragListener() {
        return dragListener;
    }

    //버튼을 눌러서 날짜를 변경하고 화면에 변경하는 함수입니다.
    @Override
    public void changeOneDay(int day) {
        cal.add(Calendar.DATE, day);
        todoView.setCalendar(cal);
    }


    @Override
    public void draggingMoveDate(int buttonId) {

        alive = true;
        if (buttonId == R.id.btn_todo_before) {
            day = -1;
        } else if (buttonId == R.id.btn_todo_next) {
            day = 1;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (alive) {
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                changeOneDay(day);
                            }
                        });
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();

    }

    @Override
    public void stoppingMoveDate() {
        alive = false;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        dbHelper.dbClose(this);
    }


    //오늘 날짜와 비교하여 값을 리턴하여 줍니다.
    public String compare_date(Date belongDate) {
        if (today.compareTo(belongDate) == 0) {
            return "same";
        } else if (today.compareTo(belongDate) > 0) {
            return "past";
        } else {
            return "future";
        }
    }

    //bottom 아이템을 이제 날짜에 종속시키는데 데이터 부분입니다.
    @Override
    public void register_todo(long no) throws ConcurrentModificationException {
        //날짜 데이트가 있고 올리려는 날짜가 과거가 아닐 시
        if (!compare_date(cal.getTime()).equals("past")) {
            Todo pop_todo = dbHelper.readATodoByNo(no);
            writeTodo(pop_todo, cal.getTime());

            //과거일시
        } else {
            Toast.makeText(getContext(), "Cannot add on past", Toast.LENGTH_SHORT).show();
        }

    }

    //todo를 sTodo 형태로 바꾸어서 register adapter에 올린다.
    private void writeTodo(Todo pop_todo, Date selcted_day) {
        SelectedTodo sTodo = null;
        sTodo = modifi_todo(pop_todo, selcted_day);
        dbHelper.deleteTodo(pop_todo.getNo());
        dbHelper.writeSelectedTodo(sTodo.getType(), sTodo.getContent(), selcted_day, selcted_day);

    }


    //todo를 selectedTodo 형태로 바꾼다.
    private SelectedTodo modifi_todo(Todo todo, Date target_date) {
        SelectedTodo temp_todo = new SelectedTodo();
        temp_todo.setDone(false);
        temp_todo.setContent(todo.getContent());
        temp_todo.setBelongDate(target_date);
        temp_todo.setPutDate(today);
        return temp_todo;
    }


    //selectedTodo의 beleong date를 변경하여 소속을 변경하는 함수입니다.
    @Override
    public void changeBelongDate(long no) {
        if (!compare_date(cal.getTime()).equals("past")) {
            SelectedTodo temp_todo = dbHelper.readASelectedTodoByNO(no);
            if (temp_todo.getBelongDate().compareTo(cal.getTime()) != 0)
                dbHelper.modifySelectedTodo(temp_todo.getNo(), temp_todo.isDone(), temp_todo.getType(), temp_todo.getContent(), cal.getTime(), temp_todo.getPutDate());
        } else {
            Toast.makeText(getContext(), "Cannot add on past", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void register_cancle(long no) {
        SelectedTodo sTodo = dbHelper.readASelectedTodoByNO(no);
        Todo todo = modifi_selectedTodo(sTodo);
        dbHelper.createTodo(todo.getType(), todo.getContent(), cal.getTime());
        dbHelper.deleteSelectedTodo(no);
    }


    //드래깅한 아이템을 삭제하는 함수
    @Override
    public void deleteTodo(String type, long no) {
        if (type.equals(TodoViewTag.SELECTED_TODO)) {
            dbHelper.deleteSelectedTodo(no);
        } else if (type.equals(TodoViewTag.TODO))
            dbHelper.deleteTodo(no);
    }



    @Override
    public void setDone(SelectedTodo todo,boolean done) {
        Log.e("tag","dbhelper setDone = " + todo.isDone());
        dbHelper.modifySelectedTodo(todo.getNo(),done,todo.getType(),todo.getContent(),todo.getBelongDate(),todo.getPutDate());
    }

    @Override
    public void checkDelete(String type, long no) {
        todoView.showDeleteDialog(type,no);
    }
}
