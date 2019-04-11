package com.soybeany.bdlib.android.web;


import com.soybeany.bdlib.web.okhttp.core.ICallback;

import static com.soybeany.bdlib.android.util.BDContext.MAIN_HANDLER;

/**
 * <br>Created by Soybeany on 2019/3/7.
 */
public interface UICallback<Result> extends ICallback<Result> {

    @Override
    default void onSuccess(Result result) {
        MAIN_HANDLER.post(() -> onUISuccess(result));
    }

    @Override
    default void onFailure(boolean isCanceled, String msg) {
        MAIN_HANDLER.post(() -> onUIFailure(isCanceled, msg));
    }

    @Override
    default void onFinal(boolean isCanceled) {
        MAIN_HANDLER.post(() -> onUIFinal(isCanceled));
    }

    /**
     * 成功时的回调(UI线程)
     */
    void onUISuccess(Result result);

    /**
     * 失败时的回调(UI线程)
     */
    void onUIFailure(boolean isCanceled, String msg);

    /**
     * 最终的回调(UI线程)
     */
    default void onUIFinal(boolean isCanceled) {
    }

    interface Empty<Result> extends UICallback<Result> {
        @Override
        default void onUISuccess(Result result) {
        }

        @Override
        default void onUIFailure(boolean isCanceled, String msg) {
        }
    }
}
