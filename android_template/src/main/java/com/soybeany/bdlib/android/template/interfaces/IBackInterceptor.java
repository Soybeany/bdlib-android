package com.soybeany.bdlib.android.template.interfaces;

import com.soybeany.bdlib.android.template.annotation.BackType;

/**
 * <br>Created by Soybeany on 2019/4/29.
 */
public interface IBackInterceptor {

    default void wannaBack(@BackType int backType) {
        if (!shouldInterceptBack(backType)) {
            onPermitBack(backType);
        }
    }

    default boolean shouldInterceptBack(@BackType int backType) {
        return false;
    }

    void onPermitBack(@BackType int backType);
}