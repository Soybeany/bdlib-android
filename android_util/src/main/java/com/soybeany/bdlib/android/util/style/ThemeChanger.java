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

        saveInfo(activity, newData);
    }

    @Override
    public void onRecreate(AppCompatActivity activity, @Nullable Info appliedData, @NonNull Info newInfo) {
        // 应用夜间模式
        boolean needRecreate = !applyNightMode(activity, newInfo.mode);
        // 按需重建，因为上一步可能已经重建了
        if (needRecreate) {
            activity.recreate();
        }
    }

    @Override
    public Info getAppliedData(AppCompatActivity activity) {
        return getInfo(activity);
    }

    /**
     * 获得当前的夜间模式
     */
    public int getCurNightMode() {
        return AppCompatDelegate.getDefaultNightMode();
    }

    /**
     * 应用夜间模式
     *
     * @return 是否生效
     */
    private boolean applyNightMode(AppCompatActivity activity, int mode) {
        if (mode == getCurNightMode()) {
            return false;
        }
        AppCompatDelegate.setDefaultNightMode(mode);
        return activity.getDelegate().applyDayNight();
    }

    private void saveInfo(AppCompatActivity activity, @NonNull Info info) {
        getInfo(activity).copy(info);
    }

    @NonNull
    private Info getInfo(AppCompatActivity activity) {
        Intent intent = activity.getIntent();
        Info info = (Info) intent.getSerializableExtra(OLD_VALUE_KEY);
        if (null == info) {
            intent.putExtra(OLD_VALUE_KEY, info = new Info());
        }
        return info;
    }

    public static class Info implements Serializable {
        int mode;
        int resId;

        public static Info theme(@StyleRes int resId) {
            return theme(AppCompatDelegate.MODE_NIGHT_NO, resId);
        }

        public static Info theme(int mode, @StyleRes int resId) {
            return new Info().copy(mode, resId);
        }

        Info() {
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj instanceof Info) {
                return mode == ((Info) obj).mode && resId == ((Info) obj).resId;
            }
            return super.equals(obj);
        }

        void copy(Info info) {
            copy(info.mode, info.resId);
        }

        private Info copy(int mode, @StyleRes int resId) {
            this.mode = mode;
            this.resId = resId;
            return this;
        }
    }
}
