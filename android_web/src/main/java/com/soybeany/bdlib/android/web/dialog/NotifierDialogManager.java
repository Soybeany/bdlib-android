//package com.soybeany.bdlib.android.web.dialog;
//
//import android.arch.lifecycle.LifecycleOwner;
//import android.support.annotation.NonNull;
//
//import com.soybeany.bdlib.android.util.IObserver;
//import com.soybeany.bdlib.android.util.dialog.DialogDismissReason;
//import com.soybeany.bdlib.android.util.dialog.IRealDialog;
//import com.soybeany.bdlib.android.util.dialog.msg.IDialogHint;
//import com.soybeany.bdlib.android.web.msg.DialogMsg;
//import com.soybeany.bdlib.android.web.notifier.DialogNotifier;
//import com.soybeany.connector.ITarget;
//import com.soybeany.connector.MsgManager;
//
//import java.util.List;
//
///**
// * <br>Created by Soybeany on 2020/4/2.
// */
//public class NotifierDialogManager extends DialogManager implements ITarget<DialogMsg.Invoker>, IObserver {
//
//    private final MsgManager<DialogMsg.Invoker, DialogMsg.Callback> mManager = new MsgManager<>();
//    private final DialogMsg.OnToProgress mOnToProgress = new DialogMsg.OnToProgress();
//    private final DialogNotifier mNotifier;
//
//    public NotifierDialogManager(String type, @NonNull NotifierDialogInfoVM vm, @NonNull IRealDialog dialog) {
//        super(type, vm, dialog);
//        mNotifier = vm.getNotifier(type, onGetInfoFactory());
//        mManager.bind(this, mNotifier, false);
//        if (dialog instanceof INotifierRealDialog) {
//            ((INotifierRealDialog) dialog).onSetupNotifier(mNotifier);
//        }
//    }
//
//    // //////////////////////////////////方法重写//////////////////////////////////
//
//    @Override
//    public void onDestroy(@NonNull LifecycleOwner owner) {
//        mInfo.hasDialogShowing = false;
//        mManager.unbind(false);
//    }
//
//    @Override
//    public void onSetupMsgProcessors(List<MsgProcessor<? extends DialogMsg.Invoker>> list) {
//        list.add(new MsgProcessor<>(DialogMsg.ToProgress.class, msg -> {
//            mRealDialog.onToProgress(msg.data);
//            mNotifier.sendCMsg(msg.senderUid, mOnToProgress.percent(msg.data));
//        }));
//        list.add(new MsgProcessor<>(DialogMsg.ShowDialog.class, msg -> {
//            mRealDialog.onShowDialog();
//            mNotifier.sendCMsg(msg.senderUid, new DialogMsg.OnShowDialog());
//        }));
//        list.add(new MsgProcessor<>(DialogMsg.DismissDialog.class, msg -> {
//            mRealDialog.onDismissDialog(msg.data);
//            mNotifier.sendCMsg(msg.senderUid, new DialogMsg.OnDismissDialog(msg.data));
//        }));
//        list.add(new MsgProcessor<>(DialogMsg.DisplayMsg.class, msg -> {
//            mRealDialog.onDisplayHint(msg.data);
//            mNotifier.sendCMsg(msg.senderUid, new DialogMsg.OnDisplayMsg(msg.data));
//        }));
//        list.add(new MsgProcessor<>(DialogMsg.ChangeCancelable.class, msg -> {
//            mRealDialog.onChangeCancelable(msg.data);
//            mNotifier.sendCMsg(msg.senderUid, new DialogMsg.OnChangeCancelable(msg.data));
//        }));
//    }
//
//    @Override
//    protected DialogInfoManager.IFactory onGetInfoFactory() {
//        return InnerInfo::new;
//    }
//
//    // //////////////////////////////////公开方法//////////////////////////////////
//
//    public DialogNotifier getNotifier() {
//        return mNotifier;
//    }
//
//    // //////////////////////////////////内部类//////////////////////////////////
//
//    private class InnerInfo extends DialogManager.InfoImpl {
//
//        @Override
//        protected void onPushMsg(String uid, IDialogHint msg) {
//            super.onPushMsg(uid, msg);
//            mNotifier.sendCMsg(uid, new DialogMsg.OnPushMsg(msg));
//        }
//
//        @Override
//        protected void onPopMsg(String uid, IDialogHint msg) {
//            super.onPopMsg(uid, msg);
//            mNotifier.sendCMsg(uid, new DialogMsg.OnPopMsg(msg));
//        }
//
//        @Override
//        protected void onSelectMsg(String uid, IDialogHint msg) {
//            super.onSelectMsg(uid, msg);
//            mNotifier.sendCMsg(uid, new DialogMsg.OnSelectMsg(msg));
//        }
//
//        @Override
//        protected void onSwitchCancelable(String uid, boolean cancelable) {
//            super.onSwitchCancelable(uid, cancelable);
//            mNotifier.sendCMsg(uid, new DialogMsg.OnSwitchCancelable(cancelable));
//        }
//
//        @Override
//        protected void onNeedShowDialog(String uid) {
//            super.onNeedShowDialog(uid);
//            mNotifier.sendCMsg(uid, new DialogMsg.OnNeedShowDialog());
//        }
//
//        @Override
//        protected void onNeedDismissDialog(String uid, DialogDismissReason reason) {
//            super.onNeedDismissDialog(uid, reason);
//            mNotifier.sendCMsg(uid, new DialogMsg.OnNeedDismissDialog(reason));
//        }
//    }
//
//}
