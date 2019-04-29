package com.soybeany.bdlib.android.template;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.soybeany.bdlib.android.template.annotation.BackType;
import com.soybeany.bdlib.android.template.interfaces.IBackIntercepter;
import com.soybeany.bdlib.android.template.interfaces.IBaseFunc;
import com.soybeany.bdlib.android.util.dialog.AbstractDialog;
import com.soybeany.bdlib.android.util.dialog.DialogKeyProvider;
import com.soybeany.bdlib.android.util.dialog.ProgressDialogImpl;
import com.soybeany.bdlib.android.util.system.KeyboardUtils;
import com.soybeany.bdlib.android.util.system.PermissionRequester;

/**
 * <br>Created by Soybeany on 2019/2/1.
 */
public abstract class BaseActivity extends AppCompatActivity implements IBaseFunc.IEx<Activity>, IBackIntercepter {
    private final IStarter mStarter = new StarterImpl(this);
    private boolean mIsNew;

    // //////////////////////////////////官方方法重写//////////////////////////////////

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsNew = (null == savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            KeyboardUtils.closeKeyboard(this);
            wannaBack(BackType.BACK_ITEM);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        wannaBack(BackType.BACK_KEY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mStarter.checkPermissionResult(requestCode, permissions, grantResults);
    }

    // //////////////////////////////////自定义方法重写//////////////////////////////////

    @Override
    public void onPermitBack(@BackType int backType) {
        switch (backType) {
            case BackType.BACK_KEY:
                super.onBackPressed();
                break;
            case BackType.BACK_ITEM:
            case BackType.BACK_OTHER:
                finish();
                break;
        }
    }

    @Override
    public void onSetContentView() {
        setContentView(setupLayoutResId());
    }

    @Override
    public Activity onGetButterKnifeSource() {
        return this;
    }

    @Override
    public Lifecycle onGetLifecycle() {
        return getLifecycle();
    }

    @Override
    public AbstractDialog onGetNewDialog() {
        return new ProgressDialogImpl(this);
    }

    @Override
    public AbstractDialog getDialog() {
        return mStarter.getDialog();
    }

    @Override
    public DialogKeyProvider getDialogKeys() {
        return mStarter.getDialogKeys();
    }

    @Override
    public <T extends ViewModel> T getViewModel(Class<T> modelClass) {
        return ViewModelProviders.of(this).get(modelClass);
    }

    @Override
    public boolean requestPermissions(@NonNull PermissionRequester.IPermissionCallback callback, @Nullable String... permissions) {
        return mStarter.requestPermissions(callback, permissions);
    }

    @Override
    public PermissionRequester onGetNewPermissionRequester() {
        return new PermissionRequester(this, ActivityCompat::requestPermissions);
    }

    @Override
    public PermissionRequester.IPermissionCallback onGetEPermissionCallback() {
        return new PermissionRequester.IPermissionCallback() {
            @Override
            public void onPermissionPass() {
                getWindow().getDecorView().post(() -> doBusiness(mIsNew));
            }

            @Override
            public void onPermissionDeny() {
                finish();
            }
        };
    }
}
