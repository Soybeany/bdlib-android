package com.soybeany.bdlib.android.web.notifier;

import com.soybeany.bdlib.android.util.dialog.DialogDismissReason;
import com.soybeany.bdlib.android.web.msg.DialogMsg;
import com.soybeany.bdlib.android.web.msg.RVMsg;

import java.util.List;

/**
 * <br>Created by Soybeany on 2020/4/2.
 */
public class DialogNotifier extends BaseNotifier<DialogMsg.Invoker, DialogMsg.Callback, RVMsg.Invoker> {

    @Override
    protected void onSetupMsgConverters(List<MsgConverter<? extends DialogMsg.Callback, RVMsg.Invoker>> list) {
        list.add(new MsgConverter<>(DialogMsg.OnDismissDialog.class,
                msg -> DialogDismissReason.CANCEL == msg.data ? new RVMsg.NeedCancel() : null)
        );
    }

}
