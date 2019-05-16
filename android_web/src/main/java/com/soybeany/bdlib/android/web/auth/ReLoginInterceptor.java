package com.soybeany.bdlib.android.web.auth;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.web.R;
import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.util.notify.IOnCallDealer;
import com.soybeany.bdlib.core.util.notify.Notifier;
import com.soybeany.bdlib.web.okhttp.core.HandledException;
import com.soybeany.bdlib.web.okhttp.notify.NotifyCall;
import com.soybeany.bdlib.web.okhttp.notify.RequestCallbackMsg;
import com.soybeany.bdlib.web.okhttp.notify.RequestInvokerMsg;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

import static com.soybeany.bdlib.android.util.BDContext.getString;

/**
 * <br>Created by Soybeany on 2019/4/15.
 */
public abstract class ReLoginInterceptor extends AuthInterceptor {

    public static final String TYPE_ON_RE_AUTH_START = "onReAuthStart";
    public static final String TYPE_ON_RE_AUTH_FINISH = "onReAuthFinish";
    public static final String TYPE_ON_RE_REQUEST_START = "onReRequestStart";
    public static final String TYPE_ON_RE_REQUEST_FINISH = "onReRequestFinish";

    private RequestCallbackMsg mMsg = new RequestCallbackMsg();

    @Override
    protected Response onInvalid(int retryTimes, @NonNull Chain chain) throws IOException {
        // 判断已重试次数
        if (retryTimes > 1) {
            throw new HandledException(getOutOfRetryTimesHint());
        }
        // 提取通知键
        Notifier<RequestInvokerMsg, RequestCallbackMsg> notifier = chain.call() instanceof NotifyCall ? ((NotifyCall) chain.call()).getNotifier() : null;
        // 尝试重登录
        Response authResponse = execute(notifier, TYPE_ON_RE_AUTH_START, TYPE_ON_RE_AUTH_FINISH, this::onAuth, call -> {
            Response response = call.execute();
            return !response.isSuccessful() ? response : null;
        }, this::onAuthException);
        // null代表正常，否则为登录异常
        if (null != authResponse) {
            return authResponse;
        }
        // 再次请求并返回新响应
        return execute(notifier, TYPE_ON_RE_REQUEST_START, TYPE_ON_RE_REQUEST_FINISH, () -> chain.call().clone(), this::onReRequest, this::onReRequestException);
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

    private Response execute(@Nullable Notifier<RequestInvokerMsg, RequestCallbackMsg> notifier, String startType, String finishType, ICGetter cGetter, IRGetter rGetter, IEGetter eGetter) throws IOException {
        IOnCallDealer cancelDealer = null;
        try {
            Call call = cGetter.getCall();
            Optional.ofNullable(notifier).ifPresent(n -> n.callback().notifyNow(mMsg.type(startType)));
            // 监听请求的取消
            cancelDealer = data -> RequestInvokerMsg.invokeOnCancel(data, call::cancel);
            if (null != notifier) {
                notifier.invoker().addDealer(cancelDealer);
            }
            return rGetter.getResponse(call);
        } catch (IOException e) {
            throw eGetter.getException(e);
        } finally {
            if (null != notifier) {
                Optional.ofNullable(cancelDealer).ifPresent(notifier.invoker()::removeDealer);
                notifier.callback().notifyNow(mMsg.type(finishType));
            }
        }
    }

    // //////////////////////////////////实现区//////////////////////////////////

    protected abstract Call onAuth() throws IOException;

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
