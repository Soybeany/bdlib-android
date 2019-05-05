package com.soybeany.bdlib.android.template.plugins.core;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;

import java.util.HashSet;
import java.util.Set;

/**
 * <br>Created by Soybeany on 2019/4/30.
 */
public class LifecyclePlugin implements IExtendPlugin {
    private Set<LifecycleObserver> mObservers = new HashSet<>();

    public LifecyclePlugin(@Nullable ICallback callback) {
        IExtendPlugin.invokeOnNotNull(callback, c -> c.onSetupObservers(mObservers));
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        Lifecycle lifecycle = owner.getLifecycle();
        for (LifecycleObserver observer : mObservers) {
            lifecycle.addObserver(observer);
        }
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        Lifecycle lifecycle = owner.getLifecycle();
        for (LifecycleObserver observer : mObservers) {
            lifecycle.removeObserver(observer);
        }
    }

    @NonNull
    @Override
    public final String getGroupId() {
        return "LifeCycle";
    }

    public interface ICallback {

        /**
         * 设置生命周期观察者
         */
        default void onSetupObservers(Set<LifecycleObserver> observers) {
        }
    }
}
