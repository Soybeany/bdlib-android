package com.soybeany.bdlib.android.util;

import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * 吐司工具类，维护着一个吐司单例
 * <br>Created by Soybeany on 2017/3/13.
 */
public class ToastUtils {

    private static Toast mToast = getNew(); // 应用级别的吐司
    private static Toast mLastToast; // 上一次使用的吐司

    private ToastUtils() {

    }

    /**
     * 获得新的吐司
     */
    public static Toast getNew() {
        return Toast.makeText(BDContext.getContext(), "", Toast.LENGTH_SHORT);
    }

    /**
     * 获得新的吐司(允许自定义位置)
     */
    public static Toast getNew(int gravity, int XOffset, int YOffset) {
        Toast toast = getNew();
        toast.setGravity(gravity, XOffset, YOffset);
        return toast;
    }

    /**
     * 显示吐司(短)
     *
     * @param msg 需要显示的信息
     */
    public static void show(String msg) {
        show(mToast, msg);
    }

    public static void show(@StringRes int resId) {
        show(BDContext.getResources().getString(resId));
    }

    /**
     * 显示吐司(短)
     *
     * @param msg 需要显示的信息
     */
    public static void show(Toast toast, String msg) {
        innerShow(toast, msg, true);
    }

    /**
     * 显示吐司(长)
     */
    public static void showLong(String msg) {
        showLong(mToast, msg);
    }

    /**
     * 显示吐司(长)
     */
    public static void showLong(Toast toast, String msg) {
        innerShow(toast, msg, false);
    }

    /**
     * 取消吐司的显示
     */
    public static void cancel() {
        mToast.cancel();
    }

    /**
     * 显示吐司
     */
    private static void innerShow(Toast toast, String msg, boolean isShort) {
        // 处理上一次的吐司
        if (null != mLastToast && mLastToast != toast) {
            mLastToast.cancel();
        }
        mLastToast = toast;
        toast.setText(msg);
        toast.setDuration(isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
        toast.show();
    }
}
