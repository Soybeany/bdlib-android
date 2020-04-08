package com.soybeany.bdlib.android.web.notifier;

import com.soybeany.bdlib.android.util.dialog.DialogDismissReason;
import com.soybeany.bdlib.android.web.msg.DMsg;
import com.soybeany.bdlib.android.web.msg.RMsg;

import java.util.List;

/**
 * <br>Created by Soybeany on 2020/4/2.
 */
public class DNotifier extends BaseNotifier<DMsg.Invoker, DMsg.Callback, RMsg.Invoker> {

    @Override
    protected void onSetupMsgConverters(List<MsgConverter<? extends DMsg.Callback, RMsg.Invoker>> list) {
        list.add(new MsgConverter<>(DMsg.OnDismissDialog.class,
                msg -> DialogDismissReason.CANCEL == msg.data ? new RMsg.Cancel() : null)
        );
    }

}
