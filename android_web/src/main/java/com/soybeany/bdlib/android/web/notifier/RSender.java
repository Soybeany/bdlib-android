package com.soybeany.bdlib.android.web.notifier;

import com.soybeany.bdlib.android.util.dialog.msg.IDialogHint;
import com.soybeany.bdlib.android.web.msg.DVMsg;
import com.soybeany.bdlib.android.web.msg.RMsg;
import com.soybeany.connector.MsgManager;

import java.util.List;

/**
 * 禁止将一个{@link RSender}同时绑定到多个{@link MsgManager}中，因为内部重用了{@link DVMsg.ToProgress}
 * <br>Created by Soybeany on 2020/4/2.
 */
public class RSender extends BaseSender<RMsg.Invoker, RMsg.Callback, DVMsg.Invoker> {

    private final DVMsg.ToProgress mProgressMsg = new DVMsg.ToProgress();
    private final IDialogHint mHint;

    public RSender(IDialogHint hint) {
        mHint = hint;
    }

    @Override
    protected void onSetupMsgConverters(List<MsgConverter<? extends RMsg.Callback, DVMsg.Invoker>> list) {
        list.add(new MsgConverter<>(RMsg.OnStart.class, msg -> new DVMsg.PushMsg(mHint)));
        list.add(new MsgConverter<>(RMsg.OnFinish.class, msg -> new DVMsg.PopMsg(mHint)));
        list.add(new MsgConverter<>(RMsg.OnDownload.class, msg -> mProgressMsg.percent(msg.data)));
        list.add(new MsgConverter<>(RMsg.OnUpload.class, msg -> mProgressMsg.percent(msg.data)));
    }

}
