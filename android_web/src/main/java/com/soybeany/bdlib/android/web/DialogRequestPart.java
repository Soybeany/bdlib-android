package com.soybeany.bdlib.android.web;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.util.HandlerThreadImpl;
import com.soybeany.bdlib.android.util.dialog.DialogKeyProvider;
import com.soybeany.bdlib.android.util.dialog.DialogMsg;
import com.soybeany.bdlib.core.util.storage.MessageCenter;
import com.soybeany.bdlib.web.okhttp.OkHttpUtils;

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

    public DialogRequestPart(OkHttpClient client) {
        super(client);
    }

    public DialogCall newDialogCall(@Nullable DialogKeyProvider provider, IRequestSupplier supplier) {
        return new DialogCall(provider, newCall(supplier));
    }

    @EverythingIsNonNull
    public static class DialogCall implements Call {
        @Nullable
        DialogKeyProvider mProvider;
        private Call mTarget;

        DialogCall(@Nullable DialogKeyProvider provider, Call call) {
            mProvider = provider;
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

        public <T> void enqueue(@NonNull DialogMsg msg, @NonNull OkHttpUICallback<T> callback) {
            // 若没有设置弹窗，则进行普通异步请求
            if (null == mProvider) {
                enqueue(callback);
                return;
            }

            // 显示弹窗、注册弹窗监听
            MessageCenter.notifyNow(mProvider.showMsgKey, msg);
            // 监听弹窗关闭的消息，取消请求任务
            MessageCenter.ICallback dialogCallback = reason -> mTarget.cancel();
            MessageCenter.register(HandlerThreadImpl.UI_THREAD, mProvider.onDismissDialogKey, dialogCallback);
            // 添加请求完成后关闭弹窗的回调
            callback.addNonUICallback(new UICallback.Empty<T>() {
                @Override
                public void onFinal(boolean isCanceled) {
                    callback.removeCallback(this);
                    // 关闭弹窗、注销弹窗监听
                    MessageCenter.unregister(dialogCallback);
                    MessageCenter.notifyNow(mProvider.popMsgKey, msg);
                }
            });
            // 异步请求
            enqueue(callback);
        }
    }
}
