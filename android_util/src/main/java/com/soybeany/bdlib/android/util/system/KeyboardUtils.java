package com.soybeany.bdlib.android.util.system;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 软键盘工具类
 * <br>Created by Ben on 2016/1/23.
 */
public class KeyboardUtils {

    /**
     * 获得输入法管理器
     */
    public static InputMethodManager getInputMethodManager(Context context) {
        return (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    /**
     * 打开软键盘
     *
     * @param context 上下文
     * @param view    视图
     */
    public static void openKeyboard(Context context, View view) {
        InputMethodManager imm = getInputMethodManager(context);
        imm.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     *
     * @param context 上下文
     * @param view    视图
     */
    public static void closeKeyboard(Context context, View view) {
        InputMethodManager imm = getInputMethodManager(context);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 关闭软键盘
     */
    public static void closeKeyboard(Activity activity) {
        InputMethodManager imm = getInputMethodManager(activity);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}
