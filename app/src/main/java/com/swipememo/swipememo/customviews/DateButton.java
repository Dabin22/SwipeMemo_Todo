package com.swipememo.swipememo.customviews;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.Button;
import com.swipememo.swipememo.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by DoDo on 2017-03-20.
 */

public class DateButton extends Button {

    private boolean repeat = false;
    private boolean hasDate = false;
    private Calendar date = null;
    public DateButton(Context context) {
        this(context, null);
    }

    public DateButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DateButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DateButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    private Spanned lineText(CharSequence text){
        String who ="<u>"+text+"</u>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(who,0);
        }
        return Html.fromHtml(who);
    }
    @Override
    public void setText(CharSequence text, BufferType type) {
        if(text == null){
            super.setText("", type);
            return;
        }
        super.setText(lineText(text), type);
    }
    public void setDate(Calendar calendar){
        this.date = calendar;
        setText(new StringBuffer(calendar.get(Calendar.MONTH))
                .append(".")
                .append(calendar.get(Calendar.DATE)).toString());
        hasDate = true;
    }
    public void setRepeat(boolean isRepeat){
        repeat = isRepeat;
        if(repeat){
            setBackgroundResource(R.drawable.todo_icon_repeat);
            setText(null);
        }else{
            setBackgroundResource(R.drawable.td_btn_calendar_icon);
        }
    }
    public Calendar getDate(){
        return date;
    }
    public boolean isRepeat(){ return repeat;}
}
