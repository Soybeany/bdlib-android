package com.soybeany.bdlib.android.util.style;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import com.soybeany.bdlib.core.java8.Optional;

import java.io.Serializable;

/**
 * <br>Created by Soybeany on 2019/4/21.
 */
public class ThemeChanger implements IQualifierChanger<ThemeChanger.Info> {
    private static final String OLD_VALUE_KEY = "THEME_CHANGER_OLD_VALUE";

    @Override
    public void applyChange(AppCompatActivity activity, @Nullable Info info) {
        if (null == info) {
            return;
        }
        // 设置主题
        Optional.ofNullable(info.resId).ifPresent(activity::setTheme);
        // 设置模式
        AppCompatDelegate.setDefaultNightMode(info.mode);

        saveMode(activity, info);
    }

    @Override
    public void recreate(AppCompatActivity activity, @Nullable Info info) {
        IQualifierChanger.recreate(activity, getMode(activity), info, this);
    }

    @Override
    public void onRecreate(AppCompatActivity activity, @Nullable Info oldInfo, @NonNull Info newInfo) {
        if (null != oldInfo && oldInfo.mode != newInfo.mode) {
            activity.getDelegate().applyDayNight();
            return;
        }
        activity.recreate();
    }

    /**
     * 获得当前的夜间模式
     */
    public int getCurNightMode() {
        return AppCompatDelegate.getDefaultNightMode();
    }

    private void saveMode(AppCompatActivity activity, @NonNull Info info) {
        activity.setIntent(new Intent(activity.getIntent()).putExtra(OLD_VALUE_KEY, info));
    }

    @Nullable
    private Info getMode(AppCompatActivity activity) {
        return (Info) activity.getIntent().getSerializableExtra(OLD_VALUE_KEY);
    }

    public static class Info implements Serializable {
        int mode;
        @Nullable
        Integer resId;

        public static Info theme(@StyleRes int resId) {
            return new Info(AppCompatDelegate.MODE_NIGHT_NO, resId);
        }

        public static Info nightMode() {
            return new Info(AppCompatDelegate.MODE_NIGHT_YES, null);
        }

        Info(int mode, @Nullable @StyleRes Integer resId) {
            this.mode = mode;
            this.resId = resId;
        }
    }
}
