package com.soybeany.bdlib.android.mvp;

/**
 * <br>Created by Soybeany on 2019/4/18.
 */
public interface IPresenterProvider {

    <V extends IView, T extends BasePresenter<V>> T getPresenter(Class<T> clazz, V v);

}
