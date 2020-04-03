package com.soybeany.bdlib.android.web;

import com.soybeany.bdlib.android.util.dialog.msg.IDialogHint;
import com.soybeany.bdlib.android.web.dialog.DialogMsg;
import com.soybeany.bdlib.android.web.okhttp.RequestMsg;
import com.soybeany.connector.MsgManager;

import java.util.List;

/**
 * 禁止将一个{@link RequestNotifier}同时绑定到多个{@link MsgManager}中，因为内部重用了{@link DialogMsg.ToProgress}
 * <br>Created by Soybeany on 2020/4/2.
 */
public class RequestNotifier extends BaseNotifier<RequestMsg.Invoker, RequestMsg.Callback, DialogMsg.Invoker> {

    private final DialogMsg.ToProgress mProgressMsg = new DialogMsg.ToProgress();
    private final IDialogHint mHint;

    RequestNotifier(IDialogHint hint) {
        mHint = hint;
    }

    @Override
    protected void onSetupMsgConverters(List<MsgConverter<? extends RequestMsg.Callback, DialogMsg.Invoker>> list) {
        list.add(new MsgConverter<>(RequestMsg.OnStart.class, msg -> new DialogMsg.ShowMsg(mHint)));
        list.add(new MsgConverter<>(RequestMsg.OnFinish.class, msg -> new DialogMsg.PopMsg(mHint)));
        list.add(new MsgConverter<>(RequestMsg.OnDownload.class, msg -> mProgressMsg.percent(msg.getData())));
        list.add(new MsgConverter<>(RequestMsg.OnUpload.class, msg -> mProgressMsg.percent(msg.getData())));
    }
}