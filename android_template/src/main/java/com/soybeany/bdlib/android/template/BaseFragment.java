package com.soybeany.bdlib.android.template;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;
import com.soybeany.bdlib.android.template.plugins.core.FragmentDevelopPlugin;
import com.soybeany.bdlib.android.template.plugins.core.LifecyclePlugin;
import com.soybeany.bdlib.android.template.plugins.core.ViewModelPlugin;
import com.soybeany.bdlib.android.util.system.PermissionRequester;

import java.util.List;

/**
 * <br>Created by Soybeany on 2019/3/19.
 */
public abstract class BaseFragment extends Fragment implements PluginDriver.ICallback,
        FragmentDevelopPlugin.ICallback, FragmentDevelopPlugin.IInvoker,
        ViewModelPlugin.ICallback, ViewModelPlugin.IInvoker,
        LifecyclePlugin.ICallback {

    private FragmentDevelopPlugin mDevelopPlugin;

    {
        PluginDriver.install(this, this);
    }

    // //////////////////////////////////官方方法重写//////////////////////////////////

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDevelopPlugin = new FragmentDevelopPlugin(getActivity(), (activity, permissions, requestCode)
                -> requestPermissions(permissions, requestCode), this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDevelopPlugin.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return mDevelopPlugin.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (null != mDevelopPlugin) {
            mDevelopPlugin.setUserVisibleHint(isVisibleToUser);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mDevelopPlugin.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // //////////////////////////////////自定义方法重写//////////////////////////////////

    @Override
    public void onSetupPlugins(List<IExtendPlugin> plugins) {
        plugins.add(mDevelopPlugin);
        plugins.add(new LifecyclePlugin(this));
        plugins.add(new ViewModelPlugin(this, null));
    }

    @Override
    public void onSetContentView(int resId) {
        mDevelopPlugin.onSetContentView(resId);
    }

    @Override
    public View getContentView() {
        return mDevelopPlugin.getContentView();
    }

    @Override
    public boolean requestPermissions(@NonNull PermissionRequester.IPermissionCallback callback, @Nullable String... permissions) {
        return mDevelopPlugin.requestPermissions(callback, permissions);
    }

    @Override
    public void onEssentialPermissionsPass(boolean isNew) {
        mDevelopPlugin.onEssentialPermissionsPass(isNew);
    }

    // //////////////////////////////////拓展方法//////////////////////////////////

    @Nullable
    public <T extends ViewModel> T getActivityViewModel(Class<T> modelClass) {
        FragmentActivity activity = getActivity();
        return null != activity ? ViewModelProviders.of(activity).get(modelClass) : null;
    }
}
