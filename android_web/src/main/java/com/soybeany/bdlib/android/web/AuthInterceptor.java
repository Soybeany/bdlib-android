package com.soybeany.bdlib.android.web;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 授权拦截器(建议使用{@link okhttp3.OkHttpClient.Builder#addInterceptor(Interceptor)}进行添加)
 * <br>Created by Soybeany on 2019/4/9.
 */
public abstract class AuthInterceptor implements Interceptor {
    private int mRetryTimes;

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        // 响应有效则不作处理
        try {
            if (isResponseValid(response)) {
                return response;
            }
        } catch (RuntimeException e) {
            response.close();
            throw e;
        }
        // 响应无效则先关闭先前响应
        response.close();
        return onInvalid(++mRetryTimes, chain);
    }

    /**
     * 重置重试次数
     */
    public void resetRetryTimes() {
        mRetryTimes = 0;
    }

    protected abstract boolean isResponseValid(Response response);

    protected abstract Response onInvalid(int retryTimes, @NonNull Chain chain) throws IOException;
}
