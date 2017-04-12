package com.swipememo.swipememo;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by DoDo on 2017-04-12.
 */

public class SwipeMemo_Todo extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());
    }
}
