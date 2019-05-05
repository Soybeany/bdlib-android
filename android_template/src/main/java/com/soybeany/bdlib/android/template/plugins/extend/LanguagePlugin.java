package com.soybeany.bdlib.android.template.plugins.extend;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.soybeany.bdlib.android.util.style.LanguageChanger;

import java.util.Locale;

/**
 * 只在{@link Activity}中使用
 * <br>Created by Soybeany on 2019/4/29.
 */
public class LanguagePlugin extends StylePlugin<Locale> {
    private static final LanguageChanger CHANGER = new LanguageChanger();

    public LanguagePlugin(@NonNull AppCompatActivity activity) {
        super(activity, CHANGER);
    }

    @NonNull
    @Override
    public final String getGroupId() {
        return "Language";
    }

    public void toLanguage(Locale locale) {
        setData(locale);
    }

    public Locale curLanguage() {
        return getData();
    }

    public interface ISetter {
        void toLanguage(Locale locale);
    }

    public interface IGetter {
        Locale curLanguage();
    }
}
