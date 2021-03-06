package com.soybeany.bdlib.android.template.plugins.extend;

import android.app.Activity;

import com.soybeany.bdlib.android.util.style.LanguageChanger;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

/**
 * 只在{@link Activity}中使用
 * <br>Created by Soybeany on 2019/4/29.
 */
public class LanguagePlugin extends StylePlugin<Locale> {
    private static final LanguageChanger CHANGER = new LanguageChanger();

    public LanguagePlugin(@NonNull AppCompatActivity activity, @NonNull MutableLiveData<Locale> data) {
        super(activity, CHANGER, data);
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
