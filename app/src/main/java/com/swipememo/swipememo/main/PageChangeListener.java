package com.swipememo.swipememo.main;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.swipememo.swipememo.viewer.fragments.todo.controller.TodoControllerFragment;

import java.util.ArrayList;

/**
 * Created by Dabin on 2017-01-10.
 */

public class PageChangeListener implements ViewPager.OnPageChangeListener {
    private ArrayList<Fragment> fragments = null;
    private TodoControllerFragment todoFragment = null;

    public PageChangeListener(ArrayList<Fragment> fragments) {
        this.fragments = fragments;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 1) {
            if (fragments.size() > 0)
                todoFragment = (TodoControllerFragment)fragments.get(position);

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
