package com.soybeany.bdlib.android.template;

import android.os.Bundle;

import com.soybeany.bdlib.android.template.annotation.BackType;
import com.soybeany.bdlib.android.template.interfaces.IPluginManager;
import com.soybeany.bdlib.android.template.plugins.core.BackInterceptorPlugin;
import com.soybeany.bdlib.android.template.plugins.core.LifecyclePlugin;
import com.soybeany.bdlib.android.template.plugins.core.StdDevelopPlugin;
import com.soybeany.bdlib.android.template.plugins.core.ViewModelPlugin;
import com.soybeany.bdlib.android.util.system.PermissionRequester;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

/**
 * <br>Created by Soybeany on 2019/2/1.
 */
public abstract class BaseActivity extends AppCompatActivity implements PluginDriver.ICallback,
        StdDevelopPlugin.ICallback, StdDevelopPlugin.IInvoker,
        ViewModelPlugin.ICallback,
        BackInterceptorPlugin.ICallback, BackInterceptorPlugin.IInvoker,
        LifecyclePlugin.ICallback {

    private final PluginDriver mDriver = new PluginDriver(this, this);
    private StdDevelopPlugin mDevelopPlugin;
    private BackInterceptorPlugin mBackPlugin;

    // //////////////////////////////////官方方法重写//////////////////////////////////

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mDriver.beforeOnCreate();
        super.onCreate(savedInstanceState);
        mDevelopPlugin.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        mBackPlugin.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mDevelopPlugin.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // //////////////////////////////////自定义方法重写//////////////////////////////////

    @Override
    public void wannaBack(int backType) {
        mBackPlugin.wannaBack(backType);
    }

    @Override
    public void onPermitBack(@BackType int backType) {
        mBackPlugin.onPermitBack(backType, super::onBackPressed);
    }

    @Override
    public void onSetupPlugins(IPluginManager manager) {
        manager.load(mDevelopPlugin = new StdDevelopPlugin(this, this).activate(this, ActivityCompat::requestPermissions));
        manager.load(new LifecyclePlugin(this));
        manager.load(new ViewModelPlugin(this, this));
        manager.load(mBackPlugin = new BackInterceptorPlugin(this, this));
    }

    @Override
    public void onSetContentView(int resId) {
        setContentView(resId);
    }

    @Override
    public boolean requestPermissions(@NonNull PermissionRequester.IPermissionCallback callback, @Nullable String... permissions) {
        return mDevelopPlugin.requestPermissions(callback, permissions);
    }

}
