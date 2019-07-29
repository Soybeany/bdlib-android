package com.soybeany.bdlib.android.mvp;

/**
 * 普通Presenter的提供者
 * <br>Created by Soybeany on 2019/4/18.
 */
public interface IPresenterProvider {
    /**
     * 获得默认生命周期范围的Presenter
     */
    <V extends IPresenterView, T extends BasePresenter<V>> T get(Class<T> clazz, V v);
}
