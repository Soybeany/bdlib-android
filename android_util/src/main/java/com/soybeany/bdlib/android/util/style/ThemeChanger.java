package com.soybeany.bdlib.android.util.style;

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

    private Integer mNightMode; // 全局的夜间模式设置

    @Override
    public void onApply(AppCompatActivity activity, @NonNull Info newData) {
        activity.getApplication().setTheme(newData.resId);
        activity.setTheme(newData.resId);

        saveMode(activity, newData);
    }

    @Override
    public void onRecreate(AppCompatActivity activity, @Nullable Info oldInfo, @NonNull Info newInfo) {
        boolean needRecreate = true;
        // 使用全局配置
        if (null != mNightMode) {
            newInfo.mode = mNightMode;
        }
        // 应用夜间模式
        if (null != oldInfo && !oldInfo.equals(newInfo)) {
            AppCompatDelegate.setDefaultNightMode(newInfo.mode);
            needRecreate = !activity.getDelegate().applyDayNight();
        }
        // 按需重建
        if (needRecreate) {
            activity.recreate();
        }
    }

    @Override
    public Info getOldData(AppCompatActivity activity) {
        return getMode(activity);
    }

    @Override
    public boolean needForceRecreate() {
        return null != mNightMode;
    }

    /**
     * 设置全局的夜间模式
     *
     * @param mode 不为null时，将替换Info中的值
     */
    public void setNightMode(AppCompatActivity activity, Integer mode) {
        mNightMode = mode;
        recreate(activity, getOldData(activity));
    }

    /**
     * 获得当前的夜间模式
     */
    public int getCurNightMode() {
        return AppCompatDelegate.getDefaultNightMode();
    }

    private void saveMode(AppCompatActivity activity, @NonNull Info info) {
        Info oldInfo = getMode(activity);
        if (null == oldInfo) {
            activity.getIntent().putExtra(OLD_VALUE_KEY, oldInfo = new Info());
        }
        oldInfo.copy(info);
    }

    @Nullable
    private Info getMode(AppCompatActivity activity) {
        return (Info) activity.getIntent().getSerializableExtra(OLD_VALUE_KEY);
    }

    public static class Info implements Serializable, Cloneable {
        int mode;
        int resId;

        public static Info theme(@StyleRes int resId) {
            return new Info().copy(AppCompatDelegate.MODE_NIGHT_NO, resId);
        }

        /**
         * 夜间模式作为
         */
        public static Info nightMode(@StyleRes int resId) {
            return new Info().copy(AppCompatDelegate.MODE_NIGHT_YES, resId);
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

        public void copy(Info info) {
            copy(info.mode, info.resId);
        }

        private Info copy(int mode, @StyleRes int resId) {
            this.mode = mode;
            this.resId = resId;
            return this;
        }
    }
}
