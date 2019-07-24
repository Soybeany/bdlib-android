package com.soybeany.bdlib.android.util.style;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.soybeany.bdlib.android.util.BDContext;

import java.util.Locale;

/**
 * <br>Created by Soybeany on 2019/4/21.
 */
public class LanguageChanger implements IQualifierChanger<Locale> {
    @Override
    public void onApply(AppCompatActivity activity, @NonNull Locale newData) {
        setLocale(BDContext.getResources(), newData);
        setLocale(activity.getResources(), newData);
    }

    @Override
    public Locale getAppliedData(AppCompatActivity activity) {
        return activity.getResources().getConfiguration().locale;
    }

    /**
     * 设置本地
     */
    private void setLocale(Resources resources, @NonNull Locale newData) {
        Configuration config = resources.getConfiguration();
        if (!newData.equals(config.locale)) {
            config.setLocale(newData);
            resources.updateConfiguration(config, resources.getDisplayMetrics());
        }
    }
}
