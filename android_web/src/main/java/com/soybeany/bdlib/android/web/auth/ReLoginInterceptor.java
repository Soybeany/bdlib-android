package com.soybeany.bdlib.android.web.auth;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.web.R;
import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.util.notify.IOnCallListener;
import com.soybeany.bdlib.web.okhttp.core.HandledException;
import com.soybeany.bdlib.web.okhttp.notify.NotifierCall;
import com.soybeany.bdlib.web.okhttp.notify.RequestNotifier;
import com.soybeany.bdlib.web.okhttp.notify.RequestNotifierMsg;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

import static com.soybeany.bdlib.android.util.BDContext.getString;

/**
 * <br>Created by Soybeany on 2019/4/15.
 */
public abstract class ReLoginInterceptor extends AuthInterceptor {

    @Override
    protected Response onInvalid(int retryTimes, @NonNull Chain chain) throws IOException {
        // 判断已重试次数
        if (retryTimes > 1) {
            throw new HandledException(getOutOfRetryTimesHint());
        }
        // 提取通知键
        RequestNotifier notifier = chain.call() instanceof NotifierCall ? ((NotifierCall) chain.call()).getNotifier() : null;
        // 尝试重登录
        Response authResponse = execute(notifier, new OnReAuthStart(), new OnReAuthFinish(), this::onAuth, call -> {
            Response response = call.execute();
            return !response.isSuccessful() ? response : null;
        }, this::onAuthException);
        // null代表正常，否则为登录异常
        if (null != authResponse) {
            return authResponse;
        }
        // 再次请求并返回新响应
        return execute(notifier, new OnReRequestStart(), new OnReRequestFinish(), () -> chain.call().clone(), this::onReRequest, this::onReRequestException);
    }

    // //////////////////////////////////重写区//////////////////////////////////

    protected String getOutOfRetryTimesHint() {
        return getString(R.string.bd_out_of_retry_times);
    }

    protected IOException onAuthException(IOException e) {
        return e instanceof HandledException ? e : new IOException(getString(R.string.bd_re_login_exception) + ":" + e.getMessage());
    }

    protected Response onReRequest(Call call) throws IOException {
        return call.execute();
    }

    protected IOException onReRequestException(IOException e) {
        return e instanceof HandledException ? e : new IOException(getString(R.string.bd_re_request_exception) + ":" + e.getMessage());
    }

    // //////////////////////////////////内部区//////////////////////////////////

    private Response execute(@Nullable RequestNotifier notifier, RequestNotifierMsg.Callback startMsg, RequestNotifierMsg.Callback finishMsg, ICGetter cGetter, IRGetter rGetter, IEGetter eGetter) throws IOException {
        IOnCallListener cancelListener = null;
        try {
            Call call = cGetter.getCall();
            Optional.ofNullable(notifier).ifPresent(n -> n.callback().notifyNow(startMsg));
            // 监听请求的取消
            cancelListener = data -> RequestNotifierMsg.invokeWhenCancel(data, call::cancel);
            if (null != notifier) {
                notifier.invoker().addListener(cancelListener);
            }
            return rGetter.getResponse(call);
        } catch (IOException e) {
            throw eGetter.getException(e);
        } finally {
            if (null != notifier) {
                Optional.ofNullable(cancelListener).ifPresent(notifier.invoker()::removeListener);
                notifier.callback().notifyNow(finishMsg);
            }
        }
    }

    // //////////////////////////////////实现区//////////////////////////////////

    protected abstract Call onAuth() throws IOException;

    // //////////////////////////////////内部类//////////////////////////////////

    public static class OnReAuthStart extends RequestNotifierMsg.Callback<Object> {
        OnReAuthStart() {
            super(null);
        }
    }

    public static class OnReAuthFinish extends RequestNotifierMsg.Callback<Object> {
        OnReAuthFinish() {
            super(null);
        }
    }

    public static class OnReRequestStart extends RequestNotifierMsg.Callback<Object> {
        OnReRequestStart() {
            super(null);
        }
    }

    public static class OnReRequestFinish extends RequestNotifierMsg.Callback<Object> {
        OnReRequestFinish() {
            super(null);
        }
    }

    // //////////////////////////////////接口区//////////////////////////////////

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
