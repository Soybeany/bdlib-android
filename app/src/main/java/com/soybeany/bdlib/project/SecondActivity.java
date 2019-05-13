package com.soybeany.bdlib.project;

import android.view.View;

import com.soybeany.bdlib.android.mvp.IPresenterProvider;
import com.soybeany.bdlib.android.mvp.MvpPlugin;
import com.soybeany.bdlib.android.template.BaseActivity;
import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;
import com.soybeany.bdlib.android.template.plugins.extend.ThemePlugin;
import com.soybeany.bdlib.android.util.LogUtils;
import com.soybeany.bdlib.android.util.ToastUtils;
import com.soybeany.bdlib.android.util.dialog.ProgressNotifyDialogFragment;
import com.soybeany.bdlib.android.util.dialog.msg.DialogCallbackMsg;
import com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg;
import com.soybeany.bdlib.core.util.notify.Notifier;

import java.util.List;

/**
 * <br>Created by Soybeany on 2019/4/15.
 */
public class SecondActivity extends BaseActivity implements ITestView, MvpPlugin.ITemplate {
    private ThemePlugin mThemePlugin;

    private TestPresenter mPt;
    private Notifier<DialogInvokerMsg, DialogCallbackMsg> mDialogNotifier;

    @Override
    public int setupLayoutResId() {
        return R.layout.activity_second;
    }

    @Override
    public void onInitPresenters(IPresenterProvider provider) {
        mPt = provider.getPresenter(TestPresenter.class, this);
    }

    @Override
    public void onInitViews() {
        ProgressNotifyDialogFragment fragment = new ProgressNotifyDialogFragment();
        fragment.bind(this);
        mDialogNotifier = fragment.getNotifier();
    }

    @Override
    public void showMsg(String desc, String msg) {
        LogUtils.test(desc + "有效果");
        ToastUtils.show(desc + msg);
    }

    public void onClick(View view) {
        mPt.testFile(mDialogNotifier);
//        mThemePlugin.toTheme(ThemeChanger.Info.theme(R.style.NoActionBar));
    }

    @Override
    public void onSetupPlugins(List<IExtendPlugin> plugins) {
        super.onSetupPlugins(plugins);
        plugins.add(new MvpPlugin(this, this, this));
        plugins.add(mThemePlugin = new ThemePlugin(this, MainActivity.THEME_DATA));
    }
}
