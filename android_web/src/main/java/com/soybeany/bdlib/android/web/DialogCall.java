package com.soybeany.bdlib.android.web;

import android.support.annotation.NonNull;

import com.soybeany.bdlib.android.util.HandlerThreadImpl;
import com.soybeany.bdlib.android.util.dialog.AbstractDialog;
import com.soybeany.bdlib.android.util.dialog.DialogMsg;
import com.soybeany.bdlib.core.util.thread.MessageCenter;
import com.soybeany.bdlib.web.okhttp.OkHttpCallback;

import okhttp3.Call;

/**
 * 带弹窗的Call(主线程中使用)
 * <br>Created by Soybeany on 2019/4/9.
 */
public class DialogCall {
    @NonNull
    private AbstractDialog mDialog;
    @NonNull
    private Call mTarget;

    public DialogCall(@NonNull AbstractDialog dialog, @NonNull Call target) {
        mDialog = dialog;
        mTarget = target;
    }

    public <T> void enqueue(@NonNull DialogMsg msg, @NonNull OkHttpCallback<T> callback) {
        // 显示弹窗、注册弹窗监听
        mDialog.show(msg);
        MessageCenter.ICallback dialogCallback = reason -> mTarget.cancel();
        MessageCenter.register(HandlerThreadImpl.UI_THREAD, mDialog.getMsgCenterKey(), dialogCallback);
        callback.addCallback(new UICallback.Empty<T>() {
            @Override
            public void onFinal(boolean isCanceled) {
                // 关闭弹窗、注销弹窗监听
                MessageCenter.unregister(dialogCallback);
                mDialog.dismiss(msg);
            }
        });
        // 异步请求
        mTarget.enqueue(callback);
    }

}
