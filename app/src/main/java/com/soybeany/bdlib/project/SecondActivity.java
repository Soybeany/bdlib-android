package com.soybeany.bdlib.project;

import android.view.View;

import com.soybeany.bdlib.android.template.BaseActivity;
import com.soybeany.bdlib.android.util.LogUtils;
import com.soybeany.bdlib.android.util.ToastUtils;

/**
 * <br>Created by Soybeany on 2019/4/15.
 */
public class SecondActivity extends BaseActivity implements ITestView {
    private TestPresenter pt;

    @Override
    public int setupLayoutResId() {
        return R.layout.activity_second;
    }

    @Override
    public void onInit() {
        pt = getViewModel(TestPresenter.class);
        pt.autoBind(getLifecycle(), this);
    }

    @Override
    public void showMsg(String desc, String msg) {
        LogUtils.test(desc + "有效果");
        ToastUtils.show(desc + msg);
    }

    public void onClick(View view) {
        pt.testFile(getDialogKeys());
    }
}
