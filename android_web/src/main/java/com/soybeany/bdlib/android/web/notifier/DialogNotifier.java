package com.soybeany.bdlib.android.web.notifier;

import com.soybeany.bdlib.android.web.msg.DialogMsg;
import com.soybeany.bdlib.android.web.msg.InfoMsg;

import java.util.List;

/**
 * <br>Created by Soybeany on 2020/4/7.
 */
public class DialogNotifier extends BaseNotifier<DialogMsg.Invoker, DialogMsg.Callback, InfoMsg.Invoker> {
    @Override
    protected void onSetupMsgConverters(List<MsgConverter<? extends DialogMsg.Callback, InfoMsg.Invoker>> list) {
        list.add(new MsgConverter<>(DialogMsg.OnNeedShowDialog.class, msg -> new InfoMsg.ShowDialog()));
        list.add(new MsgConverter<>(DialogMsg.OnNeedDismissDialog.class, msg -> new InfoMsg.DismissDialog(msg.data)));
        list.add(new MsgConverter<>(DialogMsg.OnSelectMsg.class, msg -> new InfoMsg.DisplayMsg(msg.data)));
        list.add(new MsgConverter<>(DialogMsg.OnSwitchCancelable.class, msg -> new InfoMsg.ChangeCancelable(msg.data)));
    }
}
