package com.soybeany.bdlib.android.web;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.util.HandlerThreadImpl;
import com.soybeany.bdlib.android.util.dialog.DialogKeyProvider;
import com.soybeany.bdlib.android.util.dialog.DialogMsg;
import com.soybeany.bdlib.core.util.storage.MessageCenter;
import com.soybeany.bdlib.web.okhttp.OkHttpUtils;
import com.soybeany.bdlib.web.okhttp.core.OkHttpCallback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.annotations.EverythingIsNonNull;
import okio.Timeout;

/**
 * <br>Created by Soybeany on 2019/4/11.
 */
public class DialogRequestPart extends OkHttpUtils.DefaultRequestPart {
    private DialogInfo mInfo;

    public DialogRequestPart(@Nullable DialogInfo info, OkHttpClient client) {
        super(client);
        mInfo = info;
    }

    @Override
    public DialogCall newCall(IRequestSupplier supplier) {
        return new DialogCall(mInfo, super.newCall(supplier));
    }

    @EverythingIsNonNull
    public static class DialogCall implements Call {
        @Nullable
        private DialogKeyProvider mProvider;
        @Nullable
        private DialogMsg mMsg;
        private Call mTarget;

        DialogCall(@Nullable DialogInfo info, Call call) {
            if (null != info) {
                mProvider = info.provider;
                mMsg = info.msg;
            }
            mTarget = call;
        }

        @Override
        public Request request() {
            return mTarget.request();
        }

        @Override
        public Response execute() throws IOException {
            return mTarget.execute();
        }

        @Override
        public void enqueue(Callback responseCallback) {
            mTarget.enqueue(responseCallback);
        }

        @Override
        public void cancel() {
            mTarget.cancel();
        }

        @Override
        public boolean isExecuted() {
            return mTarget.isExecuted();
        }

        @Override
        public boolean isCanceled() {
            return mTarget.isCanceled();
        }

        @Override
        public Timeout timeout() {
            return mTarget.timeout();
        }

        @Override
        public Call clone() {
            try {
                return (Call) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        public <T> void enqueue(@NonNull OkHttpCallback<T> callback) {
            // 若没有设置弹窗，则进行普通异步请求
            if (null == mProvider) {
                mTarget.enqueue(callback);
                return;
            }

            // 显示弹窗、注册弹窗监听
            MessageCenter.notifyNow(mProvider.showMsgKey, mMsg);
            // 监听弹窗关闭的消息，取消请求任务
            MessageCenter.ICallback dialogCallback = reason -> mTarget.cancel();
            MessageCenter.register(HandlerThreadImpl.UI_THREAD, mProvider.onDismissDialogKey, dialogCallback);
            // 添加请求完成后关闭弹窗的回调
            callback.addCallback(new UICallback.Empty<T>() {
                @Override
                public void onFinal(boolean isCanceled) {
                    callback.removeCallback(this);
                    // 关闭弹窗、注销弹窗监听
                    MessageCenter.unregister(dialogCallback);
                    MessageCenter.notifyNow(mProvider.popMsgKey, mMsg);
                }
            });
            // 异步请求
            mTarget.enqueue(callback);
        }
    }
}
