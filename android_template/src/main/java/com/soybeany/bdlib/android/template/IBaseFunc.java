package com.soybeany.bdlib.android.template;

import android.arch.lifecycle.Lifecycle;

import com.soybeany.bdlib.android.template.lifecycle.ButterKnifeObserver;
import com.soybeany.bdlib.android.util.dialog.AbstractDialog;
import com.soybeany.bdlib.android.util.dialog.DialogKeyProvider;

/**
 * <br>Created by Soybeany on 2019/4/18.
 */
public interface IBaseFunc {
    default void onSignalDevCallbacks() {
    }

    AbstractDialog getDialog();

    DialogKeyProvider getDialogKeys();

    interface IEx<Source> extends IBaseFunc, IDevTemplate, IVMProvider, ButterKnifeObserver.ICallback<Source> {

        void signalDoBusiness();

        Lifecycle getLifecycle();

        AbstractDialog onGetNewDialog();
    }
}
