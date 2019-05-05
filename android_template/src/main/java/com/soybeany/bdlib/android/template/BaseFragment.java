package com.soybeany.bdlib.android.template;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;
import com.soybeany.bdlib.android.template.plugins.core.LifecyclePlugin;
import com.soybeany.bdlib.android.template.plugins.core.StdDevelopPlugin;
import com.soybeany.bdlib.android.template.plugins.core.ViewModelPlugin;
import com.soybeany.bdlib.android.util.system.PermissionRequester;

import java.util.Set;

/**
 * <br>Created by Soybeany on 2019/3/19.
 */
public abstract class BaseFragment extends Fragment implements PluginDriver.ICallback,
        StdDevelopPlugin.ITemplate, LifecyclePlugin.ITemplate, ViewModelPlugin.ITemplate {

    private StdDevelopPlugin mStdDevelopPlugin;

    private View mContentV;
    private int mPreparedCount; // 已准备好的位置的计数
    private boolean mIsNew;

    {
        PluginDriver.install(this, this);
    }

    // //////////////////////////////////官方方法重写//////////////////////////////////

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsNew = (null == savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return mContentV;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            tryToSignalDoBusiness();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mStdDevelopPlugin.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // //////////////////////////////////自定义方法重写//////////////////////////////////

    @Override
    public void onSetupPlugins(Set<IExtendPlugin> plugins) {
        plugins.add(mStdDevelopPlugin = new StdDevelopPlugin(new PermissionRequester(getActivity(),
                (activity, permissions, requestCode) -> requestPermissions(permissions, requestCode)), mIsNew, this));
        plugins.add(new LifecyclePlugin(this));
        plugins.add(new ViewModelPlugin(this));
    }

    @Override
    public void onSetContentView(int resId) {
        mContentV = getLayoutInflater().inflate(setupLayoutResId(), null, false);
    }

    @Override
    public boolean requestPermissions(@NonNull PermissionRequester.IPermissionCallback callback, @Nullable String... permissions) {
        return mStdDevelopPlugin.requestPermissions(callback, permissions);
    }

    @Override
    public void onEssentialPermissionsPass(boolean isNew) {
        tryToSignalDoBusiness();
    }

    // //////////////////////////////////拓展方法//////////////////////////////////

    @Nullable
    public <T extends ViewModel> T getActivityViewModel(Class<T> modelClass) {
        FragmentActivity activity = getActivity();
        return null != activity ? ViewModelProviders.of(activity).get(modelClass) : null;
    }

    // //////////////////////////////////内部方法//////////////////////////////////

    private void tryToSignalDoBusiness() {
        if (mPreparedCount > 1) {
            return;
        } else if (mPreparedCount == 1) {
            IExtendPlugin.invokeInUiThread(() -> doBusiness(mIsNew));
        }
        mPreparedCount++;
    }
}
