package com.soybeany.bdlib.android.db;

/**
 * 数据库升级接口
 * <br>Created by Soybeany on 2017/1/19.
 */
public interface IMigration extends Comparable<IMigration> {

    /**
     * 获得版本号
     */
    int getVerCode();

    /**
     * 获得需要执行的语句
     */
    ISQLInfo[] getSqlArr();

}
