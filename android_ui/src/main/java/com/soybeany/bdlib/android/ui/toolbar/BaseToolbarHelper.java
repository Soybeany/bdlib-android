package com.soybeany.bdlib.android.ui.toolbar;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * <br>Created by Soybeany on 2019/4/26.
 */
public class BaseToolbarHelper {
    protected ActionBar mActionBar;

    public BaseToolbarHelper(AppCompatActivity activity, @NonNull Toolbar toolbar) {
        activity.setSupportActionBar(toolbar);
        mActionBar = activity.getSupportActionBar();
        setBackEnable(true); // 默认显示返回上一层的箭头
    }

    public void setBackEnable(boolean flag) {
        mActionBar.setDisplayHomeAsUpEnabled(flag);
    }
}
