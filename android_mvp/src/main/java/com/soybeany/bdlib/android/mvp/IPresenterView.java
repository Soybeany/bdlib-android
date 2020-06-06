package com.soybeany.bdlib.android.mvp;

import com.soybeany.bdlib.android.util.ToastUtils;

import androidx.annotation.MainThread;

/**
 * <br>Created by Soybeany on 2019/4/16.
 */
public interface IPresenterView {
    @MainThread
    default void showToast(String msg) {
        ToastUtils.show(msg);
    }
}
