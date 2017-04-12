package com.swipememo.swipememo;

import android.app.Application;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.swipememo.swipememo.main.CustomPagerAdapter;
import com.swipememo.swipememo.model.database.DBHelper;
import com.swipememo.swipememo.model.database.RealmHelper;
import com.swipememo.swipememo.model.types.Todo;
import com.swipememo.swipememo.viewer.fragments.todo.controller.TodoControllerFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ViewPager vp;
    ArrayList<Fragment>fragments;
    CustomPagerAdapter customPagerAdapter;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vp = (ViewPager)findViewById(R.id.viewPager);
        TodoControllerFragment todoFragment = TodoControllerFragment.newInstance();
        fragments = new ArrayList<>();
        fragments.add(todoFragment);
        customPagerAdapter = new CustomPagerAdapter(fragments,getSupportFragmentManager());
        vp.setAdapter(customPagerAdapter);
    }
}
