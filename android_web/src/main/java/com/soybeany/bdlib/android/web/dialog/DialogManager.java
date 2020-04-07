package com.soybeany.bdlib.android.web.dialog;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;

import com.soybeany.bdlib.android.util.IObserver;
import com.soybeany.bdlib.android.util.dialog.IRealDialog;
import com.soybeany.bdlib.android.web.msg.DialogMsg;
import com.soybeany.bdlib.android.web.msg.InfoMsg;
import com.soybeany.bdlib.android.web.notifier.DialogNotifier;
import com.soybeany.bdlib.android.web.notifier.InfoNotifier;
import com.soybeany.connector.ITarget;
import com.soybeany.connector.MsgManager;
import com.soybeany.connector.MsgSender;

import java.util.List;

/**
 * 弹窗管理器，负责单弹窗多消息的处理
 * <br>Created by Soybeany on 2020/4/2.
 */
@SuppressWarnings("WeakerAccess")
public class DialogManager implements ITarget<InfoMsg.Invoker>, IObserver {

    public final InfoNotifier iNotifier = new InfoNotifier();
    public final DialogNotifier dNotifier;

    private final MsgManager<InfoMsg.Invoker, InfoMsg.Callback> mIManager = new MsgManager<>();
    private final MsgManager<DialogMsg.Invoker, DialogMsg.Callback> mDManager = new MsgManager<>();

    protected final DialogInfoManager mInfo;
    protected final IRealDialog mRealDialog;

    public DialogManager(String type, @NonNull DialogInfoVM vm, @NonNull IRealDialog realDialog) {
        mInfo = vm.getInfoManager(type);
        dNotifier = mInfo.notifier;
        mRealDialog = realDialog;
        MsgSender.connect(iNotifier, dNotifier);
        mIManager.bind(this, iNotifier, false);
        mDManager.bind(new DialogMsgTarget(), dNotifier, false);
        if (realDialog instanceof INotifierRealDialog) {
            ((INotifierRealDialog) realDialog).onSetupNotifier(iNotifier);
        }
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        mInfo.hasDialogShowing = false;
        mIManager.unbind(false);
        mDManager.unbind(false);
        MsgSender.disconnect(iNotifier, dNotifier);
    }

    @Override
    public void onSetupMsgProcessors(List<MsgProcessor<? extends InfoMsg.Invoker>> list) {
        list.add(new MsgProcessor<>(InfoMsg.ShowDialog.class, msg -> {
            mRealDialog.onShowDialog();
            iNotifier.sendCMsg(msg.senderUid, new InfoMsg.OnShowDialog());
        }));
        list.add(new MsgProcessor<>(InfoMsg.DismissDialog.class, msg -> {
            mRealDialog.onDismissDialog(msg.data);
            iNotifier.sendCMsg(msg.senderUid, new InfoMsg.OnDismissDialog(msg.data));
        }));
        list.add(new MsgProcessor<>(InfoMsg.DisplayMsg.class, msg -> {
            mRealDialog.onDisplayHint(msg.data);
            iNotifier.sendCMsg(msg.senderUid, new InfoMsg.OnDisplayMsg(msg.data));
        }));
        list.add(new MsgProcessor<>(InfoMsg.ChangeCancelable.class, msg -> {
            mRealDialog.onChangeCancelable(msg.data);
            iNotifier.sendCMsg(msg.senderUid, new InfoMsg.OnChangeCancelable(msg.data));
        }));
    }

    // //////////////////////////////////内部类//////////////////////////////////

    private class DialogMsgTarget implements ITarget<DialogMsg.Invoker> {
        private final InfoMsg.OnToProgress mOnToProgress = new InfoMsg.OnToProgress();

        @Override
        public void onSetupMsgProcessors(List<MsgProcessor<? extends DialogMsg.Invoker>> list) {
            list.add(new MsgProcessor<>(DialogMsg.ToProgress.class, msg -> {
                mRealDialog.onToProgress(msg.data);
                iNotifier.sendCMsg(msg.senderUid, mOnToProgress.percent(msg.data));
            }));
        }
    }

}
