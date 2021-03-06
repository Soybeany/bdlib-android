package com.soybeany.bdlib.android.template.plugins.core;

import android.app.Activity;
import android.os.Bundle;

import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;
import com.soybeany.bdlib.android.util.ToastUtils;
import com.soybeany.bdlib.android.util.system.PermissionRequester;

import java.util.HashSet;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;

import static com.soybeany.bdlib.android.template.interfaces.IExtendPlugin.invokeInUiThread;
import static com.soybeany.bdlib.android.template.interfaces.IExtendPlugin.invokeOnNotNull;
import static com.soybeany.bdlib.android.util.R.string.bd_permission_deny;

/**
 * 标准开发，一般用于Activity，也可用于Fragment(但建议使用{@link FragmentDevelopPlugin})，
 * 需自行调用{@link #onCreate(Bundle)}、
 * {@link #onRequestPermissionsResult(int, String[], int[])}、
 * {@link #activate(FragmentActivity, PermissionRequester.IPermissionDealer)}
 * <br>Created by Soybeany on 2019/4/30.
 */
public class StdDevelopPlugin implements IExtendPlugin, PermissionRequester.IPermissionCallback {
    public static final String GROUP_ID = "Develop";

    protected boolean mIsNew; // 此模板是否为新创建（屏幕旋转、recreate等导致的重新创建，则为false）
    @Nullable
    protected ICallback mCallback;
    @Nullable
    private PermissionRequester mPR;
    @Nullable
    private LifecycleOwner mOwner;

    public StdDevelopPlugin(@Nullable LifecycleOwner owner, @Nullable ICallback callback) {
        mOwner = owner;
        mCallback = callback;
    }

    @Override
    public void initAfterSetContentView() {
        invokeOnNotNull(mCallback, ICallback::onInitViews);

        if (null != mPR && null != mOwner) {
            mPR.startObserve(mOwner);
        }
    }

    @Override
    public void onPermissionPass() {
        invokeOnNotNull(mCallback, callback -> callback.onEssentialPermissionsPass(mIsNew));
        onPermissionPassInner();
    }

    @Override
    public void onPermissionDeny() {
        invokeOnNotNull(mCallback, ICallback::onEssentialPermissionsDeny);
    }

    @NonNull
    @Override
    public final String getGroupId() {
        return GROUP_ID;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        mIsNew = (null == savedInstanceState);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        invokeOnNotNull(mPR, pr -> pr.checkResults(requestCode, permissions, grantResults));
    }

    public boolean requestPermissions(@NonNull PermissionRequester.IPermissionCallback callback, @Nullable String... permissions) {
        return null != mPR && mPR.requestPermissions(callback, permissions);
    }

    public StdDevelopPlugin activate(@Nullable FragmentActivity activity, @Nullable PermissionRequester.IPermissionDealer dealer) {
        Set<String> permissionSet = new HashSet<>();
        invokeOnNotNull(mCallback, c -> c.onSetupEssentialPermissions(permissionSet));
        if (null != activity && null != dealer) {
            mPR = new PermissionRequester(activity, dealer).withEPermission(this, permissionSet.toArray(new String[0]));
        }
        return this;
    }

    public void deactivate() {
        mPR = null;
    }

    /**
     * 权限通过后的内部操作
     */
    protected void onPermissionPassInner() {
        signalDoBusiness(mCallback, mIsNew);
    }

    /**
     * 触发doBusiness回调
     */
    protected void signalDoBusiness(ICallback callback, boolean isNew) {
        invokeOnNotNull(callback, c -> invokeInUiThread(() -> c.doBusiness(isNew)));
    }

    /**
     * 主动执行
     */
    public interface IInvoker {
        /**
         * 请求权限
         */
        boolean requestPermissions(@NonNull PermissionRequester.IPermissionCallback callback, @Nullable String... permissions);
    }

    /**
     * 回调
     */
    public interface ICallback {
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
         * 设置必要的权限
         */
        default void onSetupEssentialPermissions(Set<String> permissions) {
        }

        /**
         * 必要权限均通过
         */
        default void onEssentialPermissionsPass(boolean isNew) {
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
