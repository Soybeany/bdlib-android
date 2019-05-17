package com.soybeany.bdlib.project;

import android.support.v4.widget.SwipeRefreshLayout;

import com.soybeany.bdlib.android.mvp.IPresenterProvider;
import com.soybeany.bdlib.android.mvp.MvpPlugin;
import com.soybeany.bdlib.android.template.BaseActivity;
import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;
import com.soybeany.bdlib.android.template.plugins.extend.ButterKnifePlugin;
import com.soybeany.bdlib.android.ui.layout.NotifySRLHelper;
import com.soybeany.bdlib.android.util.ToastUtils;
import com.soybeany.bdlib.android.util.dialog.DialogNotifier;

import java.util.List;

import butterknife.BindView;

/**
 * <br>Created by Soybeany on 2019/5/17.
 */
public class SwipeRefreshActivity extends BaseActivity implements DialogNotifier.IProvider,
        ButterKnifePlugin.ICallback, ITestView, MvpPlugin.ITemplate {

    @BindView(R.id.srl)
    SwipeRefreshLayout srLayout;

    private TestPresenter mPt;
    private NotifySRLHelper mNotifySRLHelper;

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
        mNotifySRLHelper = new NotifySRLHelper(this, "swipe", srLayout);
        srLayout.setColorSchemeResources(R.color.colorAccent);
        srLayout.setOnRefreshListener(() -> {
            mPt.testFile();
        });
    }

    @Override
    public void onSetupPlugins(List<IExtendPlugin> plugins) {
        super.onSetupPlugins(plugins);
        plugins.add(new ButterKnifePlugin(this));
        plugins.add(new MvpPlugin(this, this, this));
    }

    @Override
    public void showMsg(String desc, String msg) {
        ToastUtils.show(desc + msg);
    }

    @Override
    public Object onGetButterKnifeSource() {
        return this;
    }

    @Override
    public DialogNotifier getDialogNotifier(String type) {
        return mNotifySRLHelper.getDialogNotifier();
    }
}
