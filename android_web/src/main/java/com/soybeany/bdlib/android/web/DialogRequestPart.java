package com.soybeany.bdlib.android.web;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;

import com.soybeany.bdlib.android.util.HandlerThreadImpl;
import com.soybeany.bdlib.android.util.dialog.AbstractDialog;
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

import static com.soybeany.bdlib.android.util.BDContext.MAIN_HANDLER;

/**
 * <br>Created by Soybeany on 2019/4/11.
 */
public class DialogRequestPart extends OkHttpUtils.DefaultRequestPart {

    public DialogRequestPart(OkHttpClient client) {
        super(client);
    }

    public DialogCall newDialogCall(LifecycleOwner owner, AbstractDialog dialog, IRequestSupplier supplier) {
        return new DialogCall(new DialogProvider(owner, dialog), newCall(supplier));
    }

    @EverythingIsNonNull
    public static class DialogCall implements Call {
        private DialogProvider mProvider;
        private Call mTarget;

        DialogCall(DialogProvider provider, Call call) {
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

        public <T> void enqueue(@NonNull DialogMsg msg, @NonNull OkHttpSafeCallback<T> callback) {
            // 显示弹窗、注册弹窗监听
            MAIN_HANDLER.post(() -> mProvider.showMsg(msg));
            // 监听弹窗关闭的消息，取消请求任务
            MessageCenter.ICallback dialogCallback = reason -> mTarget.cancel();
            MessageCenter.register(HandlerThreadImpl.UI_THREAD, mProvider.getKeyProvider().getOnDismissDialogKey(), dialogCallback);
            // 添加请求完成后关闭弹窗的回调
            callback.addUnsafeCallback(new UICallback.Empty<T>() {
                @Override
                public void onFinal(boolean isCanceled) {
                    // 关闭弹窗、注销弹窗监听
                    MessageCenter.unregister(dialogCallback);
                    mProvider.popMsg(msg);
                }
            });
            // 异步请求
            mTarget.enqueue(callback);
        }
    }
}
