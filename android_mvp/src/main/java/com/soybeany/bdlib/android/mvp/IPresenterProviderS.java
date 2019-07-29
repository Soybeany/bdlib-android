package com.soybeany.bdlib.android.mvp;

/**
 * 单例Presenter的提供者
 * <br>Created by Soybeany on 2019/7/29.
 */
public interface IPresenterProviderS extends IPresenterProvider {
    String TYPE_DEFAULT = "DEFAULT";

    // //////////////////////////////////获得单例//////////////////////////////////

    /**
     * 获得单例的Presenter，使用{@link #TYPE_DEFAULT}类型
     */
    @Override
    default <V extends IPresenterView, T extends BasePresenter<V>> T get(Class<T> clazz, V v) {
        return get(clazz, v, TYPE_DEFAULT);
    }

    /**
     * 获得指定类型的单例Presenter
     */
    <V extends IPresenterView, T extends BasePresenter<V>> T get(Class<T> clazz, V v, String type);

    // //////////////////////////////////清除单例//////////////////////////////////

    /**
     * 清除单例Presenter，使用{@link #TYPE_DEFAULT}类型
     *
     * @return 是否移除成功
     */
    default <V extends IPresenterView, T extends BasePresenter<V>> boolean release(Class<T> clazz) {
        return release(clazz, TYPE_DEFAULT);
    }

    /**
     * 清除指定类型的单例Presenter
     *
     * @return 是否移除成功
     */
    <V extends IPresenterView, T extends BasePresenter<V>> boolean release(Class<T> clazz, String type);

    /**
     * 清除指定Class的全部单例Presenter
     *
     * @return 是否移除成功
     */
    <V extends IPresenterView, T extends BasePresenter<V>> boolean releaseAll(Class<T> clazz);
}
