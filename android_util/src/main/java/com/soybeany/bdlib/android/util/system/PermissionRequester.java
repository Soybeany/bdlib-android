package com.soybeany.bdlib.android.util.system;

import android.Manifest;
import android.app.Dialog;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;

import com.soybeany.bdlib.android.util.IObserver;
import com.soybeany.bdlib.android.util.R;
import com.soybeany.bdlib.android.util.StdHintUtils;
import com.soybeany.bdlib.android.util.ToastUtils;
import com.soybeany.bdlib.core.java8.Optional;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 权限请求者，需要在最后调用{@link #startObserve()}
 * <br>Created by Soybeany on 2017/3/23.
 */
public class PermissionRequester implements IObserver {

    // //////////////////////////////////常用权限//////////////////////////////////

    /**
     * 读取手机状态
     */
    public static final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;

    /**
     * 读取外部存储
     */
    public static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;

    /**
     * 写入外部存储
     */
    public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;


    // //////////////////////////////////内部变量//////////////////////////////////

    private static final int DEFAULT_REQUEST_CODE = 10000; // 必要权限的请求码
    private static int AUTO_REQUEST_CODE; // 自动增长的请求码

    private static final Map<FragmentActivity, AlertDialog> DIALOG_MAP = new HashMap<>(); // 管理页面弹窗的映射

    private FragmentActivity mActivity; // 活动页面
    private IPermissionDealer mDealer; // 权限处理者
    @Nullable
    private IPermissionCallback mECallback; // 必要权限回调
    @Nullable
    private String[] mEPermissions; // 必要权限

    private final SparseArray<IPermissionCallback> mCallbackMap = new SparseArray<>(); // 回调映射
    private final List<DenyInfo> mDenyList = new LinkedList<>(); // 拒绝信息的列表

    private boolean mHasSignaled; // 是否已触发回调
    @StringRes
    private int mMsgResId = R.string.bd_permission_request; // 权限提示，请点击"设置"-"权限"-打开所需权限

    /**
     * 获得自增长请求码
     */
    private static int getAutoRequestCode() {
        return AUTO_REQUEST_CODE++ % DEFAULT_REQUEST_CODE;
    }

    public PermissionRequester(@NonNull FragmentActivity activity, @NonNull IPermissionDealer dealer) {
        mActivity = activity;
        mDealer = dealer;
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        if (null != mECallback && !isDialogShowing()) {
            requestPermissions(DEFAULT_REQUEST_CODE, mECallback, mEPermissions);
        }
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        Optional.ofNullable(DIALOG_MAP.remove(mActivity)).ifPresent(Dialog::dismiss);
        mActivity.getLifecycle().removeObserver(this);
    }

    /**
     * 开始监控生命周期
     */
    public void startObserve() {
        mActivity.getLifecycle().addObserver(this);
    }

    /**
     * 请求指定的必要权限(重复调用将覆盖callback)
     */
    public PermissionRequester withEPermission(@NonNull IPermissionCallback callback, @Nullable String... permissions) {
        mECallback = callback;
        mEPermissions = permissions;
        return this;
    }

    /**
     * 设置提示信息
     */
    public PermissionRequester msg(@StringRes int resId) {
        mMsgResId = resId;
        return this;
    }

    /**
     * 请求指定的可选权限(可在需要的地方动态调用)
     *
     * @return 是否已获得权限
     */
    public boolean requestPermissions(@NonNull IPermissionCallback callback, @Nullable String... permissions) {
        return requestPermissions(getAutoRequestCode(), callback, permissions);
    }

    /**
     * 检测请求结果
     * 在界面的{@link AppCompatActivity#onRequestPermissionsResult(int, String[], int[])}中调用
     */
    public void checkResults(int requestCode, String[] permissions, int[] grantResults) {
        // 检测是否此类发出的请求
        IPermissionCallback callback = mCallbackMap.get(requestCode);
        if (grantResults.length == 0 || null == callback) {
            return;
        }
        mCallbackMap.remove(requestCode);

        // 检测是否全部权限均被允许
        boolean isAllGranted = true;
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                isAllGranted = false;
                break;
            }
        }

        // 权限全部被允许时，调用权限通过的回调，否则显示设置弹窗
        if (isAllGranted) {
            callback.onPermissionPass();
        } else {
            mDenyList.add(new DenyInfo(requestCode, callback, permissions));
            showDialog();
        }
    }

    private boolean isDialogShowing() {
        AlertDialog dialog = getDialog(false);
        return null != dialog && dialog.isShowing();
    }

    private void showDialog() {
        AlertDialog dialog = getDialog(true);
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    private AlertDialog getDialog(boolean createOnNull) {
        AlertDialog dialog = DIALOG_MAP.get(mActivity);
        if (!createOnNull || null != dialog) {
            return dialog;
        }
        DIALOG_MAP.put(mActivity, dialog = getNewDialog());
        return dialog;
    }

    /**
     * 请求指定权限
     *
     * @return 是否已获得权限
     */
    private boolean requestPermissions(int requestCode, @NonNull IPermissionCallback callback, @Nullable String... permissions) {
        List<String> requestList = getRequestList(permissions);
        if (!requestList.isEmpty()) {
            mCallbackMap.put(requestCode, callback);
            mDealer.onRequestPermissions(mActivity, requestList.toArray(new String[0]), requestCode);
            return false;
        } else if (DEFAULT_REQUEST_CODE == requestCode && !mHasSignaled) {
            callback.onPermissionPass();
            mHasSignaled = true;
        }
        return true;
    }

    /**
     * 获取需要申请权限的列表
     */
    @NonNull
    private List<String> getRequestList(@Nullable String[] permissions) {
        List<String> deniedList = new LinkedList<>();
        if (null != permissions) {
            for (String permission : permissions) {
                // 判断是否app已经获取到某一个权限 或者 是否显示申请权限对话框，如果同意了或者不再询问则返回false
                if (ContextCompat.checkSelfPermission(mActivity, permission) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {
                    deniedList.add(permission);
                }
            }
        }
        return deniedList;
    }

    /**
     * 初始化弹窗
     */
    private AlertDialog getNewDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(StdHintUtils.dialogTitle());
        builder.setMessage(mMsgResId);
        builder.setNegativeButton(R.string.bd_hint_dialog_cancel, (dialog, which) -> {
            for (DenyInfo info : mDenyList) {
                if (getRequestList(info.permissions).isEmpty()) {
                    info.callback.onPermissionPass();
                } else {
                    info.callback.onPermissionDeny();
                }
            }
            mDenyList.clear();
        });
        builder.setPositiveButton(R.string.bd_hint_dialog_ok, (dialog, which) -> {
            startAppSettings();
        });

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    /**
     * 启动应用的设置
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + mActivity.getPackageName()));
        mActivity.startActivity(intent);
    }

    /**
     * 权限处理者
     */
    public interface IPermissionDealer {
        /**
         * 需要申请权限时的回调
         */
        void onRequestPermissions(FragmentActivity activity, String[] permissions, int requestCode);
    }

    /**
     * 权限请求的回调接口
     */
    public interface IPermissionCallback {
        /**
         * 权限请求全部通过时的回调
         */
        void onPermissionPass();

        /**
         * 权限请求被拒绝时的回调
         */
        default void onPermissionDeny() {
            ToastUtils.show(R.string.bd_permission_deny);
        }
    }

    /**
     * 权限拒绝的信息
     */
    private static class DenyInfo {
        int requestCode;
        IPermissionCallback callback;
        String[] permissions;

        DenyInfo(int requestCode, IPermissionCallback callback, String[] permissions) {
            this.requestCode = requestCode;
            this.callback = callback;
            this.permissions = permissions;
        }
    }

}
