package com.soybeany.bdlib.android.db;

import android.database.Cursor;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <br>Created by Soybeany on 2018/1/19.
 */
public class DBUpgradeUtils {

    /**
     * 执行升级操作
     */
    public static void upgrade(int oldVersion, int newVersion, IUpgradeExecutor handler) {
        // 语句排序
        List<IMigration> migrations = new LinkedList<>();
        Collections.addAll(migrations, handler.getMigrations());
        Collections.sort(migrations);
        // 语句有效性筛选
        Map<String, String> sqlCache = new HashMap<>(); // 语句缓存，避免重复查库
        List<String> sqlList = new LinkedList<>();
        for (IMigration migration : migrations) {
            int version = migration.getVerCode();
            // 版本不在范围内则跳过
            if (version <= oldVersion || version > newVersion) {
                continue;
            }
            exeMigrations(sqlCache, sqlList, migration, handler);
        }
        handler.onExecute(sqlList);
    }

    /**
     * 执行升级语句
     */
    private static void exeMigrations(Map<String, String> sqlCache, List<String> sqlList, IMigration migration, IUpgradeExecutor handler) {
        // 提取相应的语句
        for (ISQLInfo info : migration.getSqlArr()) {
            // 查询并筛选
            String tableName = info.getTableName();
            String sql;
            if (null == (sql = sqlCache.get(tableName))) {
                try (Cursor c = handler.onRawQuery(StdSQL.queryColumnExist(tableName))) {
                    if (c != null && c.moveToFirst()) {
                        sqlCache.put(tableName, sql = c.getString(c.getColumnIndex("sql")));
                    }
                }
            }
            // 若不需要执行则跳过
            if (!info.isNecessary(sql)) {
                continue;
            }
            sqlList.add(info.build());
        }
    }

    /**
     * 升级执行者
     */
    public interface IUpgradeExecutor {

        /**
         * 获得升级信息数组
         */
        IMigration[] getMigrations();

        /**
         * 使用直接查询
         */
        Cursor onRawQuery(String sql);

        /**
         * 执行sql语句(建议使用事务)
         */
        void onExecute(List<String> sqlList);
    }
}
