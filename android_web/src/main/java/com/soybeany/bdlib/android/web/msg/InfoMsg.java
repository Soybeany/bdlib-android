package com.soybeany.bdlib.android.web.msg;

import com.soybeany.bdlib.android.util.dialog.DialogDismissReason;
import com.soybeany.bdlib.android.util.dialog.msg.IDialogHint;
import com.soybeany.connector.Msg;

/**
 * <br>Created by Soybeany on 2019/5/27.
 */
@SuppressWarnings("WeakerAccess")
public class InfoMsg {

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
     * 显示弹窗
     */
    public static class ShowDialog extends Invoker<Object> {
        public ShowDialog() {
            super(null);
        }
    }

    /**
     * 关闭弹窗
     */
    public static class DismissDialog extends Invoker<DialogDismissReason> {
        public DismissDialog(DialogDismissReason reason) {
            super(reason);
        }
    }

    /**
     * 用于展示的消息
     */
    public static class DisplayMsg extends Invoker<IDialogHint> {
        public DisplayMsg(IDialogHint msg) {
            super(msg);
        }
    }

    /**
     * 更改可取消性
     */
    public static class ChangeCancelable extends Invoker<Boolean> {
        public ChangeCancelable(Boolean flag) {
            super(flag);
        }
    }

    // //////////////////////////////////Callback区//////////////////////////////////

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
     * 显示消息时通知
     */
    public static class OnDisplayMsg extends Callback<IDialogHint> {
        public OnDisplayMsg(IDialogHint hint) {
            super(hint);
        }
    }

    /**
     * 弹出信息时通知
     */
    public static class OnChangeCancelable extends Callback<Boolean> {
        public OnChangeCancelable(Boolean flag) {
            super(flag);
        }
    }

}
