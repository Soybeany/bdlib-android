package com.soybeany.bdlib.android.template;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModel;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.template.interfaces.IBaseFunc;
import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;
import com.soybeany.bdlib.android.template.lifecycle.ButterKnifeObserver;
import com.soybeany.bdlib.android.util.IObserver;
import com.soybeany.bdlib.android.util.dialog.AbstractDialog;
import com.soybeany.bdlib.android.util.dialog.DialogKeyProvider;
import com.soybeany.bdlib.android.util.system.PermissionRequester;
import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.util.IterableUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

/**
 * 启动器实现
 * <br>Created by Soybeany on 2019/4/16.
 */
class StarterImpl implements IBaseFunc.IStarter, IObserver {
    private final LinkedList<LifecycleObserver> mObservers = new LinkedList<>();
    private IBaseFunc.IEx mEx;
    private Lifecycle mLC;
    private AbstractDialog mDialog;
    private DialogVM mDialogVM;
    private PermissionRequester mPR;
    private final List<IExtendPlugin> mPlugins = new LinkedList<>();

    StarterImpl(IBaseFunc.IEx ex) {
        mEx = ex;
        mLC = mEx.onGetLifecycle();
        addObserver(this);
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        // 弹窗
        autoShowDialog();
        // 观察者
        addObservers();
        // 插件
        addPlugins();
        IterableUtils.forEach(mPlugins, (plugin, flag) -> plugin.signalBeforeSetContentView());
        mEx.onSetContentView();
        IterableUtils.forEach(mPlugins, (plugin, flag) -> plugin.signalAfterSetContentView());
        new Handler().post(() -> IterableUtils.forEach(mPlugins, (plugin, flag) -> plugin.signalOnInitFinished()));
        // 开发者
        mEx.onInitViewModels(mEx);
        mEx.onInitViews();
        // 权限
        mPR = mEx.onGetNewPermissionRequester().withEPermission(mEx.onGetEPermissionCallback(), mEx.onGetEssentialPermissions());
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        removeObservers();
    }

    @Override
    public AbstractDialog getDialog() {
        if (null == mDialog) {
            mDialog = mEx.onGetNewDialog();
            addObserver(mDialog);
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

    @Override
    public boolean requestPermissions(@NonNull PermissionRequester.IPermissionCallback callback, @Nullable String... permissions) {
        return mPR.requestPermissions(callback, permissions);
    }

    @Override
    public void checkPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPR.checkResults(requestCode, permissions, grantResults);
    }

    /**
     * 自动显示上一次未关闭的弹窗
     */
    private void autoShowDialog() {
        mDialogVM = mEx.getViewModel(DialogVM.class);
        if (mDialogVM.hasDialog) {
            getDialog(); // 触发弹窗的创建
        }
    }

    private void addPlugins() {
        mPlugins.add(mEx);
        Optional.ofNullable(mEx.onGetNewPlugins()).ifPresent(plugins -> mPlugins.addAll(Arrays.asList(plugins)));
    }

    private void addObservers() {
        // 添加开发者自定义的观察者
        addObservers(mEx.setupObservers());
        // 添加应用自定义的观察者
        addObservers(mEx.onGetExObservers());
        // 添加ButterKnife观察者
        addObserver(new ButterKnifeObserver(mEx));
    }

    private void removeObservers() {
        Iterator<LifecycleObserver> iterator = mObservers.iterator();
        while (iterator.hasNext()) {
            mLC.removeObserver(iterator.next());
            iterator.remove();
        }
    }

    private void addObservers(@Nullable LifecycleObserver[] observers) {
        Optional.ofNullable(observers).ifPresent(list -> {
            for (LifecycleObserver observer : list) {
                addObserver(observer);
            }
        });
    }

    private void addObserver(LifecycleObserver observer) {
        mObservers.add(observer);
        mLC.addObserver(observer);
    }

    public static class DialogVM extends ViewModel {
        boolean hasDialog; // 标识此前是否持有过弹窗
    }
}
