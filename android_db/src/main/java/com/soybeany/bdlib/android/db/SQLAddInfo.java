package com.soybeany.bdlib.android.db;

import android.support.annotation.Nullable;

/**
 * <br>Created by Soybeany on 2018/1/18.
 */
public class SQLAddInfo implements ISQLInfo {

    private String mTableName; // 表名
    private String mColumnName; // 列名
    private String mType; // 类型

    /**
     * @param type 可使用{@link ISQLEntity#STRING}、{@link ISQLEntity#INTEGER}、{@link ISQLEntity#FLOAT}等值
     */
    public SQLAddInfo(String tableName, String columnName, String type) {
        mTableName = tableName;
        mColumnName = columnName;
        mType = type;
    }

    @Override
    public String getTableName() {
        return mTableName;
    }

    @Override
    public boolean isNecessary(@Nullable String createSql) {
        return null != createSql && !createSql.toLowerCase().contains(mColumnName.toLowerCase());
    }

    @Override
    public String build() {
        return ISQLEntity.SQL_ALTER_TABLE + " " + mTableName + " "
                + ISQLEntity.SQL_ADD_COLUMN + " " + mColumnName + " " + mType;
    }

}
