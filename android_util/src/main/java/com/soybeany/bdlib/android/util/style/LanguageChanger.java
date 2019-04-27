package com.soybeany.bdlib.android.util.style;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.Locale;

/**
 * <br>Created by Soybeany on 2019/4/21.
 */
public class LanguageChanger implements IQualifierChanger<Locale> {
    @Override
    public void applyChange(AppCompatActivity activity, @Nullable Locale locale) {
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    @Override
    public void recreate(AppCompatActivity activity, @Nullable Locale locale) {
        IQualifierChanger.recreate(activity, activity.getResources().getConfiguration().locale, locale, this);
    }
}
