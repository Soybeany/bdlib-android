package com.soybeany.bdlib.android.web.notifier;

import com.soybeany.bdlib.android.web.msg.DVMsg;
import com.soybeany.bdlib.android.web.msg.DialogMsg;

import java.util.List;

/**
 * <br>Created by Soybeany on 2020/4/7.
 */
public class DVNotifier extends BaseNotifier<DVMsg.Invoker, DVMsg.Callback, DialogMsg.Invoker> {

    @Override
    protected void onSetupMsgConverters(List<MsgConverter<? extends DVMsg.Callback, DialogMsg.Invoker>> list) {
        list.add(new MsgConverter<>(DVMsg.OnNeedShowDialog.class, msg -> new DialogMsg.ShowDialog()));
        list.add(new MsgConverter<>(DVMsg.OnNeedDismissDialog.class, msg -> new DialogMsg.DismissDialog(msg.data)));
        list.add(new MsgConverter<>(DVMsg.OnSelectMsg.class, msg -> new DialogMsg.DisplayMsg(msg.data)));
        list.add(new MsgConverter<>(DVMsg.OnSwitchCancelable.class, msg -> new DialogMsg.ChangeCancelable(msg.data)));
        list.add(new MsgConverter<>(DVMsg.OnToProgress.class, msg -> new DialogMsg.ChangeProgress(msg.data)));
    }

}
