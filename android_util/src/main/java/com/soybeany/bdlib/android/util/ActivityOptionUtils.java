package com.soybeany.bdlib.android.util;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;

/**
 * Activity切换动画工具类
 * <br>Created by Soybeany on 2019/4/18.
 */
public class ActivityOptionUtils {

    /**
     * 淡进淡出
     */
    public static Bundle fadeInOut(Context context) {
        return ActivityOptionsCompat.makeCustomAnimation(context, android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
    }

}
