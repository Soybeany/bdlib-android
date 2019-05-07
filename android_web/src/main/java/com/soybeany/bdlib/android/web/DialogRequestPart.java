package com.soybeany.bdlib.android.web;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.web.okhttp.OkHttpUtils;
import com.soybeany.bdlib.web.okhttp.core.OkHttpCallback;
import com.soybeany.bdlib.web.okhttp.part.DefaultCall;

import java.util.LinkedList;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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
    public DialogCall newCall(Request request) {
        return new DialogCall(mInfo, super.newCall(request));
    }

    @EverythingIsNonNull
    public static class DialogCall extends DefaultCall {
        @Nullable
        private DialogInfo mInfo;
        @NonNull
        private Call mTarget;

        private DialogHelper mHelper;

        DialogCall(@Nullable DialogInfo info, @NonNull Call call) {
            super(call, new LinkedList<>());
            mTarget = call;
            mHelper = new DialogHelper(mInfo = info);
        }

        @Override
        @SuppressWarnings("MethodDoesntCallSuperMethod")
        public Call clone() {
            return new DialogCall(mInfo, super.clone());
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
