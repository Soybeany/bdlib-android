package com.soybeany.bdlib.android.web.dialog;

import com.soybeany.bdlib.android.util.IObserver;
import com.soybeany.bdlib.android.util.dialog.DialogDismissReason;
import com.soybeany.bdlib.android.util.dialog.IRealDialog;
import com.soybeany.bdlib.android.web.msg.DMsg;
import com.soybeany.bdlib.android.web.notifier.DSender;
import com.soybeany.bdlib.android.web.notifier.DVSender;
import com.soybeany.connector.ITarget;
import com.soybeany.connector.MsgManager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

/**
 * 弹窗管理器，负责单弹窗多消息的处理
 * <br>Created by Soybeany on 2020/4/2.
 */
@SuppressWarnings("WeakerAccess")
public class DialogManager implements ITarget<DMsg.Invoker<?>>, IObserver {

    public final DVSender dvNotifier;

    private final MsgManager<DMsg.Invoker<?>, DMsg.Callback<?>> mIManager = new MsgManager<>();
    private final DMsg.OnChangeProgress mProgress = new DMsg.OnChangeProgress();

    private final DialogInfoManager mManager;
    private final IRealDialog mRealDialog;

    private final DSender mDSender;

    public DialogManager(@NonNull DialogInfoManager manager, @NonNull IRealDialog realDialog) {
        // 赋值
        mManager = manager;
        dvNotifier = mManager.dvSender;
        mDSender = mManager.dSender;
        mRealDialog = realDialog;
        // 开始连接
        dvNotifier.connect(mDSender, false);
        // 绑定
        mIManager.bind(this, mDSender, false);
        // 设置Notifier
        if (realDialog instanceof INotifierRealDialog) {
            ((INotifierRealDialog) realDialog).onSetupDVNotifier(dvNotifier);
        }
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        // 按需发送回调
        if (mManager.hasDialogShowing) {
            mDSender.sendCMsg(new DMsg.OnDismissDialog(DialogDismissReason.DESTROY));
            mManager.hasDialogShowing = false;
        }
        // 解绑
        mIManager.unbind(true);
        // 断开连接
        dvNotifier.disconnect(mDSender, true);
    }

    @Override
    public void onSetupMsgProcessors(List<MsgProcessor<? extends DMsg.Invoker<?>>> list) {
        list.add(new MsgProcessor<>(DMsg.ShowDialog.class, msg -> {
            mRealDialog.onShowDialog();
            mDSender.sendCMsg(new DMsg.OnShowDialog());
        }));
        list.add(new MsgProcessor<>(DMsg.DismissDialog.class, msg -> {
            mRealDialog.onDismissDialog(msg.data);
            mDSender.sendCMsg(new DMsg.OnDismissDialog(msg.data));
        }));
        list.add(new MsgProcessor<>(DMsg.DisplayMsg.class, msg -> {
            mRealDialog.onDisplayHint(msg.data);
            mDSender.sendCMsg(new DMsg.OnDisplayMsg(msg.data));
        }));
        list.add(new MsgProcessor<>(DMsg.ChangeCancelable.class, msg -> {
            mRealDialog.onChangeCancelable(msg.data);
            mDSender.sendCMsg(new DMsg.OnChangeCancelable(msg.data));
        }));
        list.add(new MsgProcessor<>(DMsg.ChangeProgress.class, msg -> {
            mRealDialog.onToProgress(msg.data);
            mDSender.sendCMsg(mProgress.percent(msg.data));
        }));
    }

}
