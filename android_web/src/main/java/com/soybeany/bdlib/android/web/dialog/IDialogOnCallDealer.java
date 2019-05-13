package com.soybeany.bdlib.android.web.dialog;

import com.soybeany.bdlib.android.util.dialog.msg.DialogCallbackMsg;
import com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg;
import com.soybeany.bdlib.core.util.notify.IOnCallDealer;
import com.soybeany.bdlib.core.util.notify.Notifier;
import com.soybeany.bdlib.web.okhttp.notify.RequestCallbackMsg;
import com.soybeany.bdlib.web.okhttp.notify.RequestInvokerMsg;

/**
 * <br>Created by Soybeany on 2019/5/10.
 */
public interface IDialogOnCallDealer extends IOnCallDealer {
    void onBindRequest(Notifier<DialogInvokerMsg, DialogCallbackMsg> dialogNotifier, Notifier<RequestInvokerMsg, RequestCallbackMsg> requestNotifier);
}
