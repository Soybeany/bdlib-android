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
        boolean needRecreate = true;
        // 应用夜间模式
        if (!newInfo.equals(appliedData)) {
            int mode = (null != newInfo.globalMode ? newInfo.globalMode : newInfo.mode);
            AppCompatDelegate.setDefaultNightMode(mode);
            needRecreate = !activity.getDelegate().applyDayNight();
        }
        // 按需重建
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
        public int mode;
        public Integer globalMode;
        int resId;

        public static Info theme(@StyleRes int resId) {
            return new Info().copy(AppCompatDelegate.MODE_NIGHT_NO, null, resId);
        }

        /**
         * 夜间模式作为
         */
        public static Info nightMode(@StyleRes int resId) {
            return new Info().copy(AppCompatDelegate.MODE_NIGHT_YES, null, resId);
        }

        Info() {
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj instanceof Info) {
                return mode == ((Info) obj).mode && resId == ((Info) obj).resId
                        && (globalMode == null && ((Info) obj).globalMode == null || globalMode != null && globalMode.equals(obj));
            }
            return super.equals(obj);
        }

        void copy(Info info) {
            copy(info.mode, info.globalMode, info.resId);
        }

        private Info copy(int mode, Integer globalMode, @StyleRes int resId) {
            this.mode = mode;
            this.globalMode = globalMode;
            this.resId = resId;
            return this;
        }
    }
}
