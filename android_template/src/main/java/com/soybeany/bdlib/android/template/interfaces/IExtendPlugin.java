package com.soybeany.bdlib.android.template.interfaces;

import android.os.Handler;
import android.os.Looper;

import com.soybeany.bdlib.android.util.IObserver;

/**
 * 可拓展插件
 * <br>Created by Soybeany on 2019/4/29.
 */
public interface IExtendPlugin extends IInitTemplate, IObserver {
    static void invokeInUiThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
