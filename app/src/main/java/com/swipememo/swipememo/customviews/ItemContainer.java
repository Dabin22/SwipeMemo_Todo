package com.swipememo.swipememo.customviews;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by DoDo on 2017-03-20.
 */

public class ItemContainer extends FrameLayout implements FlingCardView.FlingCardViewListener{
    private String TAG ="ItemContainer";

    private GestureDetector gd;
    private MenuView menu = null;
    private FlingCardView content = null;
    private boolean clickable = true;

    private boolean initial = true;

    public ItemContainer(Context context) {
        this(context,null);
    }

    public ItemContainer(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ItemContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ItemContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        gd = new GestureDetector(getContext(),new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.e(TAG,"long pressed");
                if(clickable) {
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                            ItemContainer.this);
                    if(Build.VERSION.SDK_INT >23){
                        ItemContainer.this.startDragAndDrop(null,shadowBuilder,ItemContainer.this,0);
                    } else{
                        ItemContainer.this.startDrag(null, shadowBuilder, ItemContainer.this, 0);
                    }
                    ItemContainer.this.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(initial){
            getContent().addFlingCardViewListener(this);
            initial = false;
        }
    }

    public void setOnClickMenuButton(View.OnClickListener listener){
        getMenu().setOnClickListener(listener);
    }
    public void addOnFlingCardViewListener(FlingCardView.FlingCardViewListener flingCardViewListener){
        getContent().addFlingCardViewListener(flingCardViewListener);
    }

    public FlingCardView getContent(){
        if(content != null)
            return content;
        for(int i = 0; i < getChildCount() ; i ++){
            if(getChildAt(i) instanceof FlingCardView){
                return (FlingCardView) getChildAt(i);
            }
        }
        throw new IllegalArgumentException("Can't find andy FlingCardView");
    }
    public MenuView getMenu(){
        if(menu != null)
            return menu;
        for(int i = 0; i < getChildCount() ; i ++){
            if(getChildAt(i) instanceof MenuView){
                return (MenuView) getChildAt(i);
            }
        }
        throw new IllegalArgumentException("Can't find andy MenuView");
    }
    public void reset(){
        getContent().close();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gd.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        gd.onTouchEvent(ev);
        return super.onInterceptTouchEvent(ev);
    }
    public void setClickable(boolean clickable){
        this.clickable = clickable;
    }

    //FlingCardViewListener Implements

    @Override
    public void onFling(FlingCardView view, float velocityX, float velocityY) {
//        Log.e("ItemContainer","FlingCardViewListener trigger onFLing");
//        if(velocityX<0){
//            getMenu().setVisibility(VISIBLE);
//        }else{
//            getMenu().setVisibility(GONE);
//        }
    }

    @Override
    public void afterFling(FlingCardView view, float velocityX, float velocityY) {

    }

    @Override
    public void onOpen(FlingCardView view) {

    }

    @Override
    public void onClose(FlingCardView view) {

    }
}
