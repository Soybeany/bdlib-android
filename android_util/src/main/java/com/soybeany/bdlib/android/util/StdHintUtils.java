package com.soybeany.bdlib.android.util;

import android.support.annotation.StringRes;

/**
 * 标准提示语工具类
 * <br>Created by Soybeany on 2017/1/24.
 */
public class StdHintUtils {

    // ******************************基础信息部分（对内，用于拼接）******************************

    /**
     * 没有网络
     */
    public static String noConnectionBase() {
        return getString(R.string.bd_hint_no_connection_base);
    }

    /**
     * 服务器无响应
     */
    public static String noResponseBase() {
        return getString(R.string.bd_hint_no_response_base);
    }


    // ******************************前缀部分******************************

    /**
     * 取消（前缀）
     */
    public static String cancelPrefix() {
        return getString(R.string.bd_hint_cancel_prefix);
    }

    /**
     * 加载中（前缀）
     */
    public static String loadingPrefix() {
        return getString(R.string.bd_hint_loading_prefix);
    }


    // ******************************后缀1部分******************************

    /**
     * 超时（后缀1）
     */
    public static String timeoutSuffix() {
        return getString(R.string.bd_hint_timeout_suffix);
    }

    /**
     * 失败（后缀1）
     */
    public static String failSuffix() {
        return getString(R.string.bd_hint_fail_suffix);
    }

    /**
     * 成功（后缀1）
     */
    public static String successSuffix() {
        return getString(R.string.bd_hint_success_suffix);
    }


    // ******************************后缀2部分******************************

    /**
     * 重试（后缀2）
     */
    public static String retrySuffix2() {
        return getString(R.string.bd_hint_retry_suffix2);
    }

    /**
     * 重新加载（后缀2）
     */
    public static String reloadSuffix2() {
        return getString(R.string.bd_hint_reload_suffix2);
    }

    /**
     * 加载中（后缀2）
     */
    public static String loadingSuffix2() {
        return getString(R.string.bd_hint_loading_suffix2);
    }


    // ******************************标准提示语部分（对外，用于拼接）******************************

    /**
     * 登录
     */
    public static String loginHint() {
        return getString(R.string.bd_hint_login);
    }

    /**
     * 登出
     */
    public static String logoutHint() {
        return getString(R.string.bd_hint_logout);
    }

    /**
     * 重新登录
     */
    public static String reLoginHint() {
        return getString(R.string.bd_hint_re_login);
    }

    /**
     * 加载
     */
    public static String getHint() {
        return getString(R.string.bd_hint_get);
    }

    /**
     * 提交
     */
    public static String postHint() {
        return getString(R.string.bd_hint_post);
    }

    /**
     * 下载
     */
    public static String downloadHint() {
        return getString(R.string.bd_hint_download);
    }

    /**
     * 上传
     */
    public static String uploadHint() {
        return getString(R.string.bd_hint_upload);
    }

    /**
     * 请求
     */
    public static String requestHint() {
        return getString(R.string.bd_hint_request);
    }

    /**
     * 读取
     */
    public static String readHint() {
        return getString(R.string.bd_hint_read);
    }

    /**
     * 写入
     */
    public static String writeHint() {
        return getString(R.string.bd_hint_write);
    }

    /**
     * 读写
     */
    public static String readWriteHint() {
        return getString(R.string.bd_hint_read_write);
    }


    // ******************************标准请求信息部分******************************

    /**
     * 超时信息
     */
    public static String timeoutMsg() {
        return requestHint() + timeoutSuffix() + retrySuffix2();
    }

    /**
     * 重新加载信息
     */
    public static String reloadMsg() {
        return requestHint() + failSuffix() + reloadSuffix2();
    }

    /**
     * 重试信息
     */
    public static String retryMsg() {
        return requestHint() + failSuffix() + retrySuffix2();
    }

    /**
     * 取消请求
     */
    public static String cancelMsg() {
        return cancelPrefix() + requestHint();
    }

    /**
     * 标准加载中弹窗
     */
    public static String loadingDialog() {
        return loadingPrefix() + requestHint() + loadingSuffix2();
    }


    // //////////////////////////////////内部方法//////////////////////////////////

    private static String getString(@StringRes int resId) {
        return BDContext.getResources().getString(resId);
    }
}
