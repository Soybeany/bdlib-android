package com.soybeany.bdlib.android.util.dialog.msg;

import com.soybeany.bdlib.android.util.dialog.DialogDismissReason;
import com.soybeany.bdlib.core.util.notify.INotifyMsg;

/**
 * <br>Created by Soybeany on 2019/5/8.
 */
public class DialogCallbackMsg extends INotifyMsg.Impl<DialogCallbackMsg> implements INotifyMsg.Callback {

    /**
     * 显示信息时通知，data为{@link IDialogMsg}
     */
    public static final String TYPE_ON_SHOW = "onShow";

    /**
     * 弹出信息时通知，data为{@link IDialogMsg}
     */
    public static final String TYPE_ON_POP = "onPop";

    /**
     * 跳转进度时通知，data为{@link Float}(正常 0~1，缺失-1)
     */
    public static final String TYPE_ON_PROGRESS = "onProgress";

    /**
     * 弹窗打开时通知，data为null
     */
    public static final String TYPE_ON_SHOW_DIALOG = "onShowDialog";

    /**
     * 弹窗关闭时通知，data为{@link DialogDismissReason}
     */
    public static final String TYPE_ON_DISMISS_DIALOG = "onDismissDialog";
}
