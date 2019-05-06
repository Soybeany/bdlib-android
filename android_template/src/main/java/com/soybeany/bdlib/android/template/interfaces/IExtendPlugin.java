package com.soybeany.bdlib.android.template.interfaces;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.util.IObserver;
import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.java8.function.Consumer;

/**
 * 可拓展插件
 * <br>Created by Soybeany on 2019/4/29.
 */
public interface IExtendPlugin extends IInitTemplate, IObserver {
    static void invokeInUiThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    static <T> void invokeOnNotNull(@Nullable T template, Consumer<? super T> consumer) {
        Optional.ofNullable(template).ifPresent(consumer);
    }

    /**
     * 获得标识此插件的分组id，相同组的插件只允许存在一个
     */
    @NonNull
    String getGroupId();
}