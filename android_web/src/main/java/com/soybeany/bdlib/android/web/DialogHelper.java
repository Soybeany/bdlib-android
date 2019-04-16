package com.soybeany.bdlib.android.web;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.util.HandlerThreadImpl;
import com.soybeany.bdlib.android.util.dialog.DialogKeyProvider;
import com.soybeany.bdlib.android.util.dialog.DialogMsg;
import com.soybeany.bdlib.core.util.storage.MessageCenter;

import okhttp3.Call;

/**
 * <br>Created by Soybeany on 2019/4/16.
 */
class DialogHelper {
    @Nullable
    private DialogKeyProvider mProvider;
    @Nullable
    private DialogMsg mMsg;

    @Nullable
    private MessageCenter.ICallback mDialogCallback;

    DialogHelper(@Nullable DialogInfo info) {
        if (null != info) {
            mProvider = info.provider;
            mMsg = info.msg;
        }
    }

    DialogHelper(@Nullable DialogKeyProvider provider, @Nullable DialogMsg msg) {
        mProvider = provider;
        mMsg = msg;
    }

    /**
     * @return 是否有显示弹窗
     */
    public boolean showMsg(@NonNull Call call) {
        if (null != mDialogCallback) {
            return false;
        }
        mDialogCallback = reason -> call.cancel();
        return invoke((provider, msg) -> {
            // 显示弹窗、注册弹窗监听
            MessageCenter.notifyNow(provider.showMsgKey, msg);
            // 监听弹窗关闭的消息，取消请求任务
            MessageCenter.register(HandlerThreadImpl.UI_THREAD, provider.onDismissDialogKey, mDialogCallback);
        });
    }

    public void popMsg() {
        if (null == mDialogCallback) {
            return;
        }
        invoke((provider, msg) -> {
            // 关闭弹窗、注销弹窗监听
            MessageCenter.unregister(mDialogCallback);
            MessageCenter.notifyNow(provider.popMsgKey, msg);
        });
        mDialogCallback = null;
    }

    private boolean invoke(ICallback callback) {
        if (null != mMsg && null != mProvider) {
            callback.onNotNull(mProvider, mMsg);
            return true;
        }
        return false;
    }

    private interface ICallback {
        void onNotNull(@NonNull DialogKeyProvider provider, @NonNull DialogMsg msg);
    }
}
