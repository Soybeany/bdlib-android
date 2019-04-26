package com.soybeany.bdlib.android.template;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModel;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.template.lifecycle.ButterKnifeObserver;
import com.soybeany.bdlib.android.util.IObserver;
import com.soybeany.bdlib.android.util.dialog.AbstractDialog;
import com.soybeany.bdlib.android.util.dialog.DialogKeyProvider;
import com.soybeany.bdlib.core.java8.Optional;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.locks.LockSupport;

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
        signalAfterSetContentView();
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        removeObservers();
    }

    @Override
    public void signalAfterSetContentView() {
        mEx.signalAfterSetContentView();
        mEx.onInitViewModels(mEx);
        mEx.onInitViews();
        mEx.signalOnPostReady();
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
        if (null != mDialog) {
            return mDialog.getKeyProvider();
        }

        Thread curThread = Thread.currentThread();
        Looper mainLooper = Looper.getMainLooper();

        if (curThread != mainLooper.getThread()) {
            new Handler(mainLooper).post(() -> {
                getDialog();
                LockSupport.unpark(curThread);
            });
            LockSupport.park();
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

    private void addObservers() {
        Lifecycle lifecycle = mEx.getLifecycle();
        // 添加开发者自定义的观察者
        addObservers(lifecycle, mEx.setupObservers());
        // 添加应用自定义的观察者
        addObservers(lifecycle, mEx.signalExObservers());
        // 添加ButterKnife观察者
        addObserver(lifecycle, new ButterKnifeObserver(mEx));
    }

    private void removeObservers() {
        Iterator<LifecycleObserver> iterator = mObservers.iterator();
        Lifecycle lifecycle = mEx.getLifecycle();
        while (iterator.hasNext()) {
            lifecycle.removeObserver(iterator.next());
            iterator.remove();
        }
    }

    private void addObservers(Lifecycle lifecycle, @Nullable LifecycleObserver[] observers) {
        Optional.ofNullable(observers).ifPresent(list -> {
            for (LifecycleObserver observer : list) {
                addObserver(lifecycle, observer);
            }
        });
    }

    private void addObserver(Lifecycle lifecycle, LifecycleObserver observer) {
        mObservers.add(observer);
        lifecycle.addObserver(observer);
    }

    public static class DialogVM extends ViewModel {
        boolean hasDialog; // 标识此前是否持有过弹窗
    }
}
