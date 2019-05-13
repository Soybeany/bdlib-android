package com.soybeany.bdlib.android.web.dialog;

import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.util.dialog.DialogDismissReason;
import com.soybeany.bdlib.android.util.dialog.msg.DialogCallbackMsg;
import com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg;
import com.soybeany.bdlib.android.util.dialog.msg.IDialogMsg;
import com.soybeany.bdlib.core.util.notify.INotifyMsg;
import com.soybeany.bdlib.core.util.notify.Notifier;
import com.soybeany.bdlib.web.okhttp.notify.RequestCallbackMsg;
import com.soybeany.bdlib.web.okhttp.notify.RequestFinishReason;
import com.soybeany.bdlib.web.okhttp.notify.RequestInvokerMsg;

import static com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg.TYPE_POP_MSG;
import static com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg.TYPE_SHOW_MSG;
import static com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg.TYPE_TO_PROGRESS;

/**
 * <br>Created by Soybeany on 2019/5/13.
 */
public class StdBinderOnCallDealer implements IBinderOnCallDealer {
    public static final String WHEN_ON_START = "onStart";

    private final DialogInvokerMsg mInvokerMsg = new DialogInvokerMsg();

    @Nullable
    private Notifier<DialogInvokerMsg, DialogCallbackMsg> mDialogNotifier;
    @Nullable
    private Notifier<RequestInvokerMsg, RequestCallbackMsg> mRequestNotifier;
    @Nullable
    private IDialogMsg mDialogMsg;

    @Override
    public void onBindNotifier(Notifier<DialogInvokerMsg, DialogCallbackMsg> dialogNotifier, Notifier<RequestInvokerMsg, RequestCallbackMsg> requestNotifier) {
        mDialogNotifier = dialogNotifier;
        mRequestNotifier = requestNotifier;
    }

    @Override
    public void onBindDialogMsgProvider(IDialogMsgProvider msgProvider) {
        mDialogMsg = msgProvider.getMsg(WHEN_ON_START);
    }

    @Override
    public void onCall(INotifyMsg msg) {
        switch (msg.getType()) {
            // 弹窗部分
            case DialogCallbackMsg.TYPE_ON_DISMISS_DIALOG:
                onDismissDialog((DialogDismissReason) msg.getData());
                break;

            // 请求部分
            case RequestCallbackMsg.TYPE_ON_START:
                onStart();
                break;
            case RequestCallbackMsg.TYPE_ON_UPLOAD:
                onUpload((Float) msg.getData());
                break;
            case RequestCallbackMsg.TYPE_ON_DOWNLOAD:
                onDownload((Float) msg.getData());
                break;
            case RequestCallbackMsg.TYPE_ON_FINISH:
                onFinish((RequestFinishReason) msg.getData());
                break;
        }
    }

    // //////////////////////////////////弹窗部分//////////////////////////////////

    private void onDismissDialog(DialogDismissReason reason) {
        if (null != mRequestNotifier) {
            mRequestNotifier.invoker().notifyNow(RequestInvokerMsg.CANCEL_MSG);
        }
        // 弹窗部分不再需要监听
        if (null != mDialogNotifier) {
            mDialogNotifier.callback().removeDealer(this);
        }
    }

    // //////////////////////////////////请求部分//////////////////////////////////

    private void onStart() {
        dialogInvoke(TYPE_SHOW_MSG, mDialogMsg);
    }

    private void onUpload(float v) {
        dialogInvoke(TYPE_TO_PROGRESS, v);
    }

    private void onDownload(float v) {
        dialogInvoke(TYPE_TO_PROGRESS, v);
    }

    private void onFinish(RequestFinishReason requestFinishReason) {
        dialogInvoke(TYPE_POP_MSG, mDialogMsg);
        // 请求部分不再需要监听
        if (null != mRequestNotifier) {
            mRequestNotifier.callback().removeDealer(this);
        }
    }

    private void dialogInvoke(String type, Object data) {
        if (null != mDialogNotifier) {
            mDialogNotifier.invoker().notifyNow(mInvokerMsg.type(type).data(data));
        }
    }
}
