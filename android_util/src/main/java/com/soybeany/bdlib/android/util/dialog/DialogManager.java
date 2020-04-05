package com.soybeany.bdlib.android.util.dialog;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.util.dialog.msg.IDialogHint;

import static com.soybeany.bdlib.android.util.dialog.DialogDismissReason.NORM;

/**
 * 弹窗管理器，负责单弹窗多消息的处理
 * <br>Created by Soybeany on 2020/4/2.
 */
@SuppressWarnings("WeakerAccess")
public class DialogManager {

    private static final String DEFAULT_UID = "DEFAULT_UID";

    private final DialogInfoVM mVm;
    private final DialogInfoVM.Info mInfo;
    private final IRealDialog mRealDialog;

    public DialogManager(String type, @NonNull DialogInfoVM vm, @NonNull IRealDialog dialog) {
        mVm = vm;
        mInfo = vm.getInfo(type);
        mRealDialog = dialog;
    }

    // //////////////////////////////////调用区//////////////////////////////////

    /**
     * 显示指定的弹窗提示
     */
    public void showMsg(@Nullable IDialogHint msg) {
        showMsg(DEFAULT_UID, msg);
    }

    /**
     * 弹出指定的弹窗提示
     */
    public void popMsg(@Nullable IDialogHint msg) {
        popMsg(DEFAULT_UID, msg);
    }

    /**
     * 设置进度
     */
    public void toProgress(float percent) {
        toProgress(DEFAULT_UID, percent);
    }

    /**
     * 关闭弹窗
     */
    public void dismissDialog(DialogDismissReason reason) {
        dismissDialog(DEFAULT_UID, reason);
    }

    // //////////////////////////////////子类方法区//////////////////////////////////

    protected void showMsg(String uid, @Nullable IDialogHint msg) {
        if (null == msg) {
            return;
        }
        mVm.exeInMainThread(() -> {
            // 不可取消列表按需设置
            if (msg.cancelable()) {
                // 有可能是已有消息的修改，故尝试移除
                mInfo.unableCancelSet.remove(msg);
            } else {
                mInfo.unableCancelSet.add(msg);
            }
            // 设置最后弹窗时间
            msg.timestamp(System.currentTimeMillis());
            // 添加至信息集(可能覆盖)
            mInfo.hintSet.add(msg);
            // 显示
            showNewestMsg(uid, mInfo, false);
        });
    }

    protected void popMsg(String uid, @Nullable IDialogHint msg) {
        mVm.exeInMainThread(() -> {
            if (!mInfo.hintSet.remove(msg)) {
                return;
            }
            mInfo.unableCancelSet.remove(msg);
            onPopMsg(uid, msg);
            // 更改或关闭弹窗
            if (!mInfo.hintSet.isEmpty()) {
                showNewestMsg(uid, mInfo, true);
            } else {
                dismissDialog(uid, NORM);
            }
        });
    }

    protected void toProgress(String uid, float percent) {
        mRealDialog.onToProgress(percent);
        onToProgress(uid, percent);
    }

    protected void dismissDialog(String uid, DialogDismissReason reason) {
        mVm.exeInMainThread(() -> {
            for (IDialogHint msg : mInfo.hintSet) {
                onPopMsg(uid, msg);
            }
            mInfo.hintSet.clear();
            mInfo.unableCancelSet.clear();
            // 若弹窗没有关闭，则关闭
            if (mInfo.needDialogShow || mRealDialog.isDialogShowing()) {
                mRealDialog.onDismissDialog(reason);
                onDismissDialog(uid, reason);
            }
            // 设置弹窗标识
            if (mInfo.needDialogShow) {
                mInfo.needDialogShow = false;
            }
        });
    }

    // //////////////////////////////////回调区//////////////////////////////////

    protected void onShowMsg(String uid, IDialogHint msg) {
    }

    protected void onReshowMsg(String uid, IDialogHint msg) {
    }

    protected void onPopMsg(String uid, IDialogHint msg) {
    }

    protected void onToProgress(String uid, float percent) {
    }

    protected void onShowDialog(String uid) {
    }

    protected void onDismissDialog(String uid, DialogDismissReason reason) {
    }

    // //////////////////////////////////内部区//////////////////////////////////

    private void showNewestMsg(String uid, DialogInfoVM.Info info, boolean isReshow) {
        // 若弹窗没有显示，则显示
        if (!mInfo.needDialogShow || !mRealDialog.isDialogShowing()) {
            mRealDialog.onShowDialog();
            onShowDialog(uid);
        }
        // 设置弹窗标识
        if (!info.needDialogShow) {
            info.needDialogShow = true;
        }
        IDialogHint hint = info.getCurDialogHint();
        if (isReshow) {
            onReshowMsg(uid, hint);
        } else {
            onShowMsg(uid, hint);
        }
        mRealDialog.onChangeDialogHint(hint, info.shouldDialogCancelable());
    }
}
