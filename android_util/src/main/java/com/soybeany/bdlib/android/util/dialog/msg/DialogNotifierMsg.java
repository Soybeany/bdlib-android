package com.soybeany.bdlib.android.util.dialog.msg;

import com.soybeany.bdlib.android.util.dialog.DialogDismissReason;
import com.soybeany.bdlib.core.util.notify.INotifyMsg;

/**
 * <br>Created by Soybeany on 2019/5/27.
 */
public class DialogNotifierMsg {

    // //////////////////////////////////模板区//////////////////////////////////

    public static class Invoker<Data> extends INotifyMsg.InvokerImpl<Data> {
        public Invoker(Data data) {
            super(data);
        }
    }

    public static class Callback<Data> extends INotifyMsg.CallbackImpl<Data> {
        public Callback(Data data) {
            super(data);
        }
    }

    // //////////////////////////////////Invoker区//////////////////////////////////

    /**
     * 显示信息，data为{@link IDialogMsg}
     */
    public static class ShowMsg extends Invoker<IDialogMsg> {
        public ShowMsg(IDialogMsg msg) {
            super(msg);
        }
    }

    /**
     * 弹出信息，data为{@link IDialogMsg}
     */
    public static class PopMsg extends Invoker<IDialogMsg> {
        public PopMsg(IDialogMsg msg) {
            super(msg);
        }
    }

    /**
     * 跳转至进度，data为{@link Float}(正常 0~1，缺失-1)
     */
    public static class ToProgress extends Invoker<Float> {
        public ToProgress() {
            super(null);
        }

        public ToProgress toPercent(Float percent) {
            setData(percent);
            return this;
        }
    }

    /**
     * 关闭弹窗，data为{@link DialogDismissReason}
     */
    public static class DismissDialog extends Invoker<DialogDismissReason> {
        public DismissDialog(DialogDismissReason reason) {
            super(reason);
        }
    }

    // //////////////////////////////////Callback区//////////////////////////////////

    /**
     * 显示信息时通知，data为{@link IDialogMsg}
     */
    public static class OnShow extends Callback<IDialogMsg> {
        public OnShow(IDialogMsg msg) {
            super(msg);
        }
    }

    /**
     * 弹出信息时通知，data为{@link IDialogMsg}
     */
    public static class OnPop extends Callback<IDialogMsg> {
        public OnPop(IDialogMsg msg) {
            super(msg);
        }
    }

    /**
     * 跳转进度时通知，data为{@link Float}(正常 0~1，缺失-1)
     */
    public static class OnProgress extends Callback<Float> {
        public OnProgress() {
            super(null);
        }
    }

    /**
     * 弹窗打开时通知，data为null
     */
    public static class OnShowDialog extends Callback<Object> {
        public OnShowDialog() {
            super(null);
        }
    }

    /**
     * 弹窗关闭时通知，data为{@link DialogDismissReason}
     */
    public static class OnDismissDialog extends Callback<DialogDismissReason> {
        public OnDismissDialog(DialogDismissReason reason) {
            super(reason);
        }
    }
}
