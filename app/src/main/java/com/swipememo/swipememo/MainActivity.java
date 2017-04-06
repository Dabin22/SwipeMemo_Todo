package com.swipememo.swipememo;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.swipememo.swipememo.main.CustomPagerAdapter;
import com.swipememo.swipememo.viewer.fragments.todo.controller.TodoControllerFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ViewPager vp;
    ArrayList<Fragment>fragments;
    CustomPagerAdapter customPagerAdapter;
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
