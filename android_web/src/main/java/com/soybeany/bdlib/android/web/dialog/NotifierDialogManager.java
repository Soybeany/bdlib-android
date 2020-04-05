package com.soybeany.bdlib.android.web.dialog;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;

import com.soybeany.bdlib.android.util.IObserver;
import com.soybeany.bdlib.android.util.dialog.DialogDismissReason;
import com.soybeany.bdlib.android.util.dialog.DialogManager;
import com.soybeany.bdlib.android.util.dialog.IRealDialog;
import com.soybeany.bdlib.android.util.dialog.msg.IDialogHint;
import com.soybeany.bdlib.android.web.DialogNotifier;
import com.soybeany.connector.ITarget;
import com.soybeany.connector.MsgManager;

import java.util.List;

/**
 * <br>Created by Soybeany on 2020/4/2.
 */
public class NotifierDialogManager extends DialogManager implements ITarget<DialogMsg.Invoker>, IObserver {

    private final MsgManager<DialogMsg.Invoker, DialogMsg.Callback> mManager = new MsgManager<>();
    private final DialogMsg.OnToProgress mOnToProgress = new DialogMsg.OnToProgress();
    private final DialogNotifier mNotifier;

    public NotifierDialogManager(String type, @NonNull NotifierDialogInfoVM vm, @NonNull IRealDialog dialog) {
        super(type, vm, dialog);
        mNotifier = vm.getNotifier(type);
        mManager.bind(this, mNotifier, false);
        if (dialog instanceof INotifierRealDialog) {
            ((INotifierRealDialog) dialog).onSetupNotifier(mNotifier);
        }
    }

    // //////////////////////////////////方法重写//////////////////////////////////

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        mNotifier.sendCMsgWithDefaultUid(new DialogMsg.OnDestroyDialog());
        mManager.unbind(false);
    }

    @Override
    protected void onShowMsg(String uid, IDialogHint msg) {
        super.onShowMsg(uid, msg);
        mNotifier.sendCMsg(uid, new DialogMsg.OnShowMsg(msg));
    }

    @Override
    protected void onPopMsg(String uid, IDialogHint msg) {
        super.onPopMsg(uid, msg);
        mNotifier.sendCMsg(uid, new DialogMsg.OnPopMsg(msg));
    }

    @Override
    protected void onToProgress(String uid, float percent) {
        super.onToProgress(uid, percent);
        mNotifier.sendCMsg(uid, mOnToProgress.percent(percent));
    }

    @Override
    protected void onShowDialog(String uid) {
        super.onShowDialog(uid);
        mNotifier.sendCMsg(uid, new DialogMsg.OnShowDialog());
    }

    @Override
    protected void onDismissDialog(String uid, DialogDismissReason reason) {
        super.onDismissDialog(uid, reason);
        mNotifier.sendCMsg(uid, new DialogMsg.OnDismissDialog(reason));
    }

    @Override
    public void onSetupMsgProcessors(List<MsgProcessor<? extends DialogMsg.Invoker>> list) {
        list.add(new MsgProcessor<>(DialogMsg.ShowMsg.class, msg -> showMsg(msg.senderUid, msg.data)));
        list.add(new MsgProcessor<>(DialogMsg.PopMsg.class, msg -> popMsg(msg.senderUid, msg.data)));
        list.add(new MsgProcessor<>(DialogMsg.ToProgress.class, msg -> toProgress(msg.senderUid, msg.data)));
        list.add(new MsgProcessor<>(DialogMsg.DismissDialog.class, msg -> dismissDialog(msg.senderUid, msg.data)));
    }

    // //////////////////////////////////公开方法//////////////////////////////////

    public DialogNotifier getNotifier() {
        return mNotifier;
    }

}
