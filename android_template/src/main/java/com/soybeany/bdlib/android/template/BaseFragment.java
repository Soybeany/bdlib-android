package com.soybeany.bdlib.android.template;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.soybeany.bdlib.android.template.interfaces.IPluginManager;
import com.soybeany.bdlib.android.template.plugins.core.FragmentDevelopPlugin;
import com.soybeany.bdlib.android.template.plugins.core.LifecyclePlugin;
import com.soybeany.bdlib.android.template.plugins.core.ViewModelPlugin;
import com.soybeany.bdlib.android.util.system.PermissionRequester;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * <br>Created by Soybeany on 2019/3/19.
 */
public abstract class BaseFragment extends Fragment implements PluginDriver.ICallback,
        FragmentDevelopPlugin.ICallback, FragmentDevelopPlugin.IInvoker,
        ViewModelPlugin.IFragmentCallback,
        LifecyclePlugin.ICallback {

    private final PluginDriver mDriver = new PluginDriver(this, this);
    private final FragmentDevelopPlugin mDevelopPlugin = new FragmentDevelopPlugin(this, this);

    // //////////////////////////////////官方方法重写//////////////////////////////////

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mDevelopPlugin.activate(getActivity(), (activity, permissions, requestCode) -> requestPermissions(permissions, requestCode));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDevelopPlugin.deactivate();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mDriver.beforeOnCreate();
        super.onCreate(savedInstanceState);
        mDevelopPlugin.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mDevelopPlugin.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return mDevelopPlugin.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mDevelopPlugin.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mDevelopPlugin.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // //////////////////////////////////自定义方法重写//////////////////////////////////

    @Override
    public void onSetupPlugins(IPluginManager manager) {
        manager.load(mDevelopPlugin);
        manager.load(new LifecyclePlugin(this));
        manager.load(new ViewModelPlugin(this, this));
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
}
