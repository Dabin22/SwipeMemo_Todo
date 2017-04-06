package com.swipememo.swipememo.model.settings;

/**
 * Created by DoDo on 2017-02-27.
 */

public interface SettingHelper{
    String NAME = "SETTINGS";
    String WIDTH_RATIO= "width_ratio";
    String MEMO_USE = "memo_use";
    String FIRST_VISIT ="first_visit";


    void addListener(Listener listener);
    boolean isFirst();
    void setMemoUse(boolean memoUse);
    boolean isMemoUse();
    void setBarWidthRatio(int ratio);
    int getBarWidthRatio();

}
