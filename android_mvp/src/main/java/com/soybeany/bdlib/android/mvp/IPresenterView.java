package com.soybeany.bdlib.android.mvp;

import android.support.annotation.MainThread;

import com.soybeany.bdlib.android.util.ToastUtils;

/**
 * <br>Created by Soybeany on 2019/4/16.
 */
public interface IPresenterView {
    @MainThread
    default void showToast(String msg) {
        ToastUtils.show(msg);
    }
}
