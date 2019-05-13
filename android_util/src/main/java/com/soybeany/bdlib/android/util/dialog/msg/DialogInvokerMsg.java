package com.soybeany.bdlib.android.util.dialog.msg;

import com.soybeany.bdlib.android.util.dialog.DialogDismissReason;
import com.soybeany.bdlib.core.util.notify.INotifyMsg;

/**
 * <br>Created by Soybeany on 2019/5/8.
 */
public class DialogInvokerMsg extends INotifyMsg.Impl<DialogInvokerMsg> implements INotifyMsg.Invoker {

    /**
     * 显示信息，data为{@link IDialogMsg}
     */
    public static final String TYPE_SHOW_MSG = "showMsg";

    /**
     * 弹出信息，data为{@link IDialogMsg}
     */
    public static final String TYPE_POP_MSG = "popMsg";

    /**
     * 跳转至进度，data为{@link Float}(正常 0~1，缺失-1)
     */
    public static final String TYPE_TO_PROGRESS = "toProgress";

    /**
     * 关闭弹窗，data为{@link DialogDismissReason}
     */
    public static final String TYPE_DISMISS_DIALOG = "dismissDialog";
}
