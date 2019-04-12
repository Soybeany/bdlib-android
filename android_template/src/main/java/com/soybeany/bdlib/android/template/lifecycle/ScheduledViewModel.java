package com.soybeany.bdlib.android.template.lifecycle;

import android.arch.lifecycle.ViewModel;

import com.soybeany.bdlib.android.util.BDContext;
import com.soybeany.bdlib.core.java8.Optional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 用于计划任务的ViewModel
 * <br>Created by Soybeany on 2019/3/18.
 */
public class ScheduledViewModel extends ViewModel {
    private final ScheduledThreadPoolExecutor mExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);
    private final Map<String, Runnable> mRunnableMap = new HashMap<>(); // 用于缓存runnable，避免结束后还持有对象

    @Override
    protected void onCleared() {
        super.onCleared();
        mRunnableMap.clear();
        mExecutor.shutdown();
    }

    public ScheduledViewModel corePoolSize(int size) {
        mExecutor.setCorePoolSize(size);
        return this;
    }

    public void schedule(Runnable runnable, long delay, TimeUnit unit) {
        mExecutor.schedule(getSafeRunnable(runnable), delay, unit);
    }

    public void scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        mExecutor.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }

    public void scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        mExecutor.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    private Runnable getSafeRunnable(Runnable runnable) {
        String key = BDContext.getUID();
        mRunnableMap.put(key, runnable);
        return () -> Optional.ofNullable(mRunnableMap.get(key)).ifPresent(Runnable::run);
    }
}
