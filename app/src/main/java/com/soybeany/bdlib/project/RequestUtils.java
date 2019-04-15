package com.soybeany.bdlib.project;

import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.web.DialogRequestPart;
import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.web.okhttp.core.OkHttpClientFactory;


/**
 * <br>Created by Soybeany on 2019/4/11.
 */
public class RequestUtils {
    private static final String KEY_GET_COOKIE = "set-cookie"; // 获取cookie的键
    private static final String KEY_SET_COOKIE = "Cookie"; // 设置cookie的键

    static {
        // 设置全局客户端
        OkHttpClientFactory.setupDefaultClient(builder -> {
            OkHttpClientFactory.setupTimeout(builder, 10); // 设置默认超时
        });
    }

    /**
     * 登录
     */
    public static void login() {

    }

    /**
     * 常规客户端(自动附带COOKIE信息)
     */
    public static DialogRequestPart client(@Nullable OkHttpClientFactory.IClientSetter setter) {
        return simpleClient(builder -> {
            Optional.ofNullable(setter).ifPresent(s -> s.onSetup(builder));
        });
    }

    /**
     * 最简单的客户端
     */
    public static DialogRequestPart simpleClient(OkHttpClientFactory.IClientSetter setter) {
        return new DialogRequestPart(OkHttpClientFactory.getClient(setter));
    }
}
