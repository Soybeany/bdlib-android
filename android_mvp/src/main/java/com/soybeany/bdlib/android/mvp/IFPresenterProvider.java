package com.soybeany.bdlib.android.mvp;

/**
 * Fragment使用的Provider
 * <br>Created by Soybeany on 2019/7/29.
 */
public interface IFPresenterProvider extends IPresenterProvider {

    /**
     * 获得Activity级别的Presenter
     */
    <V extends IPresenterView, T extends BasePresenter<V>> T getActivityPresenter(Class<T> clazz, V v);
}
