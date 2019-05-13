package com.soybeany.bdlib.android.web.dialog;

import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.util.dialog.msg.DialogCallbackMsg;
import com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg;
import com.soybeany.bdlib.core.util.notify.INotifyMsg;
import com.soybeany.bdlib.core.util.notify.IOnCallDealer;
import com.soybeany.bdlib.core.util.notify.Notifier;
import com.soybeany.bdlib.web.okhttp.notify.RequestCallbackMsg;
import com.soybeany.bdlib.web.okhttp.notify.RequestInvokerMsg;

/**
 * <br>Created by Soybeany on 2019/5/10.
 */
public class RequestPartDelegate implements IRequestOnCallDealer {
    private final DialogInvokerMsg mInvokerMsg = new DialogInvokerMsg();
    @Nullable
    private Notifier<RequestInvokerMsg, RequestCallbackMsg> mRequestNotifier;
    @Nullable
    private Notifier<DialogInvokerMsg, DialogCallbackMsg> mDialogNotifier;

    @Override
    public void onBindDialog(Notifier<RequestInvokerMsg, RequestCallbackMsg> requestNotifier, Notifier<DialogInvokerMsg, DialogCallbackMsg> dialogNotifier, IDialogMsgProvider provider) {
        mRequestNotifier = requestNotifier;
        mDialogNotifier = dialogNotifier;
    }

    @Override
    public void onCall(INotifyMsg iNotifyMsg) {
        throw new RuntimeException("不要调用此方法");
    }

    public void dialogInvoke(String type, Object data) {
        if (null != mDialogNotifier) {
            mDialogNotifier.invoker().notifyNow(mInvokerMsg.type(type).data(data));
        }
    }

    public void removeDealer(IOnCallDealer dealer) {
        if (null != mRequestNotifier) {
            mRequestNotifier.callback().removeDealer(dealer);
        }
    }
}
