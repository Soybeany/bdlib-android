package com.soybeany.bdlib.android.template.lifecycle;

import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.view.View;

import com.soybeany.bdlib.android.util.IObserver;
import com.soybeany.bdlib.core.java8.Optional;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 使用时需在项目的依赖中添加 annotationProcessor 'com.jakewharton:butterknife-compiler:8.8.1'(版本按需修改)
 * <br>Created by Soybeany on 2019/3/16.
 */
public class ButterKnifeObserver implements IObserver {
    private Unbinder mUnBinder;

    public ButterKnifeObserver(ICallback callback) {
        if (null == callback) {
            return;
        }
        Object target = callback.onGetButterKnifeTarget();
        Object source = callback.onGetButterKnifeSource();
        if (source instanceof Activity) {
            mUnBinder = ButterKnife.bind(target, (Activity) source);
        } else if (source instanceof View) {
            mUnBinder = ButterKnife.bind(target, (View) source);
        } else if (source instanceof Dialog) {
            mUnBinder = ButterKnife.bind(target, (Dialog) source);
        } else {
            throw new RuntimeException("使用了不支持的source类型");
        }
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        Optional.ofNullable(mUnBinder).ifPresent(Unbinder::unbind);
    }

    public interface ICallback<T> {
        default Object onGetButterKnifeTarget() {
            return this;
        }

        T onGetButterKnifeSource();
    }
}
