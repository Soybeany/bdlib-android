package com.soybeany.bdlib.android.util.system;

import com.soybeany.bdlib.android.util.BDContext;
import com.soybeany.bdlib.android.util.R;
import com.soybeany.bdlib.android.util.ToastUtils;
import com.soybeany.bdlib.core.java8.function.Supplier;
import com.soybeany.bdlib.core.util.TimeUtils;

/**
 * 双击检测器
 * <br>Created by Soybeany on 2019/5/16.
 */
public class DoubleClickChecker {

    private long mLastClickTime = 0; // 上一次点击返回的时间
    private long mInterval = 2 * TimeUtils.SECOND; // 检测间隔，默认2秒

    public static String getFinishHint() {
        return BDContext.getString(R.string.bd_double_click_exit);
    }

    /**
     * 设置检测间隔
     *
     * @param interval 间隔，单位：毫秒（1秒->1000）
     */
    public DoubleClickChecker interval(long interval) {
        mInterval = interval;
        return this;
    }

    /**
     * 检测是否双击
     */
    public boolean isDoubleClicked() {
        long currentTime = TimeUtils.getCurrentTimeStamp();
        if (currentTime - mLastClickTime < mInterval) {
            mLastClickTime = 0; // 重置
            return true;
        }
        mLastClickTime = currentTime;
        return false;
    }

    public void checkFinish(Runnable onDoubleClick) {
        check(DoubleClickChecker::getFinishHint, onDoubleClick);
    }

    /**
     * 检测
     */
    public void check(Supplier<String> hint, Runnable onDoubleClick) {
        if (isDoubleClicked()) {
            onDoubleClick.run();
            ToastUtils.cancel();
            return;
        }
        ToastUtils.show(hint.get());
    }
}
