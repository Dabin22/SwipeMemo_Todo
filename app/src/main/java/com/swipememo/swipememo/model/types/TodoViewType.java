package com.swipememo.swipememo.model.types;

import java.util.Date;

/**
 * Created by Dabin on 2016-11-27.
 */

public class TodoViewType {
    public final static String SELECTED_TODO = "SelectedTodo";
    public final static String TODO = "Todo";
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
