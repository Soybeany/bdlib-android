package com.soybeany.bdlib.android.web.dialog;

import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.util.dialog.DialogCallDealer;
import com.soybeany.bdlib.android.util.dialog.DialogDismissReason;
import com.soybeany.bdlib.android.util.dialog.msg.DialogCallbackMsg;
import com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg;
import com.soybeany.bdlib.core.util.notify.Notifier;
import com.soybeany.bdlib.web.okhttp.notify.RequestCallbackMsg;
import com.soybeany.bdlib.web.okhttp.notify.RequestInvokerMsg;

/**
 * Dialog信息转Request信息
 */
public class StdDialogCallDealer extends DialogCallDealer.Empty implements IDialogOnCallDealer {
    @Nullable
    private Notifier<DialogInvokerMsg, DialogCallbackMsg> mDialogNotifier;
    @Nullable
    private Notifier<RequestInvokerMsg, RequestCallbackMsg> mRequestNotifier;

    @Override
    public void onBindRequest(Notifier<DialogInvokerMsg, DialogCallbackMsg> dialogNotifier, Notifier<RequestInvokerMsg, RequestCallbackMsg> requestNotifier) {
        mDialogNotifier = dialogNotifier;
        mRequestNotifier = requestNotifier;
    }

    @Override
    protected void onDismissDialog(DialogDismissReason reason) {
        if (null != mRequestNotifier) {
            mRequestNotifier.invoker().notifyNow(RequestInvokerMsg.CANCEL_MSG);
        }
        if (null != mDialogNotifier) {
            mDialogNotifier.callback().removeDealer(this);
        }
    }
}
