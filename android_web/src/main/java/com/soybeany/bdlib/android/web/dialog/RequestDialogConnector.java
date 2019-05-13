package com.soybeany.bdlib.android.web.dialog;

import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.util.dialog.msg.DialogCallbackMsg;
import com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg;
import com.soybeany.bdlib.core.util.IterableUtils;
import com.soybeany.bdlib.core.util.notify.Notifier;
import com.soybeany.bdlib.web.okhttp.notify.RequestCallbackMsg;
import com.soybeany.bdlib.web.okhttp.notify.RequestInvokerMsg;

import java.util.HashSet;
import java.util.Set;

/**
 * 请求-弹窗 连接器
 * <br>Created by Soybeany on 2019/5/9.
 */
class RequestDialogConnector {
    final Set<IBinderOnCallDealer> dealers = new HashSet<>();
    @Nullable
    private IDialogMsgProvider mMsgProvider;

    void setDialogMsgProvider(@Nullable IDialogMsgProvider msgProvider) {
        mMsgProvider = msgProvider;
    }

    void connect(@Nullable Notifier<RequestInvokerMsg, RequestCallbackMsg> requestNotifier, @Nullable Notifier<DialogInvokerMsg, DialogCallbackMsg> dialogNotifier) {
        // 信息不完整则不再
        if (dealers.isEmpty() || null == requestNotifier || null == dialogNotifier || null == mMsgProvider) {
            return;
        }
        // 绑定
        IterableUtils.forEach(dealers, (dealer, flag) -> {
            requestNotifier.callback().addDealer(dealer);
            dialogNotifier.callback().addDealer(dealer);
            dealer.onBindNotifier(dialogNotifier, requestNotifier);
            dealer.onBindDialogMsgProvider(mMsgProvider);
        });
    }
}
