package com.soybeany.bdlib.android.mvp;


import com.soybeany.bdlib.core.util.storage.KeyValueStorage;

import java.util.HashMap;
import java.util.Map;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;

/**
 * Presenter工具类
 * <br>Created by Soybeany on 2019/7/29.
 */
public class PresenterUtils {
    private static final KeyValueStorage<Class<?>, Map<String, BasePresenter<?>>> PRESENTER_STORAGE = new KeyValueStorage<>();

    /**
     * 获得Presenter(受生命周期管理)
     *
     * @param lifecycle 视图的生命周期
     */
    public static <V extends IPresenterView, T extends BasePresenter<V>> T get(ViewModelProvider provider, Class<T> clazz, Lifecycle lifecycle, V v) {
        T presenter = provider.get(clazz);
        presenter.bindView(lifecycle, v);
        return presenter;
    }

    /**
     * 获得Presenter单例(不受生命周期影响)
     *
     * @param lifecycle 视图的生命周期
     */
    @SuppressWarnings("unchecked")
    public static <V extends IPresenterView, T extends BasePresenter<V>> T getSingleton(Class<T> clazz, String type, Lifecycle lifecycle, V v) {
        Map<String, BasePresenter<?>> presenters = PRESENTER_STORAGE.get(clazz, HashMap::new);
        T presenter = (T) presenters.get(type);
        if (null == presenter) {
            try {
                presenters.put(type, presenter = clazz.newInstance());
            } catch (Exception e) {
                throw new RuntimeException("无法创建无参构造的Presenter(" + clazz.getSimpleName() + ")");
            }
        }
        presenter.bindView(lifecycle, v);
        return presenter;
    }

    /**
     * 释放指定类及类别的Presenter单例
     */
    public static <V extends IPresenterView, T extends BasePresenter<V>> boolean release(Class<T> clazz, String type) {
        Map<String, BasePresenter<?>> map = PRESENTER_STORAGE.get(clazz);
        if (null == map) {
            return false;
        }
        // 移除指定类型的presenter
        BasePresenter<?> lastOne = map.remove(type);
        // 移除空映射
        if (map.isEmpty()) {
            PRESENTER_STORAGE.remove(clazz);
        }
        // 是否移除成功
        return null != lastOne;
    }

    /**
     * 释放指定类的全部Presenter单例
     */
    public static <V extends IPresenterView, T extends BasePresenter<V>> boolean releaseAll(Class<T> clazz) {
        Map<String, BasePresenter<?>> map = PRESENTER_STORAGE.get(clazz);
        if (null == map) {
            return false;
        }
        map.clear();
        return true;
    }
}
