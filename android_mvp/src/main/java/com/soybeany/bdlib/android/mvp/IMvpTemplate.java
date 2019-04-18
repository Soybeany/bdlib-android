package com.soybeany.bdlib.android.mvp;

/**
 * <br>Created by Soybeany on 2019/4/18.
 */
public interface IMvpTemplate {

    /**
     * 初始化Presenter
     */
    default void onInitPresenters(IPresenterProvider provider) {
    }
}
