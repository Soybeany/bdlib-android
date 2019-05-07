package com.soybeany.bdlib.android.db;


import static com.soybeany.bdlib.android.db.ISQLEntity.EQUAL;
import static com.soybeany.bdlib.android.db.ISQLEntity.IS;
import static com.soybeany.bdlib.android.db.ISQLEntity.IS_NOT;
import static com.soybeany.bdlib.android.db.ISQLEntity.NEGATE;
import static com.soybeany.bdlib.android.db.ISQLEntity.NO_CASE;
import static com.soybeany.bdlib.android.db.ISQLEntity.NULL;
import static com.soybeany.bdlib.android.db.ISQLEntity.SELECT_SQLITE_MASTER;

/**
 * <br>Created by Soybeany on 2017/1/19.
 */
public class StdSQL {

    private StdSQL() {
    }

    /**
     * 等于值
     */
    public static String equal(String field, Object value) {
        return field + EQUAL + "'" + value + "'";
    }

    /**
     * 查询null或非null值
     */
    public static String nuLL(String field, boolean isNull) {
        return field + " " + (isNull ? IS : IS_NOT) + " " + NULL;
    }

    /**
     * 查询布尔值
     */
    public static String bool(String field, boolean isTrue) {
        return field + (isTrue ? NEGATE : "") + EQUAL + "0";
    }

    /**
     * 查询数据库指定表是否存在指定列
     */
    public static String queryColumnExist(String tableName) {
        return SELECT_SQLITE_MASTER + " WHERE type = 'table' AND name = '" + tableName + "' " + NO_CASE;
    }
}
