package com.soybeany.bdlib.android.template.plugins.core;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;

/**
 * ViewModel
 * <br>Created by Soybeany on 2019/4/30.
 */
public class ViewModelPlugin implements IExtendPlugin {
    public static final String GROUP_ID = "ViewModel";

    @Nullable
    private final ICallback mCallback;
    private final ViewModelProvider mProvider;

    public ViewModelPlugin(@NonNull FragmentActivity activity, @Nullable ICallback callback) {
        mProvider = ViewModelProviders.of(activity);
        mCallback = callback;
    }

    public ViewModelPlugin(@NonNull Fragment fragment, @Nullable ICallback callback) {
        mProvider = ViewModelProviders.of(fragment);
        mCallback = callback;
    }

    @Override
    public void initBeforeSetContentView() {
        IExtendPlugin.invokeOnNotNull(mCallback, callback -> callback.onInitViewModels(mProvider));
    }

    @NonNull
    @Override
    public final String getGroupId() {
        return GROUP_ID;
    }

    public interface ICallback {
        /**
         * 初始化ViewModel
         */
        default void onInitViewModels(ViewModelProvider provider) {
        }
    }
}
