package com.soybeany.bdlib.android.web.notifier;

import com.soybeany.bdlib.android.web.msg.DVMsg;
import com.soybeany.bdlib.android.web.msg.RMsg;

/**
 * <br>Created by Soybeany on 2020/4/8.
 */
public class DialogNotifier {

    public BaseSender<?, ?, RMsg.Invoker<?>> receiver;
    public BaseSender<DVMsg.Invoker<?>, ?, ?> sender;

    public DialogNotifier(BaseSender<?, ?, RMsg.Invoker<?>> receiver, BaseSender<DVMsg.Invoker<?>, ?, ?> sender) {
        this.receiver = receiver;
        this.sender = sender;
    }
}
