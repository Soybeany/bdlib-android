package com.soybeany.bdlib.android.template.interfaces;

import android.arch.lifecycle.LifecycleObserver;

/**
 * 开发模板
 * <br>Created by Soybeany on 2019/2/1.
 */
public interface IDevTemplate {

    int setupLayoutResId();

    /**
     * 设置生命周期观察者
     */
    default LifecycleObserver[] setupObservers() {
        return null;
    }

    /**
     * 初始化ViewModel
     */
    default void onInitViewModels(IVMProvider provider) {
    }

    /**
     * 初始化视图
     */
    default void onInitViews() {
    }

    /**
     * 处理业务逻辑，如模拟点击、自动发起请求等
     *
     * @param isNew 此模板是否为新创建
     */
    default void doBusiness(boolean isNew) {
    }
}
