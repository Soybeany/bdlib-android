package com.soybeany.bdlib.android.template.plugins.core;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;

import java.util.Objects;

/**
 * ViewModel
 * <br>Created by Soybeany on 2019/4/30.
 */
public class ViewModelPlugin implements IExtendPlugin {
    public static final String GROUP_ID = "ViewModel";

    @Nullable
    private final ICallback mCallback;
    private final ViewModelProvider mProvider;

    @Nullable
    private Fragment mFragment;

    public ViewModelPlugin(@NonNull FragmentActivity activity, @Nullable ICallback callback) {
        mProvider = ViewModelProviders.of(activity);
        mCallback = callback;
    }

    public ViewModelPlugin(@NonNull Fragment fragment, @Nullable IFragmentCallback callback) {
        mProvider = ViewModelProviders.of(mFragment = fragment);
        mCallback = callback;
    }

    @Override
    public void initBeforeSetContentView() {
        IExtendPlugin.invokeOnNotNull(mCallback, callback -> callback.onInitViewModels(mProvider));
        // Fragment下额外回调
        if (null != mFragment && mCallback instanceof IFragmentCallback) {
            FragmentActivity activity = Objects.requireNonNull(mFragment.getActivity());
            ((IFragmentCallback) mCallback).onInitActivityViewModels(ViewModelProviders.of(activity));
        }
    }

    @NonNull
    @Override
    public final String getGroupId() {
        return GROUP_ID;
    }

    /**
     * 默认范围
     */
    public interface ICallback {
        default void onInitViewModels(ViewModelProvider provider) {
        }
    }

    /**
     * Fragment中的Activity范围
     */
    public interface IFragmentCallback extends ICallback {
        default void onInitActivityViewModels(ViewModelProvider provider) {
        }
    }
}
