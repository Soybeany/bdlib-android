package com.soybeany.bdlib.android.template;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.soybeany.bdlib.android.util.dialog.AbstractDialog;
import com.soybeany.bdlib.android.util.dialog.DialogKeyProvider;
import com.soybeany.bdlib.android.util.dialog.ProgressDialogImpl;

/**
 * <br>Created by Soybeany on 2019/2/1.
 */
public abstract class BaseActivity extends AppCompatActivity implements IBaseFunc.IEx<Activity> {
    private final IBaseFunc mFuncImpl = new BaseFuncImpl(this);
    private boolean mIsNew;

    // //////////////////////////////////方法重写//////////////////////////////////

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsNew = (null == savedInstanceState);
        beforeSetupContentView();
        setContentView(setupLayoutResId());
    }

    @Override
    public Activity onGetButterKnifeSource() {
        return this;
    }

    @Override
    public AbstractDialog onGetNewDialog() {
        return new ProgressDialogImpl(this);
    }

    @Override
    public AbstractDialog getDialog() {
        return mFuncImpl.getDialog();
    }

    @Override
    public DialogKeyProvider getDialogKeys() {
        return mFuncImpl.getDialogKeys();
    }

    @Override
    public <T extends ViewModel> T getViewModel(Class<T> modelClass) {
        return ViewModelProviders.of(this).get(modelClass);
    }

    @Override
    public void signalDoBusiness() {
        getWindow().getDecorView().post(() -> doBusiness(mIsNew));
    }

    // //////////////////////////////////内部类//////////////////////////////////
}
