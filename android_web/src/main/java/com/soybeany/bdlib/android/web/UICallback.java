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
    default void onPreTreat(int id, boolean hasResponse) {
        onWorkPreTreat(id, hasResponse);
        MAIN_HANDLER.post(() -> onUIPreTreat(id, hasResponse));
    }

    @Override
    default void onSuccess(int id, Result result) {
        onWorkSuccess(id, result);
        MAIN_HANDLER.post(() -> onUISuccess(id, result));
    }

    @Override
    default void onFailure(int id, boolean isCanceled, String msg) {
        onWorkFailure(id, isCanceled, msg);
        MAIN_HANDLER.post(() -> onUIFailure(id, isCanceled, msg));
    }

    @Override
    default void onFinal(int id, boolean isCanceled) {
        onWorkFinal(id, isCanceled);
        MAIN_HANDLER.post(() -> onUIFinal(id, isCanceled));
    }

    // //////////////////////////////////UI线程//////////////////////////////////

    /**
     * 预处理的回调(UI线程)
     */
    default void onUIPreTreat(int id, boolean hasResponse) {
    }

    /**
     * 成功时的回调(UI线程)
     */
    void onUISuccess(int id, Result result);

    /**
     * 失败时的回调(UI线程)
     */
    default void onUIFailure(int id, boolean isCanceled, String msg) {
        ToastUtils.show(msg);
    }

    /**
     * 最终的回调(UI线程)
     */
    default void onUIFinal(int id, boolean isCanceled) {
    }

    // //////////////////////////////////工作线程//////////////////////////////////

    /**
     * 预处理的回调(工作线程)
     */
    default void onWorkPreTreat(int id, boolean hasResponse) {
    }

    /**
     * 成功时的回调(工作线程)
     */
    default void onWorkSuccess(int id, Result result) {
    }

    /**
     * 失败时的回调(工作线程)
     */
    default void onWorkFailure(int id, boolean isCanceled, String msg) {
    }

    /**
     * 最终的回调(工作线程)
     */
    default void onWorkFinal(int id, boolean isCanceled) {
    }

    // //////////////////////////////////默认实现//////////////////////////////////

    interface Empty<Result> extends UICallback<Result> {
        @Override
        default void onUISuccess(int id, Result result) {
        }

        @Override
        default void onUIFailure(int id, boolean isCanceled, String msg) {
        }
    }
}
