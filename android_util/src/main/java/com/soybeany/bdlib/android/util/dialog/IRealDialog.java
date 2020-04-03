package com.soybeany.bdlib.android.util.dialog;

import com.soybeany.bdlib.android.util.dialog.msg.IDialogHint;

/**
 * 真正的弹窗实现类
 * <br>Created by Soybeany on 2020/4/2.
 */
public interface IRealDialog {

    /**
     * 弹窗是否正在显示
     */
    boolean isDialogShowing();

    /**
     * 改变弹窗提示语
     *
     * @param cancelable 总体是否可取消，即栈中不包含“不可取消的消息”
     */
    void onChangeDialogHint(IDialogHint hint, boolean cancelable);

    /**
     * 显示弹窗
     */
    void onShowDialog();

    /**
     * 更新进度
     */
    void onToProgress(float percent);

    /**
     * 关闭弹窗
     */
    void onDismissDialog(DialogDismissReason reason);
}
