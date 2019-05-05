package com.soybeany.bdlib.android.template.vm;

import android.arch.lifecycle.ViewModel;

import com.soybeany.bdlib.android.util.BDContext;
import com.soybeany.bdlib.core.util.storage.KeyValueStorage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 用于计划任务的ViewModel
 * <br>Created by Soybeany on 2019/3/18.
 */
public class ScheduledViewModel extends ViewModel {
    private final ScheduledThreadPoolExecutor mExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);
    private KeyValueStorage<String, Runnable> mStorage = new KeyValueStorage<>();

    @Override
    protected void onCleared() {
        super.onCleared();
        mStorage.clear();
        mExecutor.shutdown();
    }

    public ScheduledViewModel corePoolSize(int size) {
        mExecutor.setCorePoolSize(size);
        return this;
    }

    public void schedule(Runnable runnable, long delay, TimeUnit unit) {
        mExecutor.schedule(getSafeRunnable(runnable), delay, unit);
    }

    public void scheduleWithFixedDelay(Runnable runnable, long initialDelay, long delay, TimeUnit unit) {
        mExecutor.scheduleWithFixedDelay(getSafeRunnable(runnable), initialDelay, delay, unit);
    }

    public void scheduleAtFixedRate(Runnable runnable, long initialDelay, long period, TimeUnit unit) {
        mExecutor.scheduleAtFixedRate(getSafeRunnable(runnable), initialDelay, period, unit);
    }

    private Runnable getSafeRunnable(Runnable runnable) {
        String key = BDContext.getUID();
        mStorage.put(key, runnable);
        return () -> mStorage.invoke(key, Runnable::run); // 存储中有值才继续执行
    }
}
