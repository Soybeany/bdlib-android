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

    public ThemeChanger.Info curTheme() {
        return getData();
    }

    public interface ISetter {
        void toTheme(ThemeChanger.Info info);
    }

    public interface IGetter {
        ThemeChanger.Info curTheme();
    }
}
