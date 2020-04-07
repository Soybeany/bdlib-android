package com.soybeany.bdlib.android.web.notifier;

import com.soybeany.bdlib.android.util.dialog.DialogDismissReason;
import com.soybeany.bdlib.android.web.msg.InfoMsg;
import com.soybeany.bdlib.android.web.msg.RequestMsg;

import java.util.List;

/**
 * <br>Created by Soybeany on 2020/4/2.
 */
public class InfoNotifier extends BaseNotifier<InfoMsg.Invoker, InfoMsg.Callback, RequestMsg.Invoker> {

    @Override
    protected void onSetupMsgConverters(List<MsgConverter<? extends InfoMsg.Callback, RequestMsg.Invoker>> list) {
        list.add(new MsgConverter<>(InfoMsg.OnDismissDialog.class,
                msg -> DialogDismissReason.CANCEL == msg.data ? new RequestMsg.Cancel() : null));
    }
}
