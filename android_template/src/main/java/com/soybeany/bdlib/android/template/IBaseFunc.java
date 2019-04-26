package com.soybeany.bdlib.android.template;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.template.lifecycle.ButterKnifeObserver;
import com.soybeany.bdlib.android.util.dialog.AbstractDialog;
import com.soybeany.bdlib.android.util.dialog.DialogKeyProvider;

/**
 * <br>Created by Soybeany on 2019/4/18.
 */
public interface IBaseFunc {
    default void signalAfterSetContentView() {
    }

    AbstractDialog getDialog();

    DialogKeyProvider getDialogKeys();

    interface IEx<Source> extends IBaseFunc, IDevTemplate, IVMProvider, ButterKnifeObserver.ICallback<Source> {

        default void signalBeforeSetContentView() {
        }

        void signalOnPostReady();

        @Nullable
        default LifecycleObserver[] signalExObservers() {
            return null;
        }

        Lifecycle getLifecycle();

        AbstractDialog onGetNewDialog();
    }
}
