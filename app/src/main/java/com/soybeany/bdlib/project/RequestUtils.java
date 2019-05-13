package com.soybeany.bdlib.project;

import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.web.auth.ReLoginCallDealer;
import com.soybeany.bdlib.android.web.auth.ReLoginInterceptor;
import com.soybeany.bdlib.android.web.dialog.DialogClientPart;
import com.soybeany.bdlib.web.okhttp.core.HandledException;
import com.soybeany.bdlib.web.okhttp.core.OkHttpClientFactory;
import com.soybeany.bdlib.web.okhttp.core.OkHttpRequestFactory;
import com.soybeany.bdlib.web.okhttp.notify.NotifyCall;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <br>Created by Soybeany on 2019/4/11.
 */
public class RequestUtils {
    private static final String C_SERVER = "http://192.168.137.232:8080";
    private static final String H_SERVER = "http://192.168.0.107:8080";
    public static final String SERVER = H_SERVER;

    private static final String KEY_GET_COOKIE = "set-cookie"; // 获取cookie的键
    private static final String KEY_SET_COOKIE = "Cookie"; // 设置cookie的键

    private static String COOKIE;

    private static final Interceptor COOKIE_CACHE_INTERCEPTOR = chain -> {
        Response response = chain.proceed(chain.request());
        COOKIE = response.headers().get(KEY_GET_COOKIE);
        return response;
    }; // 缓存COOKIE的拦截器

    private static final Interceptor COOKIE_SETUP_INTERCEPTOR = chain -> {
        String accessToken = COOKIE;
        Request request = chain.request();
        if (null != accessToken) {
            request = request.newBuilder().header(KEY_SET_COOKIE, accessToken).build();
        }
        return chain.proceed(request);
    }; // 设置COOKIE的拦截器

    private static final OkHttpClientFactory.IClientSetter DEFAULT_SETTER = builder -> {
        builder.addInterceptor(COOKIE_SETUP_INTERCEPTOR);
        builder.addInterceptor(new ReAuthInterceptor());
    };

    static {
        // 设置全局客户端
        OkHttpClientFactory.setupDefaultClient(builder -> {
            OkHttpClientFactory.setupTimeout(builder, 5); // 设置默认超时
        });
    }

    /**
     * 获得登录Call
     */
    public static NotifyCall getLoginCall(String uid, String pwd) {
        return newClientPart(null).addSetter(builder -> builder.addInterceptor(COOKIE_CACHE_INTERCEPTOR))
                .newRequest().newCall(requestNotifier -> OkHttpRequestFactory.postForm(SERVER + "/mobile/auth/login").param("uid", uid).param("pwd", pwd).build(requestNotifier));
    }

    /**
     * 常规客户端(自动附带COOKIE信息)
     */
    public static DialogClientPart.DialogRequestPart newClient(@Nullable OkHttpClientFactory.IClientSetter setter) {
        return newClientPart(setter).addSetter(DEFAULT_SETTER).newRequest().configRequestPart(s -> s.add(new ReLoginCallDealer()));
    }

    /**
     * 空白客户端(不带额外设置)
     */
    public static DialogClientPart.DialogRequestPart newEmptyClient(@Nullable OkHttpClientFactory.IClientSetter setter) {
        return newClientPart(setter).newRequest();
    }

    private static DialogClientPart newClientPart(@Nullable OkHttpClientFactory.IClientSetter setter) {
        return new DialogClientPart().addSetter(setter);
    }

    private static class ReAuthInterceptor extends ReLoginInterceptor {
        @Override
        protected Call onAuth() throws IOException {
            String uid = "Soybeany", pwd = "123123";
//                    String uid = AppConfig.user(KEY_USER_LOGIN_UID), pwd = AppConfig.user(KEY_USER_LOGIN_PWD);
            if (null == uid || null == pwd) {
                throw new HandledException("用户信息已失效，请重新登录");
            }
            return getLoginCall(uid, pwd);
        }

        //        @Override
//        protected Response onReRequest(Call call) throws IOException {
//            return simpleClient(null, b -> {
//                b.addInterceptor(COOKIE_SETUP_INTERCEPTOR);
//            }).newCall(() -> OkHttpRequestFactory.get(call.request().url().toString()).build()).execute();
//        }

        @Override
        protected boolean isResponseValid(Response response) {
            return !response.request().url().toString().endsWith("/mobile/auth/invalid");
        }
    }
}
