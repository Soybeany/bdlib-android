package com.soybeany.bdlib.android.util.dialog;

import com.soybeany.bdlib.android.util.dialog.msg.IDialogHint;

/**
 * 真正的弹窗实现类
 * <br>Created by Soybeany on 2020/4/2.
 */
public interface IRealDialog {

    /**
     * 显示弹窗
     */
    void onShowDialog();

    /**
     * 更新进度
     */
    void onToProgress(float percent);

    /**
     * 显示指定提示语
     */
    void onDisplayHint(IDialogHint hint);

    /**
     * 改变弹窗的可取消性
     */
    void onChangeCancelable(boolean cancelable);

    /**
     * 关闭弹窗
     */
    void onDismissDialog(DialogDismissReason reason);
}
