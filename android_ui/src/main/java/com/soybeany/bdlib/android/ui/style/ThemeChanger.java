package com.soybeany.bdlib.android.ui.style;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import com.soybeany.bdlib.core.java8.Optional;

/**
 * <br>Created by Soybeany on 2019/4/21.
 */
public class ThemeChanger implements IQualifierChanger<Integer> {
    @Override
    public void change(AppCompatActivity activity, @Nullable Integer mode) {
        Optional.ofNullable(mode).ifPresent(AppCompatDelegate::setDefaultNightMode);
    }

    @Override
    public void recreate(AppCompatActivity activity, @Nullable Integer integer) {
        IQualifierChanger.recreate(activity, AppCompatDelegate.getDefaultNightMode(), integer);
    }
}
