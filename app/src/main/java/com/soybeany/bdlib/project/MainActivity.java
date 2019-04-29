package com.soybeany.bdlib.project;

import android.content.Intent;
import android.view.View;

import com.soybeany.bdlib.android.template.BaseActivity;
import com.soybeany.bdlib.android.util.LogUtils;
import com.soybeany.bdlib.android.util.style.LanguageChanger;
import com.soybeany.bdlib.android.util.style.ThemeChanger;
import com.soybeany.bdlib.android.util.system.PermissionRequester;

import java.util.Locale;


public class MainActivity extends BaseActivity {

    public static final ThemeChanger.Info theme1 = ThemeChanger.Info.theme(R.style.AppTheme);
    public static final ThemeChanger.Info theme2 = ThemeChanger.Info.theme(R.style.NoActionBar);

    public static Locale language = Locale.CHINESE;
    public static ThemeChanger.Info theme = theme1;


    private LanguageChanger c = new LanguageChanger();
    private ThemeChanger tc = new ThemeChanger();

    @Override
    public void signalBeforeSetContentView() {
        c.applyChange(this, language);
        tc.applyChange(this, theme);
    }

    @Override
    public int setupLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void doBusiness(boolean isNew) {
        LogUtils.test(this + " : " + isNew);
    }

    @Override
    public String[] onGetEssentialPermissions() {
        return new String[]{PermissionRequester.READ_EXTERNAL_STORAGE, PermissionRequester.WRITE_EXTERNAL_STORAGE};
    }

    public void onClick(View view) {
        startActivity(new Intent(this, SecondActivity.class));
//        language = (language == Locale.CHINESE ? Locale.ENGLISH : Locale.CHINESE);
//        view.postDelayed(() -> c.recreate(this, language), 200);

//        requestPermissions(() -> {
//            ToastUtils.show("授权成功");
//        }, PermissionRequester.READ_PHONE_STATE);
    }

    public void onClickTheme(View view) {
        theme = (theme == theme1 ? theme2 : theme1);
        view.postDelayed(() -> tc.recreate(this, theme), 200);
    }
}
