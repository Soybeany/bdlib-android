package com.soybeany.bdlib.android.util.style;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.View;

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
            Optional.ofNullable(statusBarV).ifPresent(v -> {
                setBackground(v, activity, res);
                if (needAddListener) {
                    decorView.addOnLayoutChangeListener((view, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> setBackground(v, activity, res));
                }
            });
        }));
    }

    /**
     * 每次均创建，以免不能触发底色替换
     */
    private static void setBackground(@NonNull View statusBarV, @NonNull Activity activity, @DrawableRes Integer res) {
        // 设置为指定资源
        if (null != res) {
            statusBarV.setBackgroundResource(res);
            return;
        }
        // 设置为默认资源
        TypedValue typedValue = new TypedValue();
        activity.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        int data = typedValue.data;
        statusBarV.setBackground(new ColorDrawable(data));
    }
}
