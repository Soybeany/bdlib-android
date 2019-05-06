package com.soybeany.bdlib.android.template.plugins.extend;

import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;
import com.soybeany.bdlib.core.java8.Optional;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 使用时需在项目的依赖中添加 annotationProcessor 'com.jakewharton:butterknife-compiler:8.8.1'(版本按需修改)
 * <br>Created by Soybeany on 2019/4/30.
 */
public class ButterKnifePlugin implements IExtendPlugin {
    @Nullable
    private ICallback mCallback;
    private Unbinder mUnBinder;

    public ButterKnifePlugin(@Nullable ICallback callback) {
        mCallback = callback;
    }

    @Override
    public void initAfterSetContentView() {
        if (null == mCallback) {
            return;
        }
        Object target = mCallback.onGetButterKnifeTarget();
        Object source = mCallback.onGetButterKnifeSource();
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

    @NonNull
    @Override
    public final String getGroupId() {
        return "ButterKnife";
    }

    public interface ICallback {
        default Object onGetButterKnifeTarget() {
            return this;
        }

        Object onGetButterKnifeSource();
    }
}
