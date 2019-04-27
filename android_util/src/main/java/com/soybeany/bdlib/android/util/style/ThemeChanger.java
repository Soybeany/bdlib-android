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
public class ThemeChanger implements IQualifierChanger<ThemeChanger.Mode> {
    private static final String OLD_VALUE_KEY = "THEME_CHANGER_OLD_VALUE";

    @Override
    public void applyChange(AppCompatActivity activity, @Nullable Mode mode) {
        if (null == mode) {
            return;
        }
        switch (mode.type) {
            case ThemeMode.TYPE:
                activity.setTheme(mode.data);
                break;
            case NightMode.TYPE:
                AppCompatDelegate.setDefaultNightMode(mode.data);
                break;
        }
        saveMode(activity, mode);
    }

    @Override
    public void recreate(AppCompatActivity activity, @Nullable Mode mode) {
        IQualifierChanger.recreate(activity, getMode(activity), mode, this);
    }

    @Override
    public void onRecreate(AppCompatActivity activity, @Nullable Mode oldMode, @NonNull Mode newMode) {
        if (oldMode instanceof NightMode || newMode instanceof NightMode) {
            activity.getDelegate().applyDayNight();
            return;
        }
        activity.recreate();
    }

    private void saveMode(AppCompatActivity activity, @NonNull Mode mode) {
        activity.setIntent(new Intent(activity.getIntent()).putExtra(OLD_VALUE_KEY, mode));
    }

    @Nullable
    private Mode getMode(AppCompatActivity activity) {
        return (Mode) activity.getIntent().getSerializableExtra(OLD_VALUE_KEY);
    }

    public static class Mode implements Serializable {
        String type;
        int data;

        Mode(String type, int data) {
            this.type = type;
            this.data = data;
        }
    }

    /**
     * 主题模式
     */
    public static class ThemeMode extends Mode {
        static final String TYPE = "THEME";

        public ThemeMode(@StyleRes int resId) {
            super(TYPE, resId);
        }
    }

    /**
     * 夜间模式
     */
    public static class NightMode extends Mode {
        static final String TYPE = "NIGHT";

        public NightMode(int mode) {
            super(TYPE, mode);
        }
    }
}
