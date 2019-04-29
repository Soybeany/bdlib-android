package com.soybeany.bdlib.android.template.interfaces;

/**
 * 可拓展插件，使用signal开头
 * <br>Created by Soybeany on 2019/4/29.
 */
public interface IExtendPlugin {

    default void signalBeforeSetContentView() {
    }

    default void signalAfterSetContentView() {
    }

    default void signalOnInitFinished() {
    }
}
