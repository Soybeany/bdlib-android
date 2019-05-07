package com.soybeany.bdlib.android.db;

/**
 * 查询语句实体接口
 * <br>Created by Soybeany on 2017/1/18.
 */
public interface ISQLEntity {

    // //////////////////////////////////常用语句//////////////////////////////////

    /**
     * 创建表格的语句
     */
    String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS";

    /**
     * 改变表格的语句
     */
    String SQL_ALTER_TABLE = "ALTER TABLE";

    /**
     * 重命名表格
     */
    String SQL_RENAME_TABLE = "RENAME TO";

    /**
     * 增加表格字段
     */
    String SQL_ADD_COLUMN = "ADD COLUMN";

    /**
     * 主键
     */
    String PRIMARY_KEY = "PRIMARY KEY";

    /**
     * 自增长
     */
    String AUTOINCREMENT = "AUTOINCREMENT";

    /**
     * 查询系统表(type TEXT, name TEXT, tbl_name TEXT, rootpage INTEGER, sql TEXT)
     */
    String SELECT_SQLITE_MASTER = "SELECT * FROM sqlite_master";

    /**
     * 忽略大小写
     */
    String NO_CASE = "COLLATE NOCASE";


    // //////////////////////////////////数据类型//////////////////////////////////

    /**
     * 整型
     */
    String INTEGER = "INTEGER";

    /**
     * 浮点型
     */
    String FLOAT = "FLOAT";

    /**
     * 文本类型
     */
    String TEXT = "TEXT";

    /**
     * 字符串
     */
    String STRING = "STRING";


    // //////////////////////////////////一般符号//////////////////////////////////

    /**
     * 左括号
     */
    String LEFT_BRACKET = "(";

    /**
     * 右括号
     */
    String RIGHT_BRACKET = ")";

    /**
     * 逗号分隔符
     */
    String DOT_SEPARATOR = ",";

    /**
     * 分号分隔符
     */
    String SEMICOLON_SEPARATOR = ";";


    // //////////////////////////////////比较运算符//////////////////////////////////

    /**
     * 检查两个操作数的值是否相等，如果相等则条件为真
     */
    String EQUAL = "=";

    /**
     * 对判断条件取反
     */
    String NEGATE = "!";

    /**
     * 检查左操作数的值是否大于右操作数的值，如果是则条件为真
     */
    String GREATER = ">";

    /**
     * 检查左操作数的值是否小于右操作数的值，如果是则条件为真
     */
    String LESS = "<";


    // //////////////////////////////////逻辑运算符//////////////////////////////////

    /**
     * AND 运算符允许在一个 SQL 语句的 WHERE 子句中的多个条件的存在
     */
    String AND = "AND";

    /**
     * OR 运算符用于结合一个 SQL 语句的 WHERE 子句中的多个条件
     */
    String OR = "OR";

    /**
     * NOT 运算符用于结合一个 SQL 语句的 WHERE 子句中的多个条件
     */
    String NOT = "NOT";

    /**
     * BETWEEN 运算符用于在给定最小值和最大值范围内的一系列值中搜索值
     */
    String BETWEEN = "BETWEEN";

    /**
     * EXISTS 运算符用于在满足一定条件的指定表中搜索行的存在
     */
    String EXISTS = "EXISTS";

    /**
     * IN 运算符用于把某个值与一系列指定列表的值进行比较
     */
    String IN = "IN";

    /**
     * LIKE 运算符用于把某个值与使用通配符运算符的相似值进行比较
     */
    String LIKE = "LIKE";

    /**
     * GLOB 运算符用于把某个值与使用通配符运算符的相似值进行比较（GLOB 与 LIKE 不同之处在于，它是大小写敏感的）
     */
    String GLOB = "GLOB";

    /**
     * IS 运算符与 = 相似
     */
    String IS = "IS";

    /**
     * IS NOT 运算符与 != 相似
     */
    String IS_NOT = "IS NOT";

    /**
     * || 连接两个不同的字符串，得到一个新的字符串
     */
    String CONCAT = "||";

    /**
     * UNIQUE 运算符搜索指定表中的每一行，确保唯一性（无重复）
     */
    String UNIQUE = "UNIQUE";

    /**
     * NULL 运算符用于把某个值与 NULL 值进行比较。
     */
    String NULL = "NULL";

    // //////////////////////////////////SQL语句生成//////////////////////////////////

    /**
     * 是否去重语句
     */
    boolean isDistinct();

    /**
     * 获得数据列
     */
    String[] getColumns();

    /**
     * 获得结果筛选语句
     */
    String getWhere();

    /**
     * 获得分组语句
     */
    String getGroupBy();

    /**
     * 获得分组结果筛选语句
     */
    String getHaving();

    /**
     * 获得排序语句
     */
    String getOrderBy();

    /**
     * 获得结果条数限制语句
     */
    String getLimit();

}
