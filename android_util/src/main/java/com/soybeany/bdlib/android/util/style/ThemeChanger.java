package com.soybeany.bdlib.android.util.style;

import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import java.util.WeakHashMap;

/**
 * <br>Created by Soybeany on 2019/4/21.
 */
public class ThemeChanger implements IQualifierChanger<ThemeChanger.Mode> {
    private static final WeakHashMap<String, Mode> OLD_VALUE_MAP = new WeakHashMap<>();

    @Override
    public void change(AppCompatActivity activity, @Nullable Mode mode) {
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
        OLD_VALUE_MAP.put(activity.toString(), mode);
    }

    @Override
    public void recreate(AppCompatActivity activity, @Nullable Mode mode) {
        IQualifierChanger.recreate(activity, OLD_VALUE_MAP.get(activity.toString()), mode, Mode.class);
    }

    public static class Mode {
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
