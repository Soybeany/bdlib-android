package com.soybeany.bdlib.android.web.auth;

import com.soybeany.bdlib.android.util.dialog.msg.IDialogMsg;
import com.soybeany.bdlib.android.web.dialog.IDialogMsgProvider;
import com.soybeany.bdlib.android.web.dialog.RequestDialogConnector;
import com.soybeany.bdlib.core.util.notify.INotifyMsg;

import static com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg.TYPE_POP_MSG;
import static com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg.TYPE_SHOW_MSG;

/**
 * <br>Created by Soybeany on 2019/5/8.
 */
public class ReLoginConnector extends RequestDialogConnector {
    public static final String WHEN_ON_RE_AUTH = "onReAuth";
    public static final String WHEN_ON_RE_REQUEST = "onReRequest";

    private IDialogMsg mReAuthMsg;
    private IDialogMsg mReRequestMsg;

    @Override
    public void onBindDialogMsgProvider(IDialogMsgProvider msgProvider) {
        mReAuthMsg = msgProvider.getMsg(WHEN_ON_RE_AUTH);
        mReRequestMsg = msgProvider.getMsg(WHEN_ON_RE_REQUEST);
    }

    @Override
    public void onCall(INotifyMsg msg) {
        super.onCall(msg);
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
        }
    }

    private void onReAuthStart() {
        dialogInvoke(TYPE_SHOW_MSG, mReAuthMsg);
    }

    private void onReAuthFinish() {
        dialogInvoke(TYPE_POP_MSG, mReAuthMsg);
    }

    private void onReRequestStart() {
        dialogInvoke(TYPE_SHOW_MSG, mReRequestMsg);
    }

    private void onReRequestFinish() {
        dialogInvoke(TYPE_POP_MSG, mReRequestMsg);
    }
}
