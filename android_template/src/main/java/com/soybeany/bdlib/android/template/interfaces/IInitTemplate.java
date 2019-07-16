package com.soybeany.bdlib.android.template.interfaces;

/**
 * 初始化模板，使用init开头
 * <br>Created by Soybeany on 2019/4/30.
 */
public interface IInitTemplate {
    default void initBeforeOnCreate() {
    }

    default void initBeforeSetContentView() {
    }

    default void initAfterSetContentView() {
    }

    default void initFinished() {
    }
}
