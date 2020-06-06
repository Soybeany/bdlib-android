package com.soybeany.bdlib.android.ui.toolbar;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

/**
 * <br>Created by Soybeany on 2019/4/26.
 */
public class ActionBarHelper {
    @Nullable
    private ActionBar mActionBar;

    public ActionBarHelper(AppCompatActivity activity) {
        mActionBar = activity.getSupportActionBar();
        if (null != mActionBar) {
            onSetupActionBar(mActionBar);
        }
    }

    public void setBackEnable(boolean flag) {
        if (null != mActionBar) {
            mActionBar.setDisplayHomeAsUpEnabled(flag);
        }
    }

    protected void onSetupActionBar(@NonNull ActionBar actionBar) {
        setBackEnable(true); // 默认显示返回上一层的箭头
    }
}
