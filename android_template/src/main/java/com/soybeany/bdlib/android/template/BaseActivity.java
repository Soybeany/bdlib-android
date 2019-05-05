package com.soybeany.bdlib.android.template;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.soybeany.bdlib.android.template.annotation.BackType;
import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;
import com.soybeany.bdlib.android.template.plugins.core.BackInterceptorPlugin;
import com.soybeany.bdlib.android.template.plugins.core.LifecyclePlugin;
import com.soybeany.bdlib.android.template.plugins.core.StdDevelopPlugin;
import com.soybeany.bdlib.android.template.plugins.core.ViewModelPlugin;
import com.soybeany.bdlib.android.util.system.PermissionRequester;

import java.util.Set;

/**
 * <br>Created by Soybeany on 2019/2/1.
 */
public abstract class BaseActivity extends AppCompatActivity implements PluginDriver.ICallback,
        StdDevelopPlugin.ITemplate, LifecyclePlugin.ITemplate,
        ViewModelPlugin.ITemplate, BackInterceptorPlugin.ITemplate {

    private StdDevelopPlugin mStdDevelopPlugin;
    private BackInterceptorPlugin mBackPlugin;

    private boolean mIsNew;

    {
        PluginDriver.install(this, this);
    }

    // //////////////////////////////////官方方法重写//////////////////////////////////

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsNew = (null == savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        mBackPlugin.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mStdDevelopPlugin.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // //////////////////////////////////自定义方法重写//////////////////////////////////

    @Override
    public void onPermitBack(@BackType int backType) {
        mBackPlugin.onPermitBack(backType, super::onBackPressed);
    }

    @Override
    public void onSetupPlugins(Set<IExtendPlugin> plugins) {
        plugins.add(mStdDevelopPlugin = new StdDevelopPlugin(new PermissionRequester(this,
                ActivityCompat::requestPermissions), mIsNew, this));
        plugins.add(new LifecyclePlugin(this));
        plugins.add(new ViewModelPlugin(this));
        plugins.add(mBackPlugin = new BackInterceptorPlugin(this, this));
    }

    @Override
    public void onSetContentView(int resId) {
        setContentView(resId);
    }

    @Override
    public boolean requestPermissions(@NonNull PermissionRequester.IPermissionCallback callback, @Nullable String... permissions) {
        return mStdDevelopPlugin.requestPermissions(callback, permissions);
    }
}
