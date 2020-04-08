package com.soybeany.bdlib.android.web.msg;

import com.soybeany.bdlib.android.util.dialog.DialogDismissReason;
import com.soybeany.bdlib.android.util.dialog.msg.IDialogHint;
import com.soybeany.connector.Msg;

/**
 * <br>Created by Soybeany on 2020/4/7.
 */
@SuppressWarnings("WeakerAccess")
public class DVMsg {

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
    public static class PushMsg extends Invoker<IDialogHint> {
        public PushMsg(IDialogHint msg) {
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
     * 清除信息
     */
    public static class ClearMsg extends Invoker<DialogDismissReason> {
        public ClearMsg(DialogDismissReason reason) {
            super(reason);
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

    // //////////////////////////////////Callback区//////////////////////////////////

    /**
     * 显示信息时通知，data为{@link IDialogHint}
     */
    public static class OnPushMsg extends Callback<IDialogHint> {
        public OnPushMsg(IDialogHint msg) {
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
     * 弹出信息时通知，data为{@link IDialogHint}
     */
    public static class OnNeedShowDialog extends Callback<Object> {
        public OnNeedShowDialog() {
            super(null);
        }
    }

    /**
     * 清除信息时通知
     */
    public static class OnClearMsg extends Callback<DialogDismissReason> {
        public OnClearMsg(DialogDismissReason reason) {
            super(reason);
        }
    }

    /**
     * 弹出信息时通知
     */
    public static class OnNeedDismissDialog extends Callback<DialogDismissReason> {
        public OnNeedDismissDialog(DialogDismissReason reason) {
            super(reason);
        }
    }

    /**
     * 弹出信息时通知，data为{@link IDialogHint}
     */
    public static class OnSelectMsg extends Callback<IDialogHint> {
        public OnSelectMsg(IDialogHint msg) {
            super(msg);
        }
    }

    /**
     * 弹出信息时通知
     */
    public static class OnSwitchCancelable extends Callback<Boolean> {
        public OnSwitchCancelable(Boolean flag) {
            super(flag);
        }
    }

    /**
     * 跳转至进度时通知
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

}
