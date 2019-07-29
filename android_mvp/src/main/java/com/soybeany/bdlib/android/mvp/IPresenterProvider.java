package com.soybeany.bdlib.android.mvp;

/**
 * <br>Created by Soybeany on 2019/4/18.
 */
public interface IPresenterProvider {

    String TYPE_DEFAULT = "DEFAULT";

    /**
     * 获得默认生命周期范围的Presenter
     */
    <V extends IPresenterView, T extends BasePresenter<V>> T getPresenter(Class<T> clazz, V v);

    // //////////////////////////////////获得单例//////////////////////////////////

    /**
     * 获得单例的Presenter，使用{@link #TYPE_DEFAULT}类型
     */
    default <V extends IPresenterView, T extends BasePresenter<V>> T getSingleton(Class<T> clazz, V v) {
        return getSingleton(clazz, v, TYPE_DEFAULT);
    }

    /**
     * 获得指定类型的单例Presenter
     */
    <V extends IPresenterView, T extends BasePresenter<V>> T getSingleton(Class<T> clazz, V v, String type);

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
