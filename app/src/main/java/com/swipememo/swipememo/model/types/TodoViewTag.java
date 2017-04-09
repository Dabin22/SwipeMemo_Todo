package com.swipememo.swipememo.model.types;

/**
 * Created by Dabin on 2016-11-27.
 */

public class TodoViewTag {
    public final static String SELECTED_TODO = "SelectedTodo";
    public final static String TODO = "Todo";
    public final static String REGISTERLIST = "registerList";
    public final static String BEFORE_REGISTERLIST = "before_registerList";
    private String type;
    private long no;

    public long getNo() {
        return no;
    }

    public void setNo(long no) {
        this.no = no;
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
