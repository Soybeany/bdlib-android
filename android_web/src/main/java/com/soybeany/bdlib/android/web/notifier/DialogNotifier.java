package com.soybeany.bdlib.android.web.notifier;

import com.soybeany.bdlib.android.web.msg.DVMsg;
import com.soybeany.bdlib.android.web.msg.RMsg;

/**
 * <br>Created by Soybeany on 2020/4/8.
 */
public class DialogNotifier {

    public BaseNotifier<?, ?, RMsg.Invoker> receiver;
    public BaseNotifier<DVMsg.Invoker, ?, ?> sender;

    public DialogNotifier(BaseNotifier<?, ?, RMsg.Invoker> receiver, BaseNotifier<DVMsg.Invoker, ?, ?> sender) {
        this.receiver = receiver;
        this.sender = sender;
    }
}
