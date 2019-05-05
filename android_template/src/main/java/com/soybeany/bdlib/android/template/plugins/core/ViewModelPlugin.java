package com.soybeany.bdlib.android.template.plugins.core;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;
import com.soybeany.bdlib.android.template.interfaces.IVMProvider;
import com.soybeany.bdlib.core.java8.Optional;

/**
 * ViewModel
 * <br>Created by Soybeany on 2019/4/30.
 */
public class ViewModelPlugin implements IExtendPlugin {
    @Nullable
    private ICallback mCallback;
    private IVMProvider mProvider;

    public ViewModelPlugin(@Nullable ICallback callback, @Nullable IVMProvider provider) {
        mCallback = callback;
        mProvider = Optional.ofNullable(provider).orElseGet(() -> new IInvoker() {
        });
    }

    @Override
    public void initBeforeSetContentView() {
        IExtendPlugin.invokeOnNotNull(mCallback, callback -> callback.onInitViewModels(mProvider));
    }

    @NonNull
    @Override
    public final String getGroupId() {
        return "ViewModel";
    }

    public interface IInvoker extends IVMProvider {
        @Override
        default <T extends ViewModel> T getViewModel(Class<T> modelClass) {
            if (this instanceof FragmentActivity) {
                return ViewModelProviders.of((FragmentActivity) this).get(modelClass);
            } else if (this instanceof Fragment) {
                return ViewModelProviders.of((Fragment) this).get(modelClass);
            }
            throw new RuntimeException("请在FragmentActivity或Fragment中使用");
        }
    }

    public interface ICallback {
        /**
         * 初始化ViewModel
         */
        default void onInitViewModels(IVMProvider provider) {
        }
    }
}
