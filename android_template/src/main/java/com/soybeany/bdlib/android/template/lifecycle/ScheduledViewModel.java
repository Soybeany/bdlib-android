package com.soybeany.bdlib.android.template.lifecycle;

import android.arch.lifecycle.ViewModel;

import com.soybeany.bdlib.core.util.thread.KeyValueStorage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 用于计划任务的ViewModel
 * <br>Created by Soybeany on 2019/3/18.
 */
public class ScheduledViewModel extends ViewModel {
    private final ScheduledThreadPoolExecutor mExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);
    private KeyValueStorage mStorage = new KeyValueStorage();

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
        String key = mStorage.put(runnable);
        return () -> mStorage.invoke(key, r -> ((Runnable) r).run()); // 存储中有值才继续执行
    }
}
