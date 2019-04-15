package com.soybeany.bdlib.android.web;

import android.arch.lifecycle.LifecycleOwner;

/**
 * <br>Created by Soybeany on 2019/4/15.
 */
public abstract class LifecycleCallback<Result> implements UICallback<Result> {
    final LifecycleOwner owner;

    public LifecycleCallback(LifecycleOwner owner) {
        this.owner = owner;
    }
}
