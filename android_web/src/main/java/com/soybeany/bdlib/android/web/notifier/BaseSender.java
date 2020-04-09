package com.soybeany.bdlib.android.web.notifier;

import com.soybeany.connector.Msg;
import com.soybeany.connector.MsgCenter;
import com.soybeany.connector.MsgSender;

/**
 * <br>Created by Soybeany on 2020/4/3.
 */
public abstract class BaseSender<IMsg1 extends Msg.I, CMsg1 extends Msg.C, IMsg2 extends Msg.I> extends MsgSender<CMsg1, IMsg2> {

    public void sendIMsg(IMsg1 msg) {
        MsgCenter.sendMsg(this.cKey, msg);
    }

}
