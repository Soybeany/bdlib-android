package com.soybeany.bdlib.project;

import android.view.View;

import com.soybeany.bdlib.android.mvp.IPresenterProvider;
import com.soybeany.bdlib.android.mvp.MvpPlugin;
import com.soybeany.bdlib.android.template.BaseActivity;
import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;
import com.soybeany.bdlib.android.util.LogUtils;
import com.soybeany.bdlib.android.util.ToastUtils;

/**
 * <br>Created by Soybeany on 2019/4/15.
 */
public class SecondActivity extends BaseActivity implements ITestView, MvpPlugin.ITemplate {
    private TestPresenter mPt;

    @Override
    public int setupLayoutResId() {
        return R.layout.activity_second;
    }

    @Override
    public void onInitPresenters(IPresenterProvider provider) {
        mPt = provider.getPresenter(TestPresenter.class, this);
    }

    @Override
    public void showMsg(String desc, String msg) {
        LogUtils.test(desc + "有效果");
        ToastUtils.show(desc + msg);
    }

    public void onClick(View view) {
        mPt.testFile(getDialogKeys());
    }

    @Override
    public IExtendPlugin[] onGetNewPlugins() {
        return new IExtendPlugin[]{new MvpPlugin<>(this)};
    }
}
