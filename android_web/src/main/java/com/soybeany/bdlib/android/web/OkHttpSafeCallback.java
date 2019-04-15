package com.soybeany.bdlib.android.web;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;

import com.soybeany.bdlib.android.util.IObserver;
import com.soybeany.bdlib.core.util.file.IProgressListener;
import com.soybeany.bdlib.core.util.storage.KeySetStorage;
import com.soybeany.bdlib.web.okhttp.core.ICallback;
import com.soybeany.bdlib.web.okhttp.core.OkHttpCallback;
import com.soybeany.bdlib.web.okhttp.parser.IParser;

/**
 * <br>Created by Soybeany on 2019/4/15.
 */
public class OkHttpSafeCallback<Result> extends OkHttpCallback<Result> implements IObserver {
    private KeySetStorage<LifecycleOwner, ICallback<Result>> mStorage = new KeySetStorage<>();

    public OkHttpSafeCallback(IParser<Result> parser) {
        super(parser);
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        owner.getLifecycle().removeObserver(this);
        mStorage.invokeVal(owner, this::removeCallback);
        mStorage.remove(owner);
    }

    @Override
    public OkHttpSafeCallback<Result> downloadListener(IProgressListener listener) {
        super.downloadListener(listener);
        return this;
    }

    @Override
    public OkHttpCallback<Result> addCallback(ICallback<Result> callback) {
        throw new RuntimeException("请使用addSafeCallback或addUnsafeCallback");
    }

    @Override
    public OkHttpSafeCallback<Result> removeCallback(ICallback<Result> callback) {
        super.removeCallback(callback);
        return this;
    }

    /**
     * 安全回调，View相关的调用需使用此回调
     */
    public OkHttpSafeCallback<Result> addSafeCallback(SafeCallback<Result> callback) {
        return addSafeCallback(callback.owner, callback);
    }

    /**
     * 安全回调，View相关的调用需使用此回调
     */
    public OkHttpSafeCallback<Result> addSafeCallback(LifecycleOwner owner, ICallback<Result> callback) {
        owner.getLifecycle().addObserver(this);
        super.addCallback(callback);
        return this;
    }

    /**
     * 非安全回调，非View相关的调用可以使用此回调
     */
    public OkHttpSafeCallback<Result> addUnsafeCallback(ICallback<Result> callback) {
        super.addCallback(callback);
        return this;
    }
}
