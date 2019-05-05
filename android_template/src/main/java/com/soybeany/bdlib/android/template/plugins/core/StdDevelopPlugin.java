package com.soybeany.bdlib.android.template.plugins.core;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;
import com.soybeany.bdlib.android.util.ToastUtils;
import com.soybeany.bdlib.android.util.system.PermissionRequester;

import java.util.HashSet;
import java.util.Set;

import static com.soybeany.bdlib.android.util.R.string.bd_permission_deny;

/**
 * 标准开发，需自行调用{@link #onRequestPermissionsResult(int, String[], int[])}
 * <br>Created by Soybeany on 2019/4/30.
 */
public class StdDevelopPlugin implements IExtendPlugin, PermissionRequester.IPermissionCallback {
    private PermissionRequester mPR;
    private boolean mIsNew;
    private ITemplate mTemplate;

    /**
     * @param pr    {@link PermissionRequester}构造器直接创建
     * @param isNew 判断是否为新创建，null == savedInstanceState为true
     */
    public StdDevelopPlugin(@NonNull PermissionRequester pr, boolean isNew, ITemplate template) {
        mIsNew = isNew;
        mTemplate = template;

        Set<String> permissionSet = new HashSet<>();
        mTemplate.onSetupEssentialPermissions(permissionSet);
        mPR = pr.withEPermission(this, permissionSet.toArray(new String[0]));
    }

    @Override
    public void initAfterSetContentView() {
        mTemplate.onInitViews();
    }

    @Override
    public void onPermissionPass() {
        mTemplate.onEssentialPermissionsPass(mIsNew);
    }

    @Override
    public void onPermissionDeny() {
        mTemplate.onEssentialPermissionsDeny();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPR.checkResults(requestCode, permissions, grantResults);
    }

    public boolean requestPermissions(@NonNull PermissionRequester.IPermissionCallback callback, @Nullable String... permissions) {
        return mPR.requestPermissions(callback, permissions);
    }


    public interface ITemplate {
        /**
         * 初始化视图
         */
        default void onInitViews() {
        }

        /**
         * 处理业务逻辑，如模拟点击、自动发起请求等
         *
         * @param isNew 此模板是否为新创建
         */
        default void doBusiness(boolean isNew) {
        }

        /**
         * 请求权限
         */
        boolean requestPermissions(@NonNull PermissionRequester.IPermissionCallback callback, @Nullable String... permissions);

        /**
         * 设置必要的权限
         */
        default void onSetupEssentialPermissions(Set<String> permissions) {
        }

        /**
         * 必要权限均通过
         */
        default void onEssentialPermissionsPass(boolean isNew) {
            IExtendPlugin.invokeInUiThread(() -> doBusiness(isNew));
        }

        /**
         * 必要权限没有全部通过
         */
        default void onEssentialPermissionsDeny() {
            if (this instanceof Activity) {
                ((Activity) this).finish();
                return;
            }
            ToastUtils.show(bd_permission_deny);
        }
    }
}
