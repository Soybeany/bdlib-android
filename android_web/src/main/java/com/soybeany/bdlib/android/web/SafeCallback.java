package com.soybeany.bdlib.android.web;

import android.arch.lifecycle.LifecycleOwner;

import com.soybeany.bdlib.web.okhttp.core.ICallback;

/**
 * <br>Created by Soybeany on 2019/4/15.
 */
public abstract class SafeCallback<Result> implements ICallback<Result> {
    final LifecycleOwner owner;

    public SafeCallback(LifecycleOwner owner) {
        this.owner = owner;
    }
}
