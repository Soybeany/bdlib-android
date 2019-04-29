package com.soybeany.bdlib.android.template;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.template.lifecycle.ButterKnifeObserver;
import com.soybeany.bdlib.android.util.dialog.AbstractDialog;
import com.soybeany.bdlib.android.util.dialog.DialogKeyProvider;
import com.soybeany.bdlib.android.util.system.PermissionRequester;

/**
 * 定义通用的基础功能
 * <br>Created by Soybeany on 2019/4/18.
 */
public interface IBaseFunc {
    default void signalAfterSetContentView() {
    }

    AbstractDialog getDialog();

    DialogKeyProvider getDialogKeys();

    boolean requestPermissions(@NonNull PermissionRequester.IPermissionCallback callback, @Nullable String... permissions);

    void checkPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

    interface IEx<Source> extends IBaseFunc, IDevTemplate, IVMProvider, ButterKnifeObserver.ICallback<Source> {

        default void signalBeforeSetContentView() {
        }

        default String[] getEssentialPermissions() {
            return null;
        }

        PermissionRequester.IPermissionCallback getEssentialPermissionCallback();

        @Nullable
        default LifecycleObserver[] signalExObservers() {
            return null;
        }

        Lifecycle getLifecycle();

        AbstractDialog onGetNewDialog();

        PermissionRequester onGetNewPermissionRequester();
    }
}
