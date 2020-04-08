package com.soybeany.bdlib.android.web.dialog;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;

import com.soybeany.bdlib.android.util.IObserver;
import com.soybeany.bdlib.android.util.dialog.DialogDismissReason;
import com.soybeany.bdlib.android.util.dialog.IRealDialog;
import com.soybeany.bdlib.android.web.msg.DMsg;
import com.soybeany.bdlib.android.web.notifier.DNotifier;
import com.soybeany.bdlib.android.web.notifier.DVNotifier;
import com.soybeany.connector.ITarget;
import com.soybeany.connector.MsgManager;
import com.soybeany.connector.MsgSender;

import java.util.List;

import static com.soybeany.bdlib.android.util.BDContext.MAIN_HANDLER;

/**
 * 弹窗管理器，负责单弹窗多消息的处理
 * <br>Created by Soybeany on 2020/4/2.
 */
@SuppressWarnings("WeakerAccess")
public class DialogManager implements ITarget<DMsg.Invoker>, IObserver {

    public final DVNotifier dvNotifier;

    private final MsgManager<DMsg.Invoker, DMsg.Callback> mIManager = new MsgManager<>();
    private final DMsg.OnChangeProgress mProgress = new DMsg.OnChangeProgress();

    private final DialogInfoManager mManager;
    private final IRealDialog mRealDialog;

    private final DNotifier mDNotifier;

    public DialogManager(@NonNull DialogInfoManager manager, @NonNull IRealDialog realDialog) {
        // 赋值
        mManager = manager;
        dvNotifier = mManager.dvNotifier;
        mDNotifier = mManager.dNotifier;
        mRealDialog = realDialog;
        // 开始连接
        MsgSender.connect(mDNotifier, dvNotifier);
        // 绑定
        mIManager.bind(this, mDNotifier, false);
        // 设置Notifier
        if (realDialog instanceof INotifierRealDialog) {
            ((INotifierRealDialog) realDialog).onSetupDVNotifier(dvNotifier);
        }
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        // 按需发送回调
        if (mManager.hasDialogShowing) {
            MAIN_HANDLER.post(() -> mDNotifier.sendCMsg(new DMsg.OnDismissDialog(DialogDismissReason.DESTROY)));
            mManager.hasDialogShowing = false;
        }
        // 解绑
        mIManager.unbind(false);
        // 断开连接
        MsgSender.disconnect(mDNotifier, dvNotifier);
    }

    @Override
    public void onSetupMsgProcessors(List<MsgProcessor<? extends DMsg.Invoker>> list) {
        list.add(new MsgProcessor<>(DMsg.ShowDialog.class, msg -> {
            mRealDialog.onShowDialog();
            mDNotifier.sendCMsg(new DMsg.OnShowDialog());
        }));
        list.add(new MsgProcessor<>(DMsg.DismissDialog.class, msg -> {
            mRealDialog.onDismissDialog(msg.data);
            mDNotifier.sendCMsg(new DMsg.OnDismissDialog(msg.data));
        }));
        list.add(new MsgProcessor<>(DMsg.DisplayMsg.class, msg -> {
            mRealDialog.onDisplayHint(msg.data);
            mDNotifier.sendCMsg(new DMsg.OnDisplayMsg(msg.data));
        }));
        list.add(new MsgProcessor<>(DMsg.ChangeCancelable.class, msg -> {
            mRealDialog.onChangeCancelable(msg.data);
            mDNotifier.sendCMsg(new DMsg.OnChangeCancelable(msg.data));
        }));
        list.add(new MsgProcessor<>(DMsg.ChangeProgress.class, msg -> {
            mRealDialog.onToProgress(msg.data);
            mDNotifier.sendCMsg(mProgress.percent(msg.data));
        }));
    }

}
