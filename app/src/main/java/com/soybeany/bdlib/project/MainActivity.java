package com.soybeany.bdlib.project;

import android.content.Intent;
import android.view.View;

import com.soybeany.bdlib.android.template.BaseActivity;
import com.soybeany.bdlib.android.util.LogUtils;
import com.soybeany.bdlib.android.util.style.LanguageChanger;
import com.soybeany.bdlib.android.util.style.ThemeChanger;

import java.util.Locale;


public class MainActivity extends BaseActivity {

    public static final ThemeChanger.Info theme1 = ThemeChanger.Info.theme(R.style.AppTheme);
    public static final ThemeChanger.Info theme2 = ThemeChanger.Info.theme(R.style.NoActionBar);

    public static Locale language = Locale.CHINESE;
    public static ThemeChanger.Info theme = theme1;


    private LanguageChanger c = new LanguageChanger();
    private ThemeChanger tc = new ThemeChanger();

    @Override
    public void beforeSetupContentView() {
        LogUtils.test("全局:" + getApplication().getResources().getConfiguration().locale);
        LogUtils.test("本地:" + getResources().getConfiguration().locale);
        c.applyChange(this, language);
        tc.applyChange(this, theme);
    }

    @Override
    public int setupLayoutResId() {
        return R.layout.activity_main;
    }

    public void onClick(View view) {
        startActivity(new Intent(this, SecondActivity.class));
//        language = (language == Locale.CHINESE ? Locale.ENGLISH : Locale.CHINESE);
//        view.postDelayed(() -> c.recreate(this, language), 200);
    }

    public void onClickTheme(View view) {
        theme = (theme == theme1 ? theme2 : theme1);
        view.postDelayed(() -> tc.recreate(this, theme), 200);
    }
}
