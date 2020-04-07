package com.soybeany.bdlib.android.web.notifier;

import com.soybeany.bdlib.android.util.dialog.msg.IDialogHint;
import com.soybeany.bdlib.android.web.msg.DVMsg;
import com.soybeany.bdlib.android.web.msg.RequestMsg;
import com.soybeany.connector.MsgManager;

import java.util.List;

/**
 * 禁止将一个{@link RequestNotifier}同时绑定到多个{@link MsgManager}中，因为内部重用了{@link DVMsg.ToProgress}
 * <br>Created by Soybeany on 2020/4/2.
 */
public class RequestNotifier extends BaseNotifier<RequestMsg.Invoker, RequestMsg.Callback, DVMsg.Invoker> {

    private final DVMsg.ToProgress mProgressMsg = new DVMsg.ToProgress();
    private final IDialogHint mHint;

    public RequestNotifier(IDialogHint hint) {
        mHint = hint;
    }

    @Override
    protected void onSetupMsgConverters(List<MsgConverter<? extends RequestMsg.Callback, DVMsg.Invoker>> list) {
        list.add(new MsgConverter<>(RequestMsg.OnStart.class, msg -> new DVMsg.PushMsg(mHint)));
        list.add(new MsgConverter<>(RequestMsg.OnFinish.class, msg -> new DVMsg.PopMsg(mHint)));
        list.add(new MsgConverter<>(RequestMsg.OnDownload.class, msg -> mProgressMsg.percent(msg.data)));
        list.add(new MsgConverter<>(RequestMsg.OnUpload.class, msg -> mProgressMsg.percent(msg.data)));
    }

}
