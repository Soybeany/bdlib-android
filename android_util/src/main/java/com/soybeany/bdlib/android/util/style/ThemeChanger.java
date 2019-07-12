package com.soybeany.bdlib.android.util.style;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import java.io.Serializable;

/**
 * <br>Created by Soybeany on 2019/4/21.
 */
public class ThemeChanger implements IQualifierChanger<ThemeChanger.Info> {
    private static final String OLD_VALUE_KEY = "THEME_CHANGER_OLD_VALUE";

    @Override
    public void onApply(AppCompatActivity activity, @NonNull Info newData) {
        activity.getApplication().setTheme(newData.resId);
        activity.setTheme(newData.resId);

        saveMode(activity, newData);
    }

    @Override
    public void onRecreate(AppCompatActivity activity, @Nullable Info oldInfo, @NonNull Info newInfo) {
        boolean needRecreate = true;
        if (null != oldInfo && oldInfo.mode != newInfo.mode) {
            AppCompatDelegate.setDefaultNightMode(newInfo.mode);
            needRecreate = !activity.getDelegate().applyDayNight();
        }
        if (needRecreate) {
            activity.recreate();
        }
    }

    @Override
    public Info getOldData(AppCompatActivity activity) {
        return getMode(activity);
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
        int resId;

        public static Info theme(@StyleRes int resId) {
            return new Info(AppCompatDelegate.MODE_NIGHT_NO, resId);
        }

        public static Info nightMode(@StyleRes int resId) {
            return new Info(AppCompatDelegate.MODE_NIGHT_YES, resId);
        }

        Info(int mode, @StyleRes int resId) {
            this.mode = mode;
            this.resId = resId;
        }
    }
}
