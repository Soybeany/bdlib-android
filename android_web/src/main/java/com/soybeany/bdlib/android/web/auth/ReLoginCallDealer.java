package com.soybeany.bdlib.android.web.auth;

import com.soybeany.bdlib.android.util.dialog.msg.DialogCallbackMsg;
import com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg;
import com.soybeany.bdlib.android.util.dialog.msg.IDialogMsg;
import com.soybeany.bdlib.android.web.dialog.IDialogMsgProvider;
import com.soybeany.bdlib.android.web.dialog.IRequestOnCallDealer;
import com.soybeany.bdlib.android.web.dialog.RequestPartDelegate;
import com.soybeany.bdlib.core.util.notify.INotifyMsg;
import com.soybeany.bdlib.core.util.notify.Notifier;
import com.soybeany.bdlib.web.okhttp.notify.RequestCallbackMsg;
import com.soybeany.bdlib.web.okhttp.notify.RequestFinishReason;
import com.soybeany.bdlib.web.okhttp.notify.RequestInvokerMsg;

import static com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg.TYPE_POP_MSG;
import static com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg.TYPE_SHOW_MSG;

/**
 * <br>Created by Soybeany on 2019/5/8.
 */
public class ReLoginCallDealer implements IRequestOnCallDealer {
    public static final String WHEN_ON_RE_AUTH = "onReAuth";
    public static final String WHEN_ON_RE_REQUEST = "onReRequest";

    private RequestPartDelegate mDelegate = new RequestPartDelegate();
    private IDialogMsg mReAuthMsg;
    private IDialogMsg mReRequestMsg;

    @Override
    public void onCall(INotifyMsg msg) {
        switch (msg.getType()) {
            case ReLoginInterceptor.TYPE_ON_RE_AUTH_START:
                onReAuthStart();
                break;
            case ReLoginInterceptor.TYPE_ON_RE_AUTH_FINISH:
                onReAuthFinish();
                break;
            case ReLoginInterceptor.TYPE_ON_RE_REQUEST_START:
                onReRequestStart();
                break;
            case ReLoginInterceptor.TYPE_ON_RE_REQUEST_FINISH:
                onReRequestFinish();
                break;
            case RequestCallbackMsg.TYPE_ON_FINISH:
                onFinish((RequestFinishReason) msg.getData());
                break;
        }
    }

    protected void onReAuthStart() {
        mDelegate.dialogInvoke(TYPE_SHOW_MSG, mReAuthMsg);
    }

    protected void onReAuthFinish() {
        mDelegate.dialogInvoke(TYPE_POP_MSG, mReAuthMsg);
    }

    protected void onReRequestStart() {
        mDelegate.dialogInvoke(TYPE_SHOW_MSG, mReRequestMsg);
    }

    protected void onReRequestFinish() {
        mDelegate.dialogInvoke(TYPE_POP_MSG, mReRequestMsg);
    }

    protected void onFinish(RequestFinishReason reason) {
        mDelegate.removeDealer(this);
    }

    @Override
    public void onBindDialog(Notifier<RequestInvokerMsg, RequestCallbackMsg> requestNotifier, Notifier<DialogInvokerMsg, DialogCallbackMsg> dialogNotifier, IDialogMsgProvider provider) {
        mDelegate.onBindDialog(requestNotifier, dialogNotifier, provider);
        mReAuthMsg = provider.getMsg(WHEN_ON_RE_AUTH);
        mReRequestMsg = provider.getMsg(WHEN_ON_RE_REQUEST);
    }
}
