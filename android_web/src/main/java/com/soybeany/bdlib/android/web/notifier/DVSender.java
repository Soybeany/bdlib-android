package com.soybeany.bdlib.android.web.notifier;

import com.soybeany.bdlib.android.web.msg.DMsg;
import com.soybeany.bdlib.android.web.msg.DVMsg;

import java.util.List;

/**
 * <br>Created by Soybeany on 2020/4/7.
 */
public class DVSender extends BaseSender<DVMsg.Invoker, DVMsg.Callback, DMsg.Invoker> {

    @Override
    protected void onSetupMsgConverters(List<MsgConverter<? extends DVMsg.Callback, DMsg.Invoker>> list) {
        list.add(new MsgConverter<>(DVMsg.OnNeedShowDialog.class, msg -> new DMsg.ShowDialog()));
        list.add(new MsgConverter<>(DVMsg.OnNeedDismissDialog.class, msg -> new DMsg.DismissDialog(msg.data)));
        list.add(new MsgConverter<>(DVMsg.OnSelectMsg.class, msg -> new DMsg.DisplayMsg(msg.data)));
        list.add(new MsgConverter<>(DVMsg.OnSwitchCancelable.class, msg -> new DMsg.ChangeCancelable(msg.data)));
        list.add(new MsgConverter<>(DVMsg.OnToProgress.class, msg -> new DMsg.ChangeProgress(msg.data)));
    }

}
