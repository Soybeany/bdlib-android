//package com.soybeany.bdlib.project;
//
//import android.support.annotation.Nullable;
//
//import com.soybeany.bdlib.android.web.auth.ReLoginInterceptor;
//import com.soybeany.bdlib.android.web.auth.ReLoginLogicSetter;
//import com.soybeany.bdlib.android.web.dialog.DialogClientPart;
//import com.soybeany.bdlib.web.okhttp.core.HandledException;
//import com.soybeany.bdlib.web.okhttp.core.OkHttpUtils;
//import com.soybeany.bdlib.web.okhttp.core.ParamAppender;
//
//import java.io.IOException;
//
//import okhttp3.Call;
//import okhttp3.Interceptor;
//import okhttp3.Request;
//import okhttp3.Response;
//
//import static com.soybeany.bdlib.web.okhttp.core.OkHttpUtils.IClientSetter.setupTimeout;
//
///**
// * <br>Created by Soybeany on 2019/4/11.
// */
//public class RequestUtils {
//    private static final String C_SERVER = "http://192.168.137.78:8080";
//    private static final String H_SERVER = "http://192.168.0.107:8080";
//    public static final String SERVER = C_SERVER;
//
//    private static final String KEY_GET_COOKIE = "set-cookie"; // 获取cookie的键
//    private static final String KEY_SET_COOKIE = "Cookie"; // 设置cookie的键
//
//    private static String COOKIE;
//
//    private static final Interceptor COOKIE_CACHE_INTERCEPTOR = chain -> {
//        Response response = chain.proceed(chain.request());
//        COOKIE = response.headers().get(KEY_GET_COOKIE);
//        return response;
//    }; // 缓存COOKIE的拦截器
//
//    private static final Interceptor COOKIE_SETUP_INTERCEPTOR = chain -> {
//        String accessToken = COOKIE;
//        Request request = chain.request();
//        if (null != accessToken) {
//            request = request.newBuilder().header(KEY_SET_COOKIE, accessToken).build();
//        }
//        return chain.proceed(request);
//    }; // 设置COOKIE的拦截器
//
//    private static final OkHttpUtils.IClientSetter DEFAULT_SETTER = builder -> {
//        builder.addInterceptor(COOKIE_SETUP_INTERCEPTOR);
//        builder.addInterceptor(new ReAuthInterceptor());
//    };
//
//    static {
//        // 设置全局客户端
//        OkHttpUtils.setupPrototypeClient(builder -> {
//            setupTimeout(builder, 10); // 设置默认超时
//        });
//    }
//
//    /**
//     * 获得登录Call
//     */
//    public static Call getLoginCall(String uid, String pwd) {
//        return newClientPart(null).addSetter(builder -> builder.addInterceptor(COOKIE_CACHE_INTERCEPTOR))
//                .newNotifierRequest().newCall((builder, notifier) -> builder.url(SERVER + "/mobile/auth/login").post(new ParamAppender().add("uid", uid).add("pwd", pwd).toNewFormBody().build()).build());
//    }
//
////    public static Call newCall(@Nullable OkHttpClientFactory.IClientSetter setter, OkHttpNotifierUtils.IRequestGetter requestGetter) {
////        newRequest(setter).newCall(requestGetter, new ReLoginConnectWrapper(new DialogConnectSetter(null, null), null));
////    }
//
//    /**
//     * 常规客户端(自动附带COOKIE信息)
//     */
//    public static DialogClientPart.DialogRequestPart newRequest(@Nullable OkHttpUtils.IClientSetter setter) {
//        return newClientPart(setter).addSetter(DEFAULT_SETTER).addLogic(new ReLoginLogicSetter()).newNotifierRequest();
//    }
//
//    /**
//     * 空白客户端(不带额外设置)
//     */
//    public static DialogClientPart.DialogRequestPart newEmptyClient(@Nullable OkHttpUtils.IClientSetter setter) {
//        return newClientPart(setter).newNotifierRequest();
//    }
//
//    private static DialogClientPart newClientPart(@Nullable OkHttpUtils.IClientSetter setter) {
//        return new DialogClientPart().addSetter(setter);
//    }
//
//    private static class ReAuthInterceptor extends ReLoginInterceptor {
//        @Override
//        protected Call onAuth() throws IOException {
//            String uid = "Soybeany", pwd = "123123";
////                    String uid = AppConfig.user(KEY_USER_LOGIN_UID), pwd = AppConfig.user(KEY_USER_LOGIN_PWD);
//            if (null == uid || null == pwd) {
//                throw new HandledException("用户信息已失效，请重新登录");
//            }
//            return getLoginCall(uid, pwd);
//        }
//
//        //        @Override
////        protected Response onReRequest(Call call) throws IOException {
////            return simpleClient(null, b -> {
////                b.addInterceptor(COOKIE_SETUP_INTERCEPTOR);
////            }).newCall(() -> OkHttpRequestFactory.get(call.request().url().toString()).build()).execute();
////        }
//
//        @Override
//        protected boolean isResponseValid(Response response) {
//            return !response.request().url().toString().endsWith("/mobile/auth/invalid");
//        }
//    }
//}
