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
        mManager.bind(this, mNotifier);
        if (dialog instanceof INotifierRealDialog) {
            ((INotifierRealDialog) dialog).onSetupNotifier(mNotifier);
        }
    }

    // //////////////////////////////////方法重写//////////////////////////////////

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        mManager.sendMsg(new DialogMsg.OnDismissDialog(DialogDismissReason.CLEAR));
        mManager.sendMsg(new DialogMsg.OnClearDialog());
    }

    @Override
    protected void onShowMsg(IDialogHint msg) {
        super.onShowMsg(msg);
        mManager.sendMsg(new DialogMsg.OnShowMsg(msg));
    }

    @Override
    protected void onPopMsg(IDialogHint msg) {
        super.onPopMsg(msg);
        mManager.sendMsg(new DialogMsg.OnPopMsg(msg));
    }

    @Override
    protected void onToProgress(float percent) {
        super.onToProgress(percent);
        mManager.sendMsg(mOnToProgress.percent(percent));
    }

    @Override
    protected void onShowDialog() {
        super.onShowDialog();
        mManager.sendMsg(new DialogMsg.OnShowDialog());
    }

    @Override
    protected void onDismissDialog(DialogDismissReason reason) {
        super.onDismissDialog(reason);
        mManager.sendMsg(new DialogMsg.OnDismissDialog(reason));
    }

    @Override
    public void onSetupMsgProcessors(List<MsgProcessor<? extends DialogMsg.Invoker>> list) {
        list.add(new MsgProcessor<>(DialogMsg.ShowMsg.class, msg -> showMsg(msg.getData())));
        list.add(new MsgProcessor<>(DialogMsg.PopMsg.class, msg -> popMsg(msg.getData())));
        list.add(new MsgProcessor<>(DialogMsg.ToProgress.class, msg -> toProgress(msg.getData())));
        list.add(new MsgProcessor<>(DialogMsg.DismissDialog.class, msg -> dismiss(msg.getData())));
    }

    // //////////////////////////////////公开方法//////////////////////////////////

    public DialogNotifier getNotifier() {
        return mNotifier;
    }

}
