package com.soybeany.bdlib.android.db;

import android.support.annotation.Nullable;

/**
 * SQL信息
 * <br>Created by Soybeany on 2018/1/18.
 */
public interface ISQLInfo {

    /**
     * 获得操作的表名
     */
    String getTableName();

    /**
     * 是否需要执行
     */
    boolean isNecessary(@Nullable String createSql);

    /**
     * 构建sql
     */
    String build();

}
