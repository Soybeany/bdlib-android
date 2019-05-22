package com.soybeany.bdlib.android.mvp;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.soybeany.bdlib.android.util.BDApplication;
import com.soybeany.bdlib.android.util.IObserver;
import com.soybeany.bdlib.android.util.LogUtils;
import com.soybeany.bdlib.android.util.dialog.DialogNotifier;
import com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg;
import com.soybeany.bdlib.android.util.dialog.msg.IDialogMsg;
import com.soybeany.bdlib.core.java8.function.Consumer;
import com.soybeany.bdlib.core.util.storage.KeySetStorage;

import static com.soybeany.bdlib.android.util.BDContext.MAIN_HANDLER;

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
     *
     * @return 是否有调用执行
     */
    protected boolean invoke(@NonNull Consumer<V> consumer) {
        if (mStorage.isEmpty()) {
            return false;
        }
        mStorage.invokeAllVal(consumer);
        return true;
    }

    /**
     * 执行全部View的方法(允许在工作线程中调用)
     */
    protected void uiInvoke(@NonNull Consumer<V> consumer) {
        MAIN_HANDLER.post(() -> invoke(consumer));
    }

    @Nullable
    protected DialogNotifier getTopDialogNotifier() {
        return getTopDialogNotifier(DialogNotifier.IMultiTypeProvider.TYPE_DEFAULT);
    }

    /**
     * 获得目前位于任务栈最顶端的弹窗提供者
     */
    @Nullable
    @SuppressWarnings("SameParameterValue")
    protected DialogNotifier getTopDialogNotifier(String type) {
        Activity activity = BDApplication.getTopActivity();
        if (activity instanceof DialogNotifier.IMultiTypeProvider) {
            return ((DialogNotifier.IMultiTypeProvider) activity).getDialogNotifier(type);
        } else if (activity instanceof DialogNotifier.IProvider) {
            return ((DialogNotifier.IProvider) activity).getDialogNotifier();
        }
        return null;
    }

    /**
     * 为工作线程的任务包装上弹窗(不可取消)
     */
    protected void wrapDialog(@Nullable DialogNotifier notifier, @Nullable IDialogMsg msg, @WorkerThread Runnable runnable) {
        if (null == notifier || null == msg) {
            LogUtils.w("包装弹窗", "notifier或msg为null，无法正常弹窗");
            runnable.run();
            return;
        }
        // 正常执行
        DialogInvokerMsg data = new DialogInvokerMsg().type(DialogInvokerMsg.TYPE_SHOW_MSG).data(msg.cancelable(false));
        notifier.invoker().notifyNow(data);
        runnable.run();
        notifier.invoker().notifyNow(data.type(DialogInvokerMsg.TYPE_POP_MSG));
    }
}
