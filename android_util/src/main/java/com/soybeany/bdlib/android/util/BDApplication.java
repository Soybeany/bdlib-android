package com.soybeany.bdlib.android.util;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import java.util.Stack;

/**
 * 带有默认BD系列库配置的应用
 * <br>Created by Soybeany on 2017/3/24.
 */
public class BDApplication extends Application {

    private static final DeviceBtnClickWatcher mWatcher = new DeviceBtnClickWatcher(); // 设备按钮点击观察者
    private static final LifecycleCallback mCallback = new LifecycleCallback(mWatcher); // 生命周期回调

    /**
     * 获得最顶端的活动页面
     */
    public static Activity getTopActivity() {
        return mCallback.getTopActivity();
    }

    /**
     * 获得活动页面栈
     */
    public static Stack<Activity> getActivityStack() {
        return mCallback.activityStack;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BDContext.init(this);
        DeviceInfoUtils.print();
        registerActivityLifecycleCallbacks(mCallback.hint(setupHackedHint()).detectValve(setupDetectValve()));
        registerDeviceBtnWatcher();
        setPolicy();
    }

    @Override
    public void onTerminate() {
        unregisterDeviceBtnWatcher();
        unregisterActivityLifecycleCallbacks(mCallback);
        super.onTerminate();
    }

    /**
     * 禁用劫持提示
     */
    protected void disableHackedHint() {
        mCallback.mNeedHint = false;
    }

    /**
     * 设置被劫持时的提示
     */
    protected String setupHackedHint() {
        return "检测到“" + DeviceInfoUtils.getSoftwareInfo().appLabel + "”已进入后台";
    }

    /**
     * 设置检测阈值，默认1秒
     */
    protected long setupDetectValve() {
        return 1;
    }

    /**
     * 设置访问政策
     */
    private void setPolicy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
        }
    }

    /**
     * 注册设备按钮观察者
     */
    private void registerDeviceBtnWatcher() {
        registerReceiver(mWatcher, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    /**
     * 注销设备按钮观察者
     */
    private void unregisterDeviceBtnWatcher() {
        unregisterReceiver(mWatcher);
    }

    /**
     * 生命周期回调
     */
    private static class LifecycleCallback implements ActivityLifecycleCallbacks {

        Stack<Activity> activityStack = new Stack<>(); // 活动页面栈
        private DeviceBtnClickWatcher mWatcher; // 观察者

        private boolean mNeedHint = true; // 是否需要劫持提示
        private String mHint; // 提示语
        private long mDetectValve; // 检测阈值

        LifecycleCallback(DeviceBtnClickWatcher watcher) {
            mWatcher = watcher;
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            activityStack.push(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            // 留空
        }

        @Override
        public void onActivityResumed(Activity activity) {
            if (activity != getTopActivity()) {
                activityStack.remove(activity);
                activityStack.push(activity);
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            // 留空
        }

        @Override
        public void onActivityStopped(Activity activity) {
            if (mNeedHint && activity == getTopActivity() && mWatcher.getDeltaTime() > mDetectValve) {
                ToastUtils.showLong(mHint);
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            // 留空
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            activityStack.remove(activity);
            if (activityStack.isEmpty()) {
                ToastUtils.cancel();
            }
        }

        /**
         * 设置被劫持的提示语
         */
        LifecycleCallback hint(String hint) {
            mHint = hint;
            return this;
        }

        /**
         * 设置检测阈值
         */
        LifecycleCallback detectValve(long valve) {
            mDetectValve = valve;
            return this;
        }

        /**
         * 获得最顶端的活动页面
         */
        Activity getTopActivity() {
            return activityStack.peek();
        }
    }

    /**
     * 设备按钮点击的观察者
     */
    private static class DeviceBtnClickWatcher extends BroadcastReceiver {

        private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
        private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        private long mLastClickStamp; // 上一次点击的戳

        @Override
        public void onReceive(Context context, Intent intent) {
            if (!Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
                return;
            }
            String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
            if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason) || SYSTEM_DIALOG_REASON_RECENT_APPS.equals(reason)) {
                mLastClickStamp = System.currentTimeMillis();
            }
        }

        /**
         * 获得离最后一次点击的时间差
         */
        long getDeltaTime() {
            return System.currentTimeMillis() - mLastClickStamp;
        }
    }

}
