package com.soybeany.bdlib.android.util;

/**
 * 标准提示语工具类
 * <br>Created by Soybeany on 2017/1/24.
 */
public class StdHintUtils {

    // ******************************基础信息部分（对内，用于拼接）******************************

    /**
     * 没有网络
     */
    public static String STD_NO_CONNECTION_BASE = "无法连接到服务器";

    /**
     * 服务器无响应
     */
    public static String STD_NO_RESPONSE_BASE = "服务器无响应";


    // ******************************前缀部分******************************

    /**
     * 取消（前缀）
     */
    public static final String STD_CANCEL_PREFIX = "您取消了";

    /**
     * 加载中（前缀）
     */
    public static final String STD_LOADING_PREFIX = "正在";


    // ******************************后缀1部分******************************

    /**
     * 超时（后缀1）
     */
    public static final String STD_TIMEOUT_SUFFIX = "超时";

    /**
     * 失败（后缀1）
     */
    public static final String STD_FAIL_SUFFIX = "失败";

    /**
     * 成功（后缀1）
     */
    public static final String STD_SUCCESS_SUFFIX = "成功";


    // ******************************后缀2部分******************************

    /**
     * 重试（后缀2）
     */
    public static final String STD_RETRY_SUFFIX = "，请稍后再试";

    /**
     * 重新加载（后缀2）
     */
    public static final String STD_RELOAD_SUFFIX = "，请刷新页面";

    /**
     * 加载中（后缀2）
     */
    public static final String STD_LOADING_SUFFIX = "，请稍候...";


    // ******************************标准提示语部分（对外，用于拼接）******************************

    /**
     * 登录
     */
    public static final String STD_LOGIN_HINT = "登录";

    /**
     * 重新登录
     */
    public static final String STD_RE_LOGIN_HINT = "重新登录";

    /**
     * 加载
     */
    public static final String STD_GET_HINT = "加载数据";

    /**
     * 提交
     */
    public static final String STD_POST_HINT = "提交数据";

    /**
     * 下载
     */
    public static final String STD_DOWNLOAD_HINT = "下载数据";

    /**
     * 上传
     */
    public static final String STD_UPLOAD_HINT = "上传数据";

    /**
     * 请求
     */
    public static final String STD_REQUEST_HINT = "请求数据";

    /**
     * 读取
     */
    public static final String STD_READ_HINT = "读取数据";

    /**
     * 写入
     */
    public static final String STD_WRITE_HINT = "写入数据";

    /**
     * 读写
     */
    public static final String STD_READ_WRITE_HINT = "读写数据";


    // ******************************标准请求信息部分******************************

    /**
     * 标准超时信息
     */
    public static final String STD_TIMEOUT_MSG = STD_REQUEST_HINT + STD_TIMEOUT_SUFFIX + STD_RETRY_SUFFIX;

    /**
     * 标准重新加载信息
     */
    public static final String STD_RELOAD_MSG = STD_REQUEST_HINT + STD_FAIL_SUFFIX + STD_RELOAD_SUFFIX;


    // ******************************标准请求吐司部分******************************

    /**
     * 标准重新加载吐司
     */
    public static final String STD_RELOAD_TOAST = STD_REQUEST_HINT + STD_FAIL_SUFFIX + STD_RETRY_SUFFIX;

    /**
     * 标准取消请求吐司
     */
    public static final String STD_CANCEL_TOAST = STD_CANCEL_PREFIX + STD_REQUEST_HINT;


    // ******************************标准弹窗提示语部分******************************

    /**
     * 标准加载中弹窗
     */
    public static final String STD_LOADING_DIALOG = STD_LOADING_PREFIX + STD_REQUEST_HINT + STD_LOADING_SUFFIX;

}
