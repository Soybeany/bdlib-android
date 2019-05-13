package com.soybeany.bdlib.android.util.dialog;

import com.soybeany.bdlib.android.util.dialog.msg.DialogCallbackMsg;
import com.soybeany.bdlib.android.util.dialog.msg.IDialogMsg;
import com.soybeany.bdlib.core.util.notify.INotifyMsg;
import com.soybeany.bdlib.core.util.notify.IOnCallDealer;

/**
 * <br>Created by Soybeany on 2019/5/9.
 */
public abstract class DialogCallDealer implements IOnCallDealer {
    @Override
    public void onCall(INotifyMsg msg) {
        switch (msg.getType()) {
            case DialogCallbackMsg.TYPE_ON_SHOW_DIALOG:
                onShowDialog();
                break;
            case DialogCallbackMsg.TYPE_ON_DISMISS_DIALOG:
                onDismissDialog((DialogDismissReason) msg.getData());
                break;
            case DialogCallbackMsg.TYPE_ON_SHOW:
                onShowMsg((IDialogMsg) msg.getData());
                break;
            case DialogCallbackMsg.TYPE_ON_POP:
                onPopMsg((IDialogMsg) msg.getData());
                break;
            case DialogCallbackMsg.TYPE_ON_PROGRESS:
                onProgress((Float) msg.getData());
                break;
        }
    }

    protected abstract void onShowDialog();

    protected abstract void onDismissDialog(DialogDismissReason reason);

    protected abstract void onShowMsg(IDialogMsg msg);

    protected abstract void onPopMsg(IDialogMsg msg);

    protected abstract void onProgress(float percent);

    public static class Empty extends DialogCallDealer {
        @Override
        protected void onShowDialog() {
        }

        @Override
        protected void onDismissDialog(DialogDismissReason reason) {
        }

        @Override
        protected void onShowMsg(IDialogMsg msg) {
        }

        @Override
        protected void onPopMsg(IDialogMsg msg) {
        }

        @Override
        protected void onProgress(float percent) {
        }
    }
}
