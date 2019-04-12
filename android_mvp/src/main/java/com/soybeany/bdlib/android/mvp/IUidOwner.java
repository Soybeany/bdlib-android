package com.soybeany.bdlib.android.mvp;

/**
 * <br>Created by Soybeany on 2019/4/12.
 */
public interface IUidOwner {

    String DEFAULT_UID = "default_uid";

    static String getKey(Class clazz, String uid) {
        return clazz.getName() + "-" + uid;
    }

    /**
     * 用于相同类型时区分当前对象
     */
    default String getUid() {
        return DEFAULT_UID;
    }
}
