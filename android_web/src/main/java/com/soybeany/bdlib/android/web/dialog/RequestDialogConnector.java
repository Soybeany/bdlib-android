package com.soybeany.bdlib.android.web.dialog;

import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.util.dialog.DialogDismissReason;
import com.soybeany.bdlib.android.util.dialog.msg.DialogCallbackMsg;
import com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg;
import com.soybeany.bdlib.core.util.notify.INotifyMsg;
import com.soybeany.bdlib.core.util.notify.IOnCallDealer;
import com.soybeany.bdlib.core.util.notify.Notifier;
import com.soybeany.bdlib.web.okhttp.notify.RequestCallbackMsg;
import com.soybeany.bdlib.web.okhttp.notify.RequestFinishReason;
import com.soybeany.bdlib.web.okhttp.notify.RequestInvokerMsg;

/**
 * <br>Created by Soybeany on 2019/5/10.
 */
public abstract class RequestDialogConnector implements IOnCallDealer {
    private final DialogInvokerMsg mInvokerMsg = new DialogInvokerMsg();

    @Nullable
    private Notifier<DialogInvokerMsg, DialogCallbackMsg> mDialogNotifier;
    @Nullable
    private Notifier<RequestInvokerMsg, RequestCallbackMsg> mRequestNotifier;

    @Override
    public void onCall(INotifyMsg msg) {
        switch (msg.getType()) {
            case DialogCallbackMsg.TYPE_ON_DISMISS_DIALOG:
                onDismissDialog((DialogDismissReason) msg.getData());
                break;
            case RequestCallbackMsg.TYPE_ON_FINISH:
                onFinish((RequestFinishReason) msg.getData());
                break;
        }
    }

    public void onBindNotifier(Notifier<DialogInvokerMsg, DialogCallbackMsg> dialogNotifier, Notifier<RequestInvokerMsg, RequestCallbackMsg> requestNotifier) {
        mDialogNotifier = dialogNotifier;
        mRequestNotifier = requestNotifier;
    }

    protected void requestInvoke(RequestInvokerMsg msg) {
        if (null != mRequestNotifier) {
            mRequestNotifier.invoker().notifyNow(msg);
        }
    }

    protected void dialogInvoke(String type, Object data) {
        if (null != mDialogNotifier) {
            mDialogNotifier.invoker().notifyNow(mInvokerMsg.type(type).data(data));
        }
    }

    protected void onDismissDialog(DialogDismissReason reason) {
        // 弹窗部分不再需要监听
        if (null != mDialogNotifier) {
            mDialogNotifier.callback().removeDealer(this);
        }
    }

    protected void onFinish(RequestFinishReason requestFinishReason) {
        // 请求部分不再需要监听
        if (null != mRequestNotifier) {
            mRequestNotifier.callback().removeDealer(this);
        }
    }

    public abstract void onBindDialogMsgProvider(IDialogMsgProvider msgProvider);
}
