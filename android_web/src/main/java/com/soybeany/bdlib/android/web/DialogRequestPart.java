package com.soybeany.bdlib.android.web;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
        private DialogInfo mInfo;
        @NonNull
        private Call mTarget;

        private DialogHelper mHelper;

        DialogCall(@Nullable DialogInfo info, @NonNull Call call) {
            mTarget = call;
            mHelper = new DialogHelper(mInfo = info);
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
        @SuppressWarnings("MethodDoesntCallSuperMethod")
        public Call clone() {
            return new DialogCall(mInfo, mTarget.clone());
        }

        public <T> void enqueue(@NonNull OkHttpCallback<T> callback) {
            // 若没有设置弹窗或信息，则进行普通异步请求
            if (!mHelper.showMsg(mTarget)) {
                mTarget.enqueue(callback);
                return;
            }

            // 添加请求完成后关闭弹窗的回调
            callback.addCallback(new UICallback.Empty<T>() {
                @Override
                public void onFinal(boolean isCanceled) {
                    callback.removeCallback(this);
                    mHelper.popMsg(true);
                }
            });
            // 异步请求
            mTarget.enqueue(callback);
        }
    }
}
