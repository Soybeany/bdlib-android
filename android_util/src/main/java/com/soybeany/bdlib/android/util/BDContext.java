package com.soybeany.bdlib.android.util;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Handler;
import android.support.annotation.StringRes;
import android.util.TypedValue;

import com.soybeany.bdlib.core.java8.Optional;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 应用上下文，供没有界面但需要上下文的类使用（一般为类库内部使用）
 * <br>Created by Soybeany on 2017/1/20.
 */
public class BDContext {

    public static final Handler MAIN_HANDLER = new Handler();
    public static final ExecutorService WORK_THREADS = Executors.newCachedThreadPool();

    public static final Map<String, Object> GLOBAL_DATA = new ConcurrentHashMap<>(); // 缓存应用数据的映射

    private static final String KEY_APPLICATION_CONTEXT = "application_context"; // 应用的上下文

    private BDContext() {

    }

    public static void init(Application application) {
        GLOBAL_DATA.put(KEY_APPLICATION_CONTEXT, application);
    }

    /**
     * 获得应用上下文
     */
    public static Context getContext() {
        return (Context) Optional.ofNullable(GLOBAL_DATA.get(KEY_APPLICATION_CONTEXT)).orElseThrow(() -> new RuntimeException("需要先设置Application"));
    }

    /**
     * 获得唯一码
     */
    public static String getUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获得应用的资源
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 获得字符串
     */
    public static String getString(@StringRes int resId) {
        return getResources().getString(resId);
    }

    /**
     * 获得资源管理器
     */
    public static AssetManager getAssets() {
        return getContext().getAssets();
    }

    /**
     * 获得资源的像素值
     */
    public static int getPixel(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    /**
     * 获得应用主色调
     *
     * @return 颜色值
     */
    public static int getColorPrimary() {
        return getAttributeValue(R.attr.colorPrimary).data;
    }

    /**
     * 获得应用当前主题下的资源值(非引用)
     */
    public static TypedValue getAttributeValue(int resId) {
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(resId, typedValue, true);
        return typedValue;
    }
}
