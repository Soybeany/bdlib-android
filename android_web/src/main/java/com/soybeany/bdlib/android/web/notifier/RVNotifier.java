package com.soybeany.bdlib.android.web.notifier;

import com.soybeany.bdlib.android.web.msg.RVMsg;
import com.soybeany.bdlib.android.web.msg.RequestMsg;

import java.util.List;

/**
 * <br>Created by Soybeany on 2020/4/7.
 */
public class RVNotifier extends BaseNotifier<RVMsg.Invoker, RVMsg.Callback, RequestMsg.Invoker> {

    @Override
    protected void onSetupMsgConverters(List<MsgConverter<? extends RVMsg.Callback, RequestMsg.Invoker>> list) {
        list.add(new MsgConverter<>(RVMsg.OnNeedCancel.class, msg -> new RequestMsg.Cancel()));
    }

}
