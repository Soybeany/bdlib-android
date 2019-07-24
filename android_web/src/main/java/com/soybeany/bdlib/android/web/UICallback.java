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
    default void onPreTreat(int id, int type) {
        onWorkPreTreat(id, type);
        MAIN_HANDLER.post(() -> onUIPreTreat(id, type));
    }

    @Override
    default void onSuccess(int id, Result result) {
        onWorkSuccess(id, result);
        MAIN_HANDLER.post(() -> onUISuccess(id, result));
    }

    @Override
    default void onFailure(int id, int type, String msg) {
        onWorkFailure(id, type, msg);
        MAIN_HANDLER.post(() -> onUIFailure(id, type, msg));
    }

    @Override
    default void onFinal(int id, int type) {
        onWorkFinal(id, type);
        MAIN_HANDLER.post(() -> onUIFinal(id, type));
    }

    // //////////////////////////////////UI线程//////////////////////////////////

    /**
     * 预处理的回调(UI线程)
     */
    default void onUIPreTreat(int id, int type) {
    }

    /**
     * 成功时的回调(UI线程)
     */
    void onUISuccess(int id, Result result);

    /**
     * 失败时的回调(UI线程)
     */
    default void onUIFailure(int id, int type, String msg) {
        ToastUtils.show(msg);
    }

    /**
     * 最终的回调(UI线程)
     */
    default void onUIFinal(int id, int type) {
    }

    // //////////////////////////////////工作线程//////////////////////////////////

    /**
     * 预处理的回调(工作线程)
     */
    default void onWorkPreTreat(int id, int type) {
    }

    /**
     * 成功时的回调(工作线程)
     */
    default void onWorkSuccess(int id, Result result) {
    }

    /**
     * 失败时的回调(工作线程)
     */
    default void onWorkFailure(int id, int type, String msg) {
    }

    /**
     * 最终的回调(工作线程)
     */
    default void onWorkFinal(int id, int type) {
    }

    // //////////////////////////////////默认实现//////////////////////////////////

    interface Empty<Result> extends UICallback<Result> {
        @Override
        default void onUISuccess(int id, Result result) {
        }

        @Override
        default void onUIFailure(int id, int type, String msg) {
        }
    }
}
