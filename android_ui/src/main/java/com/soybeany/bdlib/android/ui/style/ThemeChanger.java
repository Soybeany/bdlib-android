package com.soybeany.bdlib.android.ui.style;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

/**
 * <br>Created by Soybeany on 2019/4/21.
 */
public class ThemeChanger implements IQualifierChanger<Integer> {
    @Override
    public void change(AppCompatActivity activity, Integer mode) {
        AppCompatDelegate.setDefaultNightMode(mode);
    }
}
