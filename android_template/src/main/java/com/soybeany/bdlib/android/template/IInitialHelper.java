package com.soybeany.bdlib.android.template;

import android.arch.lifecycle.LifecycleObserver;

/**
 * <br>Created by Soybeany on 2019/2/1.
 */
public interface IInitialHelper {

    int setupLayoutResId();

    default LifecycleObserver[] setupObservers() {
        return null;
    }

    /**
     * 初始化视图、ViewModel等
     */
    default void onInit() {
    }

    /**
     * 处理业务逻辑，如模拟点击、自动发起请求等
     */
    default void doBusiness() {
    }
}
