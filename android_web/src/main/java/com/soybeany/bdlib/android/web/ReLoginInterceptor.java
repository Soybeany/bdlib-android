package com.soybeany.bdlib.android.web;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.util.StdHintUtils;
import com.soybeany.bdlib.android.util.dialog.DialogKeyProvider;
import com.soybeany.bdlib.android.util.dialog.DialogMsg;
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

    public ReLoginInterceptor(@Nullable DialogInfo info) {
        mInfo = info;
    }

    @Override
    protected Response onInvalid(@NonNull Chain chain) throws IOException {
        // 尝试重登录
        Response authResponse = execute(this::getAuthMsg, this::onAuth, call -> {
            Response response = call.execute();
            return !response.isSuccessful() ? response : null;
        }, this::onAuthException);
        // null代表正常，否则为登录异常
        if (null != authResponse) {
            return authResponse;
        }
        // 再次请求并返回新响应
        return execute(this::getReRequestMsg, () -> chain.call().clone(), this::onReRequest, this::onReRequestException);
    }

    // //////////////////////////////////重写区//////////////////////////////////

    protected DialogMsg getAuthMsg(DialogMsg oldMsg) {
        return new DialogMsg(StdHintUtils.STD_RE_LOGIN_HINT).cancelable(oldMsg.isCancelable()).multiHint((hint, count, cancelable) -> hint);
    }

    protected IOException onAuthException(IOException e) {
        return e instanceof HandledException ? e : new IOException("重登录异常:" + e.getMessage());
    }

    protected Response onReRequest(Call call) throws IOException {
        return call.execute();
    }

    protected DialogMsg getReRequestMsg(DialogMsg oldMsg) {
        return oldMsg;
    }

    protected IOException onReRequestException(IOException e) {
        return e instanceof HandledException ? e : new IOException("重请求异常:" + e.getMessage());
    }

    // //////////////////////////////////内部区//////////////////////////////////

    private Response execute(IMGetter mGetter, ICGetter cGetter, IRGetter rGetter, IEGetter eGetter) throws IOException {
        // 赋值
        DialogKeyProvider provider = (null != mInfo) ? mInfo.provider : null;
        DialogMsg oldMsg = (null != mInfo ? mInfo.msg : null);
        DialogMsg newMsg = mGetter.getMsg(oldMsg);
        DialogHelper helper = new DialogHelper(provider, newMsg);
        // 操作
        try {
            Call call = cGetter.getCall();
            helper.showMsg(call);
            return rGetter.getResponse(call);
        } catch (IOException e) {
            throw eGetter.getException(e);
        } finally {
            if (oldMsg != newMsg) {
                helper.popMsg();
            }
        }
    }

    // //////////////////////////////////实现区//////////////////////////////////

    protected abstract Call onAuth() throws IOException;

    // //////////////////////////////////接口区//////////////////////////////////

    private interface IMGetter {
        DialogMsg getMsg(DialogMsg msg);
    }

    private interface ICGetter {
        Call getCall() throws IOException;
    }

    private interface IRGetter {
        Response getResponse(Call call) throws IOException;
    }

    private interface IEGetter {
        IOException getException(IOException e);
    }
}
