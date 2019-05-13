package com.soybeany.bdlib.android.web.dialog;

import com.soybeany.bdlib.android.util.dialog.msg.DialogCallbackMsg;
import com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg;
import com.soybeany.bdlib.android.util.dialog.msg.IDialogMsg;
import com.soybeany.bdlib.core.util.notify.Notifier;
import com.soybeany.bdlib.web.okhttp.notify.RequestCallDealer;
import com.soybeany.bdlib.web.okhttp.notify.RequestCallbackMsg;
import com.soybeany.bdlib.web.okhttp.notify.RequestFinishReason;
import com.soybeany.bdlib.web.okhttp.notify.RequestInvokerMsg;

import static com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg.TYPE_POP_MSG;
import static com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg.TYPE_SHOW_MSG;
import static com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg.TYPE_TO_PROGRESS;

/**
 * Request信息转Dialog信息
 */
public class StdRequestCallDealer extends RequestCallDealer implements IRequestOnCallDealer {
    public static final String WHEN_ON_START = "onStart";

    private RequestPartDelegate mDelegate = new RequestPartDelegate();
    private IDialogMsg mDialogMsg;

    @Override
    public void onBindDialog(Notifier<RequestInvokerMsg, RequestCallbackMsg> requestNotifier, Notifier<DialogInvokerMsg, DialogCallbackMsg> dialogNotifier, IDialogMsgProvider provider) {
        mDelegate.onBindDialog(requestNotifier, dialogNotifier, provider);
        mDialogMsg = provider.getMsg(WHEN_ON_START);
    }

    @Override
    protected void onStart() {
        mDelegate.dialogInvoke(TYPE_SHOW_MSG, mDialogMsg);
    }

    @Override
    protected void onUpload(float v) {
        mDelegate.dialogInvoke(TYPE_TO_PROGRESS, v);
    }

    @Override
    protected void onDownload(float v) {
        mDelegate.dialogInvoke(TYPE_TO_PROGRESS, v);
    }

    @Override
    protected void onFinish(RequestFinishReason requestFinishReason) {
        mDelegate.dialogInvoke(TYPE_POP_MSG, mDialogMsg);
        mDelegate.removeDealer(this);
    }
}
