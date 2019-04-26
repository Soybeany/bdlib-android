package com.soybeany.bdlib.android.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 视图转移工具类
 * <br>Created by Soybeany on 2017/3/21.
 */
public class ViewTransferUtils {

    /**
     * 最后一位的下标
     */
    public static final int LAST_INDEX = -1;

    /**
     * 转移视图
     *
     * @param view      被取代的视图
     * @param viewGroup 用于取代的布局
     * @param index     被取代的视图在取代布局中的位置
     */
    public static void transfer(View view, ViewGroup viewGroup, int index) {
        transfer(view, viewGroup, getDefaultLayoutParams(), index);
    }

    /**
     * 转移视图
     *
     * @param view      被取代的视图
     * @param viewGroup 用于取代的布局
     * @param params    被取代的视图在取代布局中的布局参数
     * @param index     被取代的视图在取代布局中的位置
     */
    public static void transfer(View view, ViewGroup viewGroup, ViewGroup.LayoutParams params, int index) {
        transfer(view, viewGroup, params, index, true);
    }

    /**
     * 取代视图
     *
     * @param view      被取代的视图
     * @param viewGroup 用于取代的布局
     */
    public static void replace(View view, ViewGroup viewGroup) {
        transfer(view, viewGroup, null, LAST_INDEX, false);
    }

    /**
     * 获得填充父视图的线性布局参数
     */
    public static LinearLayout.LayoutParams getLinearLayoutParams() {
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    /**
     * 获得默认的布局参数
     */
    public static ViewGroup.MarginLayoutParams getDefaultLayoutParams() {
        return new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    /**
     * 获得填充父视图的帧布局参数
     */
    public static FrameLayout.LayoutParams getFrameLayoutParams() {
        return new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    /**
     * 获得填充父视图的相对布局参数
     */
    public static RelativeLayout.LayoutParams getRelativeLayoutParams() {
        return new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    /**
     * 转移视图
     */
    private static void transfer(View view, ViewGroup viewGroup, ViewGroup.LayoutParams params, int index, boolean needAdd) {
        // 提取原本view的布局参数
        viewGroup.setLayoutParams(view.getLayoutParams());

        // 确保新布局空闲
        ViewGroup parent = (ViewGroup) viewGroup.getParent();
        if (null != parent) {
            parent.removeView(viewGroup);
        }

        // 获得原本view的位置，并用新布局替代其位置
        parent = (ViewGroup) view.getParent();
        if (null != parent) {
            for (int i = 0; i < parent.getChildCount(); i++) {
                if (view.equals(parent.getChildAt(i))) {
                    parent.removeViewAt(i); // 移除原本view
                    parent.addView(viewGroup, i); // 替代为新布局
                    break;
                }
            }
        }

        if (needAdd) {
            // 将原本view转移到新布局中
            view.setLayoutParams(params);
            viewGroup.addView(view, index);
        }
    }

}
