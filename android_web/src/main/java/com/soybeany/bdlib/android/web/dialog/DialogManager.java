package com.soybeany.bdlib.android.web.dialog;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;

import com.soybeany.bdlib.android.util.IObserver;
import com.soybeany.bdlib.android.util.dialog.DialogDismissReason;
import com.soybeany.bdlib.android.util.dialog.IRealDialog;
import com.soybeany.bdlib.android.web.msg.DialogMsg;
import com.soybeany.bdlib.android.web.notifier.DVNotifier;
import com.soybeany.bdlib.android.web.notifier.DialogNotifier;
import com.soybeany.connector.ITarget;
import com.soybeany.connector.MsgManager;

import java.util.List;

import static com.soybeany.bdlib.android.util.BDContext.MAIN_HANDLER;

/**
 * 弹窗管理器，负责单弹窗多消息的处理
 * <br>Created by Soybeany on 2020/4/2.
 */
@SuppressWarnings("WeakerAccess")
public class DialogManager implements ITarget<DialogMsg.Invoker>, IObserver {

    public final DVNotifier dvNotifier;

    private final MsgManager<DialogMsg.Invoker, DialogMsg.Callback> mIManager = new MsgManager<>();
    private final DialogMsg.OnChangeProgress mProgress = new DialogMsg.OnChangeProgress();

    private final DialogInfoManager mManager;
    private final IRealDialog mRealDialog;

    private final DialogNotifier mDialogNotifier;

    public DialogManager(@NonNull DialogInfoManager manager, @NonNull IRealDialog realDialog) {
        // 赋值
        mManager = manager;
        dvNotifier = mManager.dvNotifier;
        mDialogNotifier = mManager.dialogNotifier;
        mRealDialog = realDialog;
        // 开始连接
        mManager.connect();
        // 绑定
        mIManager.bind(this, mDialogNotifier, false);
        // 设置Notifier
        if (realDialog instanceof INotifierRealDialog) {
            ((INotifierRealDialog) realDialog).onSetupNotifier(mDialogNotifier);
        }
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        // 按需发送回调
        if (mManager.hasDialogShowing) {
            MAIN_HANDLER.post(() -> mDialogNotifier.sendCMsgWithDefaultUid(new DialogMsg.OnDismissDialog(DialogDismissReason.DESTROY)));
            mManager.hasDialogShowing = false;
        }
        // 解绑
        mIManager.unbind(false);
        // 断开连接
        mManager.disconnect();
    }

    @Override
    public void onSetupMsgProcessors(List<MsgProcessor<? extends DialogMsg.Invoker>> list) {
        list.add(new MsgProcessor<>(DialogMsg.ShowDialog.class, msg -> {
            mRealDialog.onShowDialog();
            mDialogNotifier.sendCMsgWithDefaultUid(new DialogMsg.OnShowDialog());
        }));
        list.add(new MsgProcessor<>(DialogMsg.DismissDialog.class, msg -> {
            mRealDialog.onDismissDialog(msg.data);
            mDialogNotifier.sendCMsgWithDefaultUid(new DialogMsg.OnDismissDialog(msg.data));
        }));
        list.add(new MsgProcessor<>(DialogMsg.DisplayMsg.class, msg -> {
            mRealDialog.onDisplayHint(msg.data);
            mDialogNotifier.sendCMsgWithDefaultUid(new DialogMsg.OnDisplayMsg(msg.data));
        }));
        list.add(new MsgProcessor<>(DialogMsg.ChangeCancelable.class, msg -> {
            mRealDialog.onChangeCancelable(msg.data);
            mDialogNotifier.sendCMsgWithDefaultUid(new DialogMsg.OnChangeCancelable(msg.data));
        }));
        list.add(new MsgProcessor<>(DialogMsg.ChangeProgress.class, msg -> {
            mRealDialog.onToProgress(msg.data);
            mDialogNotifier.sendCMsgWithDefaultUid(mProgress.percent(msg.data));
        }));
    }

}
