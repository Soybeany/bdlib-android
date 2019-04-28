package com.soybeany.bdlib.android.util.dialog;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.soybeany.bdlib.android.util.R;
import com.soybeany.bdlib.android.util.StdHintUtils;
import com.soybeany.bdlib.core.java8.Optional;

import static com.soybeany.bdlib.android.util.BDContext.getResources;

/**
 * <br>Created by Soybeany on 2019/3/21.
 */
public class DialogMsg implements Comparable<DialogMsg> {

    String prefix = StdHintUtils.loadingPrefix();
    String suffix = StdHintUtils.loadingSuffix2();
    String hint;
    IMultiHintListener hintListener = (msg, count, cancelable) -> getResources().getString(R.string.bd_dialog_multi_hint, count);
    boolean cancelable;

    long showTime; // 此信息被show时的时间

    public DialogMsg(String hint) {
        hint(hint);
    }

    public DialogMsg(@StringRes int resId) {
        this(getResources().getString(resId));
    }

    @Override
    public int compareTo(@NonNull DialogMsg o) {
        return (int) (showTime - o.showTime);
    }

    /**
     * 设置单项提示
     */
    public DialogMsg hint(String hint) {
        this.hint = Optional.ofNullable(hint).orElseGet(() -> getResources().getString(R.string.bd_dialog_default_hint));
        return this;
    }

    /**
     * 获得单项提示
     */
    public String hint() {
        return hint;
    }

    /**
     * 设置多项提示
     */
    public DialogMsg multiHint(IMultiHintListener listener) {
        if (null != listener) {
            hintListener = listener;
        }
        return this;
    }

    /**
     * 设置返回键是否能取消弹窗
     */
    public DialogMsg cancelable(boolean flag) {
        this.cancelable = flag;
        return this;
    }

    /**
     * 弹窗是否能被取消
     */
    public boolean isCancelable() {
        return cancelable;
    }

    public interface IMultiHintListener {
        String getHint(String hint, int count, boolean cancelable);
    }
}
