package com.soybeany.bdlib.android.util.system;

import android.util.DisplayMetrics;

import com.soybeany.bdlib.android.util.BDContext;


/**
 * 密度工具类
 * <br>Created by Soybeany on 2017/4/18.
 */
public class DensityUtils {

    /**
     * dp转px
     */
    public static int dp2px(float dpValue) {
        return (int) (dpValue * getMetrics().density + 0.5f);
    }

    /**
     * px转dp
     */
    public static int px2dp(float pxValue) {
        return (int) (pxValue / getMetrics().density + 0.5f);
    }

    /**
     * sp换px
     */
    public static int sp2px(float spValue) {
        return (int) (spValue * getMetrics().scaledDensity + 0.5f);
    }

    /**
     * px转sp
     */
    public static int px2sp(float pxValue) {
        return (int) (pxValue / getMetrics().scaledDensity + 0.5f);
    }

    /**
     * 获得度量
     */
    private static DisplayMetrics getMetrics() {
        return BDContext.getResources().getDisplayMetrics();
    }

}
