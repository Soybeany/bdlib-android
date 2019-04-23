package com.soybeany.bdlib.project;

import android.view.View;

import com.soybeany.bdlib.android.template.BaseActivity;
import com.soybeany.bdlib.android.util.style.LanguageChanger;
import com.soybeany.bdlib.android.util.style.ThemeChanger;

import java.util.Locale;


public class MainActivity extends BaseActivity {

    public static Locale language = Locale.CHINESE;
    public static int theme = R.style.AppTheme;


    private LanguageChanger c = new LanguageChanger();
    private ThemeChanger tc = new ThemeChanger();

    @Override
    public void beforeSetupContentView() {
        c.change(this, language);
        tc.change(this, new ThemeChanger.ThemeMode(theme));
    }

    @Override
    public int setupLayoutResId() {
        return R.layout.activity_main;
    }

    public void onClick(View view) {
//        startActivity(new Intent(this, SecondActivity.class));
        language = (language == Locale.CHINESE ? Locale.ENGLISH : Locale.CHINESE);
        view.postDelayed(() -> c.recreate(this, null), 200);
    }

    public void onClickTheme(View view) {
        theme = (theme == R.style.AppTheme ? R.style.NoActionBar : R.style.AppTheme);
        view.postDelayed(() -> c.recreate(this, null), 200);
    }
}
