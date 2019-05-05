package com.soybeany.bdlib.android.mvp;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.soybeany.bdlib.android.template.plugins.extend.DialogPlugin;
import com.soybeany.bdlib.android.util.BDApplication;
import com.soybeany.bdlib.android.util.IObserver;
import com.soybeany.bdlib.android.util.dialog.AbstractDialog;
import com.soybeany.bdlib.android.util.dialog.DialogKeyProvider;
import com.soybeany.bdlib.android.util.dialog.DialogMsg;
import com.soybeany.bdlib.core.java8.function.Consumer;
import com.soybeany.bdlib.core.util.storage.KeySetStorage;

/**
 * <br>Created by Soybeany on 2019/4/16.
 */
public abstract class BasePresenter<V extends IPresenterView> extends ViewModel implements IObserver {
    private final KeySetStorage<Lifecycle, V> mStorage = new KeySetStorage<>();

    /**
     * 绑定View，绑定后自动解绑
     */
    public void bindView(@NonNull Lifecycle lifecycle, @Nullable V view) {
        if (null == view) {
            return;
        }
        if (!mStorage.containKey(lifecycle)) {
            lifecycle.addObserver(this);
        }
        mStorage.putVal(lifecycle, view);
    }

    /**
     * 此方法按需调用，不必强制调用，({@link Lifecycle}生命周期结束时会自动解绑)
     */
    public void unbindView(@NonNull Lifecycle lifecycle, @Nullable V view) {
        if (null == view) {
            return;
        }
        mStorage.removeVal(lifecycle, view);
        if (!mStorage.containKey(lifecycle)) {
            lifecycle.removeObserver(this);
        }
    }

    /**
     * 此方法按需调用，不必强制调用，({@link Lifecycle}生命周期结束时会自动解绑)
     */
    public void unbindViews(@NonNull Lifecycle lifecycle) {
        mStorage.remove(lifecycle);
        lifecycle.removeObserver(this);
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        unbindViews(owner.getLifecycle());
    }

    /**
     * 执行全部View的方法
     */
    protected void invoke(@NonNull Consumer<V> consumer) {
        mStorage.invokeAllVal(consumer);
    }

    /**
     * 获得目前位于任务栈最顶端的弹窗提供者
     */
    protected DialogKeyProvider getTopProvider() {
        Activity activity = BDApplication.getTopActivity();
        return activity instanceof DialogPlugin.ITemplate ? ((DialogPlugin.ITemplate) activity).getDialogKeys() : null;
    }

    /**
     * 为工作线程的任务包装上弹窗
     */
    protected void wrapDialog(@Nullable DialogKeyProvider provider, @Nullable DialogMsg msg, @WorkerThread Runnable runnable) {
        AbstractDialog.wrap(provider, msg, runnable);
    }
}
