package com.soybeany.bdlib.android.web;

import com.soybeany.bdlib.android.web.dialog.DialogMsg;
import com.soybeany.bdlib.android.web.okhttp.RequestMsg;

import java.util.List;

/**
 * <br>Created by Soybeany on 2020/4/2.
 */
public class DialogNotifier extends BaseNotifier<DialogMsg.Invoker, DialogMsg.Callback, RequestMsg.Invoker> {

    @Override
    protected void onSetupMsgConverters(List<MsgConverter<? extends DialogMsg.Callback, RequestMsg.Invoker>> list) {
        list.add(new MsgConverter<>(DialogMsg.OnDismissDialog.class, msg -> new RequestMsg.Cancel()));
    }
}
