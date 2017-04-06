package com.swipememo.swipememo.model.settings;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by DoDo on 2017-02-26.
 * SingleTone Pattern is applied
 */

public class PreferenceHelper implements SettingHelper{

    private static PreferenceHelper instance = new PreferenceHelper();
    private static SharedPreferences mP = null;

    private PreferenceHelper() {
    }

    public static PreferenceHelper getInstance(Context context){
        if(mP == null)
            mP = context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return instance;
    }

    @Override
    public void addListener(Listener listener) {
        mP.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public boolean isFirst(){
        boolean result = mP.getBoolean(FIRST_VISIT,true);
        changeBoolean(FIRST_VISIT,false);
        return result;
    }

    @Override
    public void setMemoUse(boolean memoUse){
        changeBoolean(MEMO_USE,memoUse);
    }

    @Override
    public boolean isMemoUse(){
        return mP.getBoolean(MEMO_USE,true);
    }

    @Override
    public void setBarWidthRatio(int ratio) {

    }

    @Override
    public int getBarWidthRatio() {
        return 0;
    }


    private void  changeBoolean(String name, boolean bool){
        mP.edit().putBoolean(name,bool).apply();
    }
    private void changefloat(String name, float f){
        mP.edit().putFloat(name,f).apply();
    }

}
