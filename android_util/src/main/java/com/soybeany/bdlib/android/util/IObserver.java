package com.soybeany.bdlib.android.util;


import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * <br>Created by Soybeany on 2019/3/16.
 */
public interface IObserver extends LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    default void onCreate(@NonNull LifecycleOwner owner) {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    default void onStart(@NonNull LifecycleOwner owner) {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    default void onResume(@NonNull LifecycleOwner owner) {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    default void onPause(@NonNull LifecycleOwner owner) {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    default void onStop(@NonNull LifecycleOwner owner) {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    default void onDestroy(@NonNull LifecycleOwner owner) {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    default void onLifecycleChanged(@NonNull LifecycleOwner owner, @NonNull Lifecycle.Event event) {
    }
}
