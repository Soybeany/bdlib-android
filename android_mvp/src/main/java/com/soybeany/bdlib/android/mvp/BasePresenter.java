package com.soybeany.bdlib.android.mvp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.soybeany.bdlib.android.util.IObserver;
import com.soybeany.bdlib.core.java8.function.Consumer;
import com.soybeany.bdlib.core.util.storage.KeySetStorage;

/**
 * <br>Created by Soybeany on 2019/4/16.
 */
public abstract class BasePresenter<V extends IView> extends ViewModel implements IObserver {
    private final KeySetStorage<Lifecycle, V> mStorage = new KeySetStorage<>();

    /**
     * 绑定View，绑定后自动解绑
     */
    public void autoBind(Lifecycle lifecycle, V view) {
        if (!mStorage.containKey(lifecycle)) {
            lifecycle.addObserver(this);
        }
        mStorage.putVal(lifecycle, view);
    }

    /**
     * 此方法按需调用，不必强制调用({@link Lifecycle}生命周期结束时会自动解绑)
     */
    public void unbind(Lifecycle lifecycle, V view) {
        mStorage.removeVal(lifecycle, view);
        if (!mStorage.containKey(lifecycle)) {
            lifecycle.removeObserver(this);
        }
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        Lifecycle lifecycle = owner.getLifecycle();
        mStorage.remove(lifecycle);
        lifecycle.removeObserver(this);
    }

    /**
     * 执行全部View的方法
     */
    protected void invoke(Consumer<V> consumer) {
        mStorage.invokeAllVal(consumer);
    }
}
