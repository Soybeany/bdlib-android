package com.soybeany.bdlib.android.util.style;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.View;

import com.soybeany.bdlib.android.util.BDContext;
import com.soybeany.bdlib.android.util.R;
import com.soybeany.bdlib.core.java8.Optional;

import static com.soybeany.bdlib.android.util.BDContext.getResources;

/**
 * <br>Created by Soybeany on 2019/4/25.
 */
public class DrawableStatusBarUtils {

    /**
     * 全面应用，带有监听器监听每次布局的变化，需在{@link Activity#setContentView(int)}后调用
     *
     * @param res 资源值，为null时则使用主题默认的设置
     */
    public static void fullApply(@NonNull Activity activity, @DrawableRes Integer res) {
        innerApply(activity, res, true);
    }

    /**
     * 简单应用，需在{@link Activity#setContentView(int)}后调用
     */
    public static void simpleApply(@NonNull Activity activity, @DrawableRes Integer res) {
        innerApply(activity, res, false);
    }

    private static void innerApply(@NonNull Activity activity, @DrawableRes Integer res, boolean needAddListener) {
        Optional.ofNullable(activity.getWindow().getDecorView()).ifPresent(decorView -> decorView.post(() -> {
            Resources resources = getResources();
            View statusBarV = decorView.findViewById(resources.getIdentifier("statusBarBackground", "id", "android"));
            if (null == statusBarV) {
                return;
            }
            int defaultColor = BDContext.getAttributeValue(R.attr.colorPrimaryDark).data;
            setBackground(statusBarV, res, defaultColor);
            if (needAddListener) {
                decorView.addOnLayoutChangeListener((view, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> setBackground(statusBarV, res, defaultColor));
            }
        }));
    }

    private static void setBackground(@NonNull View statusBarV, @DrawableRes Integer res, int defaultColor) {
        // 设置为指定资源
        if (null != res) {
            statusBarV.setBackgroundResource(res);
            return;
        }
        // 设置为默认资源
        Drawable drawable = statusBarV.getBackground();
        if (drawable instanceof ColorDrawable && ((ColorDrawable) drawable).getColor() != defaultColor) {
            statusBarV.setBackgroundColor(defaultColor);
        }
    }
}
