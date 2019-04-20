package com.soybeany.bdlib.android.template;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModel;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.soybeany.bdlib.android.template.lifecycle.ButterKnifeObserver;
import com.soybeany.bdlib.android.util.IObserver;
import com.soybeany.bdlib.android.util.dialog.AbstractDialog;
import com.soybeany.bdlib.android.util.dialog.DialogKeyProvider;
import com.soybeany.bdlib.core.java8.Optional;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * 通用功能实现
 * <br>Created by Soybeany on 2019/4/16.
 */
class BaseFuncImpl implements IBaseFunc, IObserver {
    private final LinkedList<LifecycleObserver> mObservers = new LinkedList<>();
    private IBaseFunc.IEx mEx;
    private AbstractDialog mDialog;
    private DialogVM mDialogVM;

    BaseFuncImpl(IBaseFunc.IEx ex) {
        mEx = ex;
        addObserver(mEx.getLifecycle(), this);
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        mDialogVM = mEx.getViewModel(DialogVM.class);
        autoShowDialog();
        addObservers();
        onSignalDevCallbacks();
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        removeObservers();
    }

    @Override
    public void onSignalDevCallbacks() {
        mEx.onSignalDevCallbacks();
        mEx.onInitViewModels(mEx);
        mEx.onInitViews();
        mEx.signalDoBusiness();
    }

    @Override
    public AbstractDialog getDialog() {
        if (null == mDialog) {
            mDialog = mEx.onGetNewDialog();
            addObserver(mEx.getLifecycle(), mDialog);
            mDialogVM.hasDialog = true;
        }
        return mDialog;
    }

    @Override
    public DialogKeyProvider getDialogKeys() {
        Looper mainLooper;
        if (null == mDialog && Thread.currentThread() != (mainLooper = Looper.getMainLooper()).getThread()) {
            activeDialogInUIThread(mainLooper);
        }
        return getDialog().getKeyProvider();
    }

    /**
     * 自动显示上一次未关闭的弹窗
     */
    private void autoShowDialog() {
        if (mDialogVM.hasDialog) {
            getDialog(); // 触发弹窗的创建
        }
    }

    private void activeDialogInUIThread(Looper mainLooper) {
        try {
            new Handler(mainLooper).post(() -> {
                getDialog();
                mDialogVM.notify();
            });
            mDialogVM.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void addObservers() {
        Lifecycle lifecycle = mEx.getLifecycle();
        Optional.ofNullable(mEx.setupObservers()).ifPresent(observers -> {
            for (LifecycleObserver observer : observers) {
                addObserver(lifecycle, observer);
            }
        }); // 添加开发者自定义的观察者
        addObserver(lifecycle, new ButterKnifeObserver(mEx)); // 添加ButterKnife观察者
    }

    private void removeObservers() {
        Iterator<LifecycleObserver> iterator = mObservers.iterator();
        Lifecycle lifecycle = mEx.getLifecycle();
        while (iterator.hasNext()) {
            lifecycle.removeObserver(iterator.next());
            iterator.remove();
        }
    }

    private void addObserver(Lifecycle lifecycle, LifecycleObserver observer) {
        mObservers.add(observer);
        lifecycle.addObserver(observer);
    }

    public static class DialogVM extends ViewModel {
        boolean hasDialog; // 标识此前是否持有过弹窗
    }
}
