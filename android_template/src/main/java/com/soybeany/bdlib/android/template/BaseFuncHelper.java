package com.soybeany.bdlib.android.template;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.soybeany.bdlib.android.util.BDContext;
import com.soybeany.bdlib.android.util.IObserver;
import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.util.storage.MessageCenter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import static com.soybeany.bdlib.android.util.HandlerThreadImpl.UI_THREAD;

/**
 * 通用功能辅助器
 * <br>Created by Soybeany on 2019/4/16.
 */
class BaseFuncHelper implements IObserver {
    private VM mVM;
    private Lifecycle mLifecycle;
    private final LinkedList<LifecycleObserver> mObservers = new LinkedList<>();

    /**
     * 初始化，在onCreate中调用
     */
    BaseFuncHelper init(Lifecycle lifecycle, VM vm) {
        mLifecycle = lifecycle;
        mVM = vm;
        return this;
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        removeObservers();
        unRegister();
    }

    /**
     * 注册回调，自动注销
     *
     * @return 用于监听的uid
     */
    String autoRegister(String key, MessageCenter.ICallback callback) {
        String uid = getUid(key);
        MessageCenter.register(UI_THREAD, uid, callback);
        return uid;
    }

    /**
     * 一般放在onCreate中调用
     */
    void addObservers(LifecycleObserver[] observerArr, LifecycleObserver... additionalArr) {
        Optional.ofNullable(observerArr).ifPresent(observers -> mObservers.addAll(Arrays.asList(observers)));
        Optional.ofNullable(additionalArr).ifPresent(additional -> mObservers.addAll(Arrays.asList(additional)));
        // 进行回调
        for (LifecycleObserver observer : mObservers) {
            mLifecycle.addObserver(observer);
        }
    }

    private void removeObservers() {
        Iterator<LifecycleObserver> iterator = mObservers.iterator();
        while (iterator.hasNext()) {
            mLifecycle.removeObserver(iterator.next());
            iterator.remove();
        }
    }

    private void unRegister() {
        for (String uid : mVM.keyMap.values()) {
            MessageCenter.unregister(uid);
        }
    }

    /**
     * 根据key获得稳定的uid
     */
    private String getUid(String key) {
        String uid = mVM.keyMap.get(key);
        if (null == uid) {
            mVM.keyMap.put(key, uid = BDContext.getUID());
        }
        return uid;
    }

    public static class VM extends ViewModel {
        final Map<String, String> keyMap = new HashMap<>(); // 用于记录生成的随机key
    }
}
