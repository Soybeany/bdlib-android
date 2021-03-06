package com.soybeany.bdlib.android.template.plugins.core;

import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;

import java.util.HashSet;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

/**
 * <br>Created by Soybeany on 2019/4/30.
 */
public class LifecyclePlugin implements IExtendPlugin {
    public static final String GROUP_ID = "LifeCycle";

    private final Set<LifecycleObserver> mObservers = new HashSet<>();

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
        return GROUP_ID;
    }

    public interface ICallback {

        /**
         * 设置生命周期观察者
         */
        default void onSetupObservers(Set<LifecycleObserver> observers) {
        }
    }
}
