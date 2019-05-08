package com.soybeany.bdlib.android.web;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.web.okhttp.OkHttpUtils;
import com.soybeany.bdlib.web.okhttp.core.NotifyRequest;
import com.soybeany.bdlib.web.okhttp.core.OkHttpCallback;
import com.soybeany.bdlib.web.okhttp.part.NotifyCall;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.internal.annotations.EverythingIsNonNull;

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
    public NotifyCall newCall(NotifyRequest request) {
        return new DialogCall(mInfo, super.newCall(request), request.key);
    }

    @EverythingIsNonNull
    public static class DialogCall extends NotifyCall {
        @Nullable
        private DialogInfo mInfo;
        @NonNull
        private Call mTarget;
        private String mNotifyKey;
        private DialogHelper mHelper;

        DialogCall(@Nullable DialogInfo info, @NonNull Call call, String notifyKey) {
            super(call, notifyKey);
            mTarget = call;
            mNotifyKey = notifyKey;
            mHelper = new DialogHelper(mInfo = info);
        }

        @Override
        public Call clone() {
            return new DialogCall(mInfo, cloneTarget(), mNotifyKey);
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
