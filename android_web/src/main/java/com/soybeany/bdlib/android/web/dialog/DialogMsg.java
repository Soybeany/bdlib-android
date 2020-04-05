package com.soybeany.bdlib.android.web.dialog;

import com.soybeany.bdlib.android.util.dialog.DialogDismissReason;
import com.soybeany.bdlib.android.util.dialog.msg.IDialogHint;
import com.soybeany.connector.Msg;

/**
 * <br>Created by Soybeany on 2019/5/27.
 */
@SuppressWarnings("WeakerAccess")
public class DialogMsg {

    // //////////////////////////////////模板区//////////////////////////////////

    public static class Invoker<Data> extends Msg.I<Data> {
        public Invoker(Data data) {
            super(data);
        }
    }

    public static class Callback<Data> extends Msg.C<Data> {
        public Callback(Data data) {
            super(data);
        }
    }

    // //////////////////////////////////Invoker区//////////////////////////////////

    /**
     * 显示信息，data为{@link IDialogHint}
     */
    public static class ShowMsg extends Invoker<IDialogHint> {
        public ShowMsg(IDialogHint msg) {
            super(msg);
        }
    }

    /**
     * 弹出信息，data为{@link IDialogHint}
     */
    public static class PopMsg extends Invoker<IDialogHint> {
        public PopMsg(IDialogHint msg) {
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

        public ToProgress percent(Float percent) {
            data = percent;
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
     * 显示信息时通知，data为{@link IDialogHint}
     */
    public static class OnShowMsg extends Callback<IDialogHint> {
        public OnShowMsg(IDialogHint msg) {
            super(msg);
        }
    }

    /**
     * 弹出信息时通知，data为{@link IDialogHint}
     */
    public static class OnPopMsg extends Callback<IDialogHint> {
        public OnPopMsg(IDialogHint msg) {
            super(msg);
        }
    }

    /**
     * 跳转进度时通知，data为{@link Float}(正常 0~1，缺失-1)
     */
    public static class OnToProgress extends Callback<Float> {
        public OnToProgress() {
            super(null);
        }

        public OnToProgress percent(Float percent) {
            data = percent;
            return this;
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

    /**
     * 弹窗销毁时通知，一般为相应activity被销毁时触发，data为null
     */
    public static class OnDestroyDialog extends Callback<Object> {
        public OnDestroyDialog() {
            super(null);
        }
    }
}
