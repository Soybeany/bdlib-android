package com.soybeany.bdlib.android.util;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.soybeany.bdlib.android.util.system.DeviceInfoUtils;

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
        registerActivityLifecycleCallbacks(mCallback.hint(setupHackedHint())
                .keyDetectValve(setupKeyDetectValve()).onStartDetectValve(setupOnStartDetectValve()));
        registerDeviceBtnWatcher();
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
     * 设置key检测阈值，默认1秒
     */
    protected long setupKeyDetectValve() {
        return 1000;
    }

    /**
     * 设置onStart检测阈值，默认1秒
     */
    protected long setupOnStartDetectValve() {
        return 1000;
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
        private long mKeyDetectValve = 1000; // Key检测阈值
        private long mOnStartDetectValve = 1000; // onStart检测阈值

        private long mLastStartTime; // 最后一次调用onStart的时间
        private Handler mHandler = new Handler(Looper.getMainLooper());

        LifecycleCallback(DeviceBtnClickWatcher watcher) {
            mWatcher = watcher;
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            activityStack.push(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            mLastStartTime = System.currentTimeMillis();
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
            if (mNeedHint && activity == getTopActivity() && mWatcher.getDeltaTime() > mKeyDetectValve) {
                // 一定延迟后若页面还没恢复，则认为是被劫持
                mHandler.postDelayed(() -> {
                    if (System.currentTimeMillis() - mLastStartTime > mOnStartDetectValve) {
                        ToastUtils.showLong(mHint);
                    }
                }, mOnStartDetectValve);
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
         * 设置Key检测阈值(0 ~ 10秒)
         */
        LifecycleCallback keyDetectValve(long valve) {
            if (valve > 0 && valve <= 10000) {
                mKeyDetectValve = valve;
            }
            return this;
        }

        /**
         * 设置onStart检测阈值(0 ~ 10秒)
         */
        LifecycleCallback onStartDetectValve(long valve) {
            if (valve > 0 && valve <= 10000) {
                mOnStartDetectValve = valve;
            }
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
