package com.soybeany.bdlib.android.template.plugins.extend;

import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.soybeany.bdlib.android.util.style.ThemeChanger;

/**
 * 只在{@link Activity}中使用
 * <br>Created by Soybeany on 2019/4/29.
 */
public class ThemePlugin extends StylePlugin<ThemeChanger.Info> {
    private static final ThemeChanger CHANGER = new ThemeChanger();

    public ThemePlugin(@NonNull AppCompatActivity activity, @NonNull MutableLiveData<ThemeChanger.Info> data) {
        super(activity, CHANGER, data);
    }

    @NonNull
    @Override
    public final String getGroupId() {
        return "Theme";
    }

    public void toTheme(ThemeChanger.Info info) {
        setData(info);
    }

    /**
     * 设置全局夜间模式
     *
     * @param mode null为不使用全局设置
     */
    public void setGlobalNightMode(Integer mode) {
        ThemeChanger.Info info = CHANGER.getAppliedData(mActivity);
        info.globalMode = mode;
        toTheme(info);
    }

    /**
     * 获得全局夜间模式
     */
    public Integer getGlobalNightMode() {
        return CHANGER.getAppliedData(mActivity).globalMode;
    }

    public ThemeChanger.Info curTheme() {
        return getData();
    }

    public interface ISetter {
        void toTheme(ThemeChanger.Info info);

        void setNightMode(int mode);
    }

    public interface IGetter {
        ThemeChanger.Info curTheme();

        Integer getGlobalNightMode();
    }
}
