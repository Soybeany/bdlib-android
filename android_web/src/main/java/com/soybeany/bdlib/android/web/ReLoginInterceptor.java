package com.soybeany.bdlib.android.web;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.util.StdHintUtils;
import com.soybeany.bdlib.android.util.dialog.DialogKeyProvider;
import com.soybeany.bdlib.android.util.dialog.DialogMsg;
import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.java8.function.Consumer;
import com.soybeany.bdlib.core.util.storage.MessageCenter;
import com.soybeany.bdlib.web.okhttp.core.HandledException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * <br>Created by Soybeany on 2019/4/15.
 */
public abstract class ReLoginInterceptor extends AuthInterceptor {
    @Nullable
    private DialogInfo mInfo;
    @NonNull
    private ICallback mCallback;

    public ReLoginInterceptor(@Nullable DialogInfo info, @NonNull ICallback callback) {
        mInfo = info;
        mCallback = callback;
    }

    @Override
    protected Response onInvalid(@NonNull Chain chain) throws IOException {
        DialogMsg oldMsg = (null != mInfo ? mInfo.msg : null);
        // 尝试重登录
        DialogMsg authMsg = mCallback.getAuthMsg(oldMsg);
        try {
            showMsg(authMsg);
            Response reLogin = mCallback.onAuth();
            if (!reLogin.isSuccessful()) {
                return reLogin;
            }
        } catch (IOException e) {
            throw mCallback.onAuthException(e);
        } finally {
            popMsg(oldMsg, authMsg);
        }
        // 再次请求并返回新响应
        DialogMsg reRequestMsg = mCallback.getReRequestMsg(oldMsg);
        try {
            showMsg(reRequestMsg);
            return mCallback.onReRequest(chain.call());
        } catch (IOException e) {
            throw mCallback.onReRequestException(e);
        } finally {
            popMsg(oldMsg, reRequestMsg);
        }
    }

    private void showMsg(DialogMsg msg) {
        invoke(provider -> MessageCenter.notifyNow(provider.showMsgKey, msg));
    }

    private void popMsg(DialogMsg oldMsg, DialogMsg curMsg) {
        invoke(provider -> {
            if (oldMsg != curMsg) {
                MessageCenter.notifyNow(provider.popMsgKey, curMsg);
            }
        });
    }

    private void invoke(Consumer<DialogKeyProvider> consumer) {
        Optional.ofNullable(mInfo).map(info -> info.provider).ifPresent(consumer);
    }

    public interface ICallback {
        default DialogMsg getAuthMsg(DialogMsg oldMsg) {
            return new DialogMsg(StdHintUtils.STD_RE_LOGIN_HINT).cancelable(oldMsg.isCancelable()).multiHint((hint, count, cancelable) -> hint);
        }

        Response onAuth() throws IOException;

        default IOException onAuthException(IOException e) {
            return e instanceof HandledException ? e : new IOException("重登录异常:" + e.getMessage());
        }

        default DialogMsg getReRequestMsg(DialogMsg oldMsg) {
            return oldMsg;
        }

        default Response onReRequest(Call call) throws IOException {
            return call.execute();
        }

        default IOException onReRequestException(IOException e) {
            return e instanceof HandledException ? e : new IOException("重请求异常:" + e.getMessage());
        }
    }
}
