package com.soybeany.bdlib.android.util.dialog;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.util.HandlerThreadImpl;
import com.soybeany.bdlib.android.util.IObserver;
import com.soybeany.bdlib.android.util.dialog.msg.DialogCallbackMsg;
import com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg;
import com.soybeany.bdlib.android.util.dialog.msg.IDialogMsg;
import com.soybeany.bdlib.core.util.notify.INotifyMsg;
import com.soybeany.bdlib.core.util.notify.IOnCallDealer;
import com.soybeany.bdlib.core.util.notify.Notifier;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static com.soybeany.bdlib.android.util.dialog.DialogDismissReason.CANCEL;
import static com.soybeany.bdlib.android.util.dialog.DialogDismissReason.NORM;
import static com.soybeany.bdlib.android.util.dialog.DialogDismissReason.OTHER;
import static com.soybeany.bdlib.android.util.dialog.msg.DialogCallbackMsg.TYPE_ON_DISMISS_DIALOG;
import static com.soybeany.bdlib.android.util.dialog.msg.DialogCallbackMsg.TYPE_ON_POP;
import static com.soybeany.bdlib.android.util.dialog.msg.DialogCallbackMsg.TYPE_ON_SHOW;
import static com.soybeany.bdlib.android.util.dialog.msg.DialogCallbackMsg.TYPE_ON_SHOW_DIALOG;
import static com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg.TYPE_DISMISS_DIALOG;
import static com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg.TYPE_POP_MSG;
import static com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg.TYPE_SHOW_MSG;
import static com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg.TYPE_TO_PROGRESS;

/**
 * 通知弹窗代理，使用者需在弹窗关闭监听中调用{@link #onCancel()}
 * <br>Created by Soybeany on 2019/5/10.
 */
public class NotifyDialogDelegate implements IOnCallDealer, IObserver {
    private final SortedSet<IDialogMsg> mMsgSet = new TreeSet<>(); // 收录的弹窗信息
    private final Set<IDialogMsg> mUnableCancelSet = new HashSet<>(); // 收录的弹窗信息(不可取消)
    private final Notifier<DialogInvokerMsg, DialogCallbackMsg> mNotifier = new Notifier<>(HandlerThreadImpl.UI_THREAD);

    private final Lifecycle mLifecycle;
    private final DialogInvokerMsg mInvokerMsg = new DialogInvokerMsg();
    private final DialogCallbackMsg mCallbackMsg = new DialogCallbackMsg();
    private final IRealDialog mRealDialog;

    public NotifyDialogDelegate(@NonNull LifecycleOwner owner, @NonNull IRealDialog realDialog) {
        mLifecycle = owner.getLifecycle();
        mLifecycle.addObserver(this);
        mRealDialog = realDialog;
    }

    // //////////////////////////////////重写区//////////////////////////////////

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        mNotifier.register();
        mNotifier.invoker().addDealer(this);
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        dismiss(OTHER);
        mNotifier.invoker().removeDealer(this);
        mNotifier.unregister();
        mLifecycle.removeObserver(this);
    }

    @Override
    public void onCall(INotifyMsg msg) {
        switch (msg.getType()) {
            case TYPE_SHOW_MSG:
                showMsg((IDialogMsg) msg.getData());
                break;
            case TYPE_POP_MSG:
                popMsg((IDialogMsg) msg.getData());
                break;
            case TYPE_TO_PROGRESS:
                toProgress((Float) msg.getData());
                break;
            case TYPE_DISMISS_DIALOG:
                dismiss((DialogDismissReason) msg.getData());
                break;
        }
    }

    // //////////////////////////////////调用区//////////////////////////////////

    public Notifier<DialogInvokerMsg, DialogCallbackMsg> getNotifier() {
        return mNotifier;
    }

    public void showMsg(@Nullable IDialogMsg msg) {
        if (null == msg) {
            return;
        }
        // 不可取消列表按需设置
        if (msg.cancelable()) {
            mUnableCancelSet.remove(msg);
        } else {
            mUnableCancelSet.add(msg);
        }
        // 设置最后弹窗时间
        msg.showTime(System.currentTimeMillis());
        // 添加至信息集(可能覆盖)
        mMsgSet.add(msg);
        // 显示
        showNewestMsg(false);
    }

    public void popMsg(@Nullable IDialogMsg msg) {
        if (!mMsgSet.remove(msg)) {
            return;
        }
        mUnableCancelSet.remove(msg);
        notifyCallback(TYPE_ON_POP, msg);
        // 更改或关闭弹窗
        if (!mMsgSet.isEmpty()) {
            showNewestMsg(true);
        } else {
            dismiss(NORM);
        }
    }

    public void toProgress(float percent) {
        mRealDialog.realToProgress(percent);
    }

    public void cancel() {
        if (dialogCancelable()) {
            mRealDialog.realCancel();
        }
    }

    public void dismiss(DialogDismissReason reason) {
        for (IDialogMsg msg : mMsgSet) {
            notifyCallback(TYPE_ON_POP, msg);
        }
        mMsgSet.clear();
        mUnableCancelSet.clear();
        if (DialogDismissReason.CANCEL.equals(reason) || mRealDialog.isDialogShowing()) {
            mRealDialog.realDismiss();
            notifyCallback(TYPE_ON_DISMISS_DIALOG, reason);
        }
    }

    public void onCancel() {
        mNotifier.invoker().notifyNow(mInvokerMsg.type(TYPE_DISMISS_DIALOG).data(CANCEL));
    }

    // //////////////////////////////////内部区//////////////////////////////////

    private void showNewestMsg(boolean isReShow) {
        if (!mRealDialog.isDialogShowing()) {
            notifyCallback(TYPE_ON_SHOW_DIALOG, null);
            mRealDialog.realShow();
        }
        IDialogMsg msg = mMsgSet.last();
        if (!isReShow) {
            notifyCallback(TYPE_ON_SHOW, msg);
        }
        mRealDialog.signalSetupMsg(msg.hint(), dialogCancelable());
    }

    private boolean dialogCancelable() {
        return mUnableCancelSet.isEmpty();
    }

    private void notifyCallback(@NonNull String type, @Nullable Object data) {
        mNotifier.callback().notifyNow(mCallbackMsg.type(type).data(data));
    }

    // //////////////////////////////////接口区//////////////////////////////////

    public interface IRealDialog {
        void signalSetupMsg(String hint, boolean cancelable);

        void realShow();

        void realToProgress(float percent);

        void realCancel();

        void realDismiss();

        boolean isDialogShowing();
    }
}
