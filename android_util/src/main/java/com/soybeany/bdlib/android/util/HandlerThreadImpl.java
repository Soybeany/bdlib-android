package com.soybeany.bdlib.android.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.util.thread.IExecutable;

public class HandlerThreadImpl implements IExecutable {

    public static final IExecutable UI_THREAD = getNew(Looper.getMainLooper(), false);

    private static int mThreadInitNumber; // 线程编号

    private Thread thread; // 线程
    private Handler handler; // 处理器

    private boolean canStop; // 是否允许停止


    private static synchronized int nextThreadNum() {
        return mThreadInitNumber++;
    }

    public static IExecutable getNew(String name) {
        return getNew(name, true);
    }

    public static IExecutable getNew(Looper looper) {
        return getNew(looper, true);
    }

    /**
     * 获得新处理器
     */
    private static HandlerThreadImpl getNew(Looper looper, boolean canStop) {
        return Optional.ofNullable(looper)
                .map(obj -> new HandlerThreadImpl(looper.getThread(), new Handler(looper), canStop))
                .orElseGet(() -> getNew("loop", canStop));
    }

    private static HandlerThreadImpl getNew(String name, boolean canStop) {
        HandlerThread thread = new HandlerThread("BDMsgCenter" + name + "-" + nextThreadNum());
        thread.start();
        return new HandlerThreadImpl(thread, new Handler(thread.getLooper()), canStop);
    }

    private HandlerThreadImpl(Thread thread, Handler handler, boolean canStop) {
        this.thread = thread;
        this.handler = handler;
        this.canStop = canStop;
    }

    @Override
    public void post(Runnable runnable, long delayMills) {
        handler.postDelayed(runnable, delayMills);
    }

    @Override
    public void stop() {
        if (canStop && thread instanceof HandlerThread) {
            ((HandlerThread) thread).quitSafely();
        }
    }
}