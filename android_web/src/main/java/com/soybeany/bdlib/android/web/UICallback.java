package com.soybeany.bdlib.android.web;


import com.soybeany.bdlib.android.util.ToastUtils;
import com.soybeany.bdlib.web.okhttp.core.ICallback;

import static com.soybeany.bdlib.android.util.BDContext.MAIN_HANDLER;

/**
 * 使用UI线程的回调(也包含工作线程)
 * <br>Created by Soybeany on 2019/3/7.
 */
public interface UICallback<Result> extends ICallback<Result> {

    @Override
    default void onSuccess(Result result) {
        onWorkSuccess(result);
        MAIN_HANDLER.post(() -> onUISuccess(result));
    }

    @Override
    default void onFailure(boolean isCanceled, String msg) {
        onWorkFailure(isCanceled, msg);
        MAIN_HANDLER.post(() -> onUIFailure(isCanceled, msg));
    }

    @Override
    default void onFinal(boolean isCanceled) {
        onWorkFinal(isCanceled);
        MAIN_HANDLER.post(() -> onUIFinal(isCanceled));
    }

    // //////////////////////////////////UI线程//////////////////////////////////

    /**
     * 成功时的回调(UI线程)
     */
    void onUISuccess(Result result);

    /**
     * 失败时的回调(UI线程)
     */
    default void onUIFailure(boolean isCanceled, String msg) {
        ToastUtils.show(msg);
    }

    /**
     * 最终的回调(UI线程)
     */
    default void onUIFinal(boolean isCanceled) {
    }

    // //////////////////////////////////工作线程//////////////////////////////////

    /**
     * 成功时的回调(工作线程)
     */
    default void onWorkSuccess(Result result) {
    }

    /**
     * 失败时的回调(工作线程)
     */
    default void onWorkFailure(boolean isCanceled, String msg) {
    }

    /**
     * 最终的回调(工作线程)
     */
    default void onWorkFinal(boolean isCanceled) {
    }

    // //////////////////////////////////默认实现//////////////////////////////////

    interface Empty<Result> extends UICallback<Result> {
        @Override
        default void onUISuccess(Result result) {
        }

        @Override
        default void onUIFailure(boolean isCanceled, String msg) {
        }
    }
}
