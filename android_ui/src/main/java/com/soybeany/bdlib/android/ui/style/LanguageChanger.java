package com.soybeany.bdlib.android.ui.style;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;

import java.util.Locale;

/**
 * <br>Created by Soybeany on 2019/4/21.
 */
public class LanguageChanger implements IQualifierChanger<Locale> {
    @Override
    public void change(AppCompatActivity activity, Locale locale) {
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    @Override
    public void recreate(AppCompatActivity activity, Locale locale) {
        IQualifierChanger.recreate(activity, activity.getResources().getConfiguration().locale, locale);
    }
}