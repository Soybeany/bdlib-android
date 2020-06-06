package com.soybeany.bdlib.project;

import com.soybeany.bdlib.android.mvp.IPresenterProvider;
import com.soybeany.bdlib.android.mvp.MvpPlugin;
import com.soybeany.bdlib.android.template.BaseActivity;
import com.soybeany.bdlib.android.template.interfaces.IPluginManager;
import com.soybeany.bdlib.android.template.plugins.extend.ButterKnifePlugin;
import com.soybeany.bdlib.android.template.plugins.extend.DialogNotifierPlugin;
import com.soybeany.bdlib.android.ui.layout.SwipeRefreshLayoutDialog;
import com.soybeany.bdlib.android.util.ToastUtils;
import com.soybeany.bdlib.android.util.dialog.IRealDialog;
import com.soybeany.bdlib.android.web.dialog.INotifierProvider;
import com.soybeany.bdlib.android.web.notifier.DialogNotifier;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

/**
 * <br>Created by Soybeany on 2019/5/17.
 */
public class SwipeRefreshActivity extends BaseActivity implements INotifierProvider, DialogNotifierPlugin.ICallback,
        ButterKnifePlugin.ICallback, ITestView, MvpPlugin.ICallback {

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
        mPt = provider.get(TestPresenter.class, this);
    }

    @Override
    public void onInitViews() {
        srLayout.setColorSchemeResources(R.color.colorAccent);
        srLayout.setOnRefreshListener(() -> {
//            mPt.testAsync();
            mPt.testFile();
        });
    }

    @Override
    public void onSetupPlugins(IPluginManager manager) {
        super.onSetupPlugins(manager);
        manager.load(new ButterKnifePlugin(this));
        manager.load(new MvpPlugin(this, this));
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

    @Override
    public IRealDialog onGetNewDialog(String type) {
        return new SwipeRefreshLayoutDialog(srLayout);
    }

    @Override
    public DialogNotifier getDialogNotifier(String type) {
        return mDialogNotifierPlugin.getDialogNotifier(type);
    }
}
