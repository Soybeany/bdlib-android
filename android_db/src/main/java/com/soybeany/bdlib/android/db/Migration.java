package com.soybeany.bdlib.android.db;

import androidx.annotation.NonNull;


/**
 * 数据库升级信息（默认实现）
 * <br>Created by Soybeany on 2017/1/13.
 */
public class Migration implements IMigration {

    /**
     * 版本号
     */
    private int verCode;

    /**
     * 需要执行的升级语句数组
     */
    private ISQLInfo[] sqlArr;

    public Migration(int verCode, ISQLInfo[] sqlArr) {
        this.verCode = verCode;
        this.sqlArr = sqlArr;
    }

    @Override
    public int getVerCode() {
        return verCode;
    }

    @Override
    public ISQLInfo[] getSqlArr() {
        return sqlArr;
    }

    @Override
    public int compareTo(@NonNull IMigration another) {
        return getVerCode() - another.getVerCode();
    }

}
