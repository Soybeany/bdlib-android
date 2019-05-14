package com.soybeany.bdlib.project;

import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.view.View;

import com.soybeany.bdlib.android.template.BaseActivity;
import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;
import com.soybeany.bdlib.android.template.plugins.extend.ThemePlugin;
import com.soybeany.bdlib.android.util.LogUtils;
import com.soybeany.bdlib.android.util.dialog.ProgressNotifyDialogFragment;
import com.soybeany.bdlib.android.util.style.ThemeChanger;
import com.soybeany.bdlib.android.util.system.PermissionRequester;

import java.util.List;
import java.util.Locale;
import java.util.Set;


public class MainActivity extends BaseActivity {

    public static final ThemeChanger.Info theme1 = ThemeChanger.Info.theme(R.style.AppTheme);
    public static final ThemeChanger.Info theme2 = ThemeChanger.Info.theme(R.style.NoActionBar);

    public static Locale language = Locale.CHINESE;
    public static ThemeChanger.Info theme = theme1;

    public static MutableLiveData<ThemeChanger.Info> THEME_DATA = new MutableLiveData<>();

    private ThemePlugin mThemePlugin;

    ProgressNotifyDialogFragment fragment = new ProgressNotifyDialogFragment();

    @Override
    public int setupLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void doBusiness(boolean isNew) {
        LogUtils.test(this + " : " + isNew);
    }

    @Override
    public void onSetupEssentialPermissions(Set<String> permissions) {
        permissions.add(PermissionRequester.READ_EXTERNAL_STORAGE);
        permissions.add(PermissionRequester.WRITE_EXTERNAL_STORAGE);
    }

    public void onClick(View view) {
        startActivity(new Intent(this, SecondActivity.class));
//        language = (language == Locale.CHINESE ? Locale.ENGLISH : Locale.CHINESE);
//        view.postDelayed(() -> c.recreate(this, language), 200);

//        requestPermissions(() -> {
//            ToastUtils.show("授权成功");
//        }, PermissionRequester.READ_PHONE_STATE);
//        fragment.show(getSupportFragmentManager(), null);
    }

    @Override
    public void onSetupPlugins(List<IExtendPlugin> plugins) {
        super.onSetupPlugins(plugins);
        plugins.add(mThemePlugin = new ThemePlugin(this, THEME_DATA));
    }

    public void onClickTheme(View view) {
        theme = (theme == theme1 ? theme2 : theme1);
        view.postDelayed(() -> mThemePlugin.toTheme(theme), 200);
    }
}
