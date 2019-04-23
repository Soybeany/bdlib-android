package com.soybeany.bdlib.project;

import android.arch.lifecycle.MutableLiveData;
import android.view.View;

import com.soybeany.bdlib.android.template.BaseActivity;
import com.soybeany.bdlib.android.ui.style.LanguageChanger;

import java.util.Locale;


public class MainActivity extends BaseActivity {

    public static MutableLiveData<Locale> language = new MutableLiveData<>();

    LanguageChanger c = new LanguageChanger();

    @Override
    public void beforeSetupContentView() {
        c.change(this, language.getValue());
    }

    @Override
    public int setupLayoutResId() {
        return R.layout.activity_main;
    }

    public void onClick(View view) {
//        startActivity(new Intent(this, SecondActivity.class));
        Locale value = language.getValue();
        if (null == value || value == Locale.CHINESE) {
            language.setValue(Locale.ENGLISH);
        } else {
            language.setValue(Locale.CHINESE);
        }

        view.postDelayed(() -> c.recreate(this, null), 200);
    }
}
