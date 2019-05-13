package com.soybeany.bdlib.android.web.dialog;

import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.util.dialog.DialogDismissReason;
import com.soybeany.bdlib.android.util.dialog.msg.IDialogMsg;
import com.soybeany.bdlib.core.util.notify.INotifyMsg;
import com.soybeany.bdlib.web.okhttp.notify.RequestCallbackMsg;
import com.soybeany.bdlib.web.okhttp.notify.RequestFinishReason;
import com.soybeany.bdlib.web.okhttp.notify.RequestInvokerMsg;

import static com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg.TYPE_POP_MSG;
import static com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg.TYPE_SHOW_MSG;
import static com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg.TYPE_TO_PROGRESS;

/**
 * <br>Created by Soybeany on 2019/5/13.
 */
public class StdConnector extends RequestDialogConnector {
    public static final String WHEN_ON_START = "onStart";

    @Nullable
    private IDialogMsg mDialogMsg;

    @Override
    public void onBindDialogMsgProvider(IDialogMsgProvider msgProvider) {
        mDialogMsg = msgProvider.getMsg(WHEN_ON_START);
    }

    @Override
    public void onCall(INotifyMsg msg) {
        super.onCall(msg);
        switch (msg.getType()) {
            case RequestCallbackMsg.TYPE_ON_START:
                onStart();
                break;
            case RequestCallbackMsg.TYPE_ON_UPLOAD:
                onUpload((Float) msg.getData());
                break;
            case RequestCallbackMsg.TYPE_ON_DOWNLOAD:
                onDownload((Float) msg.getData());
                break;
        }
    }

    // //////////////////////////////////弹窗部分//////////////////////////////////

    protected void onDismissDialog(DialogDismissReason reason) {
        super.onDismissDialog(reason);
        requestInvoke(RequestInvokerMsg.CANCEL_MSG);
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

    protected void onFinish(RequestFinishReason requestFinishReason) {
        super.onFinish(requestFinishReason);
        dialogInvoke(TYPE_POP_MSG, mDialogMsg);
    }
}
