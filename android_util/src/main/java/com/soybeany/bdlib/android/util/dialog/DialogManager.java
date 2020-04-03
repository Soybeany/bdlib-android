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
        if (null == msg) {
            return;
        }
        mVm.execute(() -> {
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
            showNewestMsg(mInfo, false);
        });
    }

    /**
     * 弹出指定的弹窗提示
     */
    public void popMsg(@Nullable IDialogHint msg) {
        mVm.execute(() -> {
            if (!mInfo.hintSet.remove(msg)) {
                return;
            }
            mInfo.unableCancelSet.remove(msg);
            onPopMsg(msg);
            // 更改或关闭弹窗
            if (!mInfo.hintSet.isEmpty()) {
                showNewestMsg(mInfo, true);
            } else {
                dismiss(NORM);
            }
        });
    }

    /**
     * 设置进度
     */
    public synchronized void toProgress(float percent) {
        mRealDialog.onToProgress(percent);
        onToProgress(percent);
    }

    /**
     * 关闭弹窗
     */
    public synchronized void dismiss(DialogDismissReason reason) {
        mVm.execute(() -> {
            for (IDialogHint msg : mInfo.hintSet) {
                onPopMsg(msg);
            }
            mInfo.hintSet.clear();
            mInfo.unableCancelSet.clear();
            // 若弹窗没有关闭，则关闭
            if (mRealDialog.isDialogShowing()) {
                mRealDialog.onDismissDialog(reason);
                onDismissDialog(reason);
            }
            // 设置弹窗标识
            if (mInfo.needDialogShow) {
                mInfo.needDialogShow = false;
            }
        });
    }

    // //////////////////////////////////回调区//////////////////////////////////

    protected void onShowMsg(IDialogHint msg) {
    }

    protected void onReshowMsg(IDialogHint msg) {
    }

    protected void onPopMsg(IDialogHint msg) {
    }

    protected void onToProgress(float percent) {
    }

    protected void onShowDialog() {
    }

    protected void onDismissDialog(DialogDismissReason reason) {
    }

    // //////////////////////////////////内部区//////////////////////////////////

    private void showNewestMsg(DialogInfoVM.Info info, boolean isReshow) {
        // 若弹窗没有显示，则显示
        if (!mRealDialog.isDialogShowing()) {
            mRealDialog.onShowDialog();
            onShowDialog();
        }
        // 设置弹窗标识
        if (!info.needDialogShow) {
            info.needDialogShow = true;
        }
        IDialogHint hint = info.getCurDialogHint();
        if (isReshow) {
            onReshowMsg(hint);
        } else {
            onShowMsg(hint);
        }
        mRealDialog.onChangeDialogHint(hint, info.shouldDialogCancelable());
    }
}
