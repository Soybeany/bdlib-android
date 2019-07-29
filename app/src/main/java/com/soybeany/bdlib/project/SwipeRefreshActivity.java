package com.soybeany.bdlib.project;

import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;

import com.soybeany.bdlib.android.mvp.IPresenterProvider;
import com.soybeany.bdlib.android.mvp.MvpPlugin;
import com.soybeany.bdlib.android.template.BaseActivity;
import com.soybeany.bdlib.android.template.interfaces.IPluginManager;
import com.soybeany.bdlib.android.template.plugins.extend.ButterKnifePlugin;
import com.soybeany.bdlib.android.template.plugins.extend.DialogNotifierPlugin;
import com.soybeany.bdlib.android.ui.layout.NotifySRLDialog;
import com.soybeany.bdlib.android.util.ToastUtils;
import com.soybeany.bdlib.android.util.dialog.DialogNotifier;

import butterknife.BindView;

/**
 * <br>Created by Soybeany on 2019/5/17.
 */
public class SwipeRefreshActivity extends BaseActivity implements DialogNotifier.IProvider, DialogNotifier.IDialogProvider,
        ButterKnifePlugin.ICallback, ITestView, MvpPlugin.IActivityCallback {

    @BindView(R.id.srl)
    SwipeRefreshLayout srLayout;

    private TestPresenter mPt;
    private DialogNotifierPlugin mDialogNotifierPlugin;

    @Override
    public int setupLayoutResId() {
        return R.layout.activity_swipe_refresh;
    }

    @Override
    public void onInitPresenters(IPresenterProvider provider) {
        mPt = provider.getPresenter(TestPresenter.class, this);
    }

    @Override
    public void onInitViews() {
        srLayout.setColorSchemeResources(R.color.colorAccent);
        srLayout.setOnRefreshListener(() -> {
            mPt.testFile();
        });
    }

    @Override
    public void onSetupPlugins(IPluginManager manager) {
        super.onSetupPlugins(manager);
        manager.load(new ButterKnifePlugin(this));
        manager.load(new MvpPlugin.AVer(this, this));
        manager.load(mDialogNotifierPlugin = new DialogNotifierPlugin(this, this));
    }

    @Override
    public void showMsg(String desc, String msg) {
        ToastUtils.show(desc + msg);
    }

    @Override
    public Object onGetButterKnifeSource() {
        return this;
    }

    @Nullable
    @Override
    public DialogNotifier.IDialog getNewDialog(String type, String notifierUid) {
        return new NotifySRLDialog(srLayout);
    }

    @Nullable
    @Override
    public DialogNotifier getDialogNotifier() {
        return mDialogNotifierPlugin.getDialogNotifier();
    }
}
