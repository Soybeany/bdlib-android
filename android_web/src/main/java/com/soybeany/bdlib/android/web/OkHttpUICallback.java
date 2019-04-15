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
public class OkHttpUICallback<Result> extends OkHttpCallback<Result> implements IObserver {
    private KeySetStorage<LifecycleOwner, ICallback<Result>> mStorage = new KeySetStorage<>();

    public OkHttpUICallback(IParser<Result> parser) {
        super(parser);
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        owner.getLifecycle().removeObserver(this);
        mStorage.invokeVal(owner, this::removeCallback);
        mStorage.remove(owner);
    }

    @Override
    public OkHttpUICallback<Result> downloadListener(IProgressListener listener) {
        super.downloadListener(listener);
        return this;
    }

    @Override
    public OkHttpCallback<Result> addCallback(ICallback<Result> callback) {
        throw new RuntimeException("请使用addSafeCallback或addUnsafeCallback");
    }

    @Override
    public OkHttpUICallback<Result> removeCallback(ICallback<Result> callback) {
        super.removeCallback(callback);
        return this;
    }

    /**
     * UI回调，View相关的调用需使用此回调
     */
    public OkHttpUICallback<Result> addUICallback(LifecycleCallback<Result> callback) {
        return addUICallback(callback.owner, callback);
    }

    /**
     * UI回调，View相关的调用需使用此回调
     */
    public OkHttpUICallback<Result> addUICallback(LifecycleOwner owner, UICallback<Result> callback) {
        owner.getLifecycle().addObserver(this);
        super.addCallback(callback);
        return this;
    }

    /**
     * 非UI回调，非View相关的调用可以使用此回调
     */
    public OkHttpUICallback<Result> addNonUICallback(ICallback<Result> callback) {
        super.addCallback(callback);
        return this;
    }
}
