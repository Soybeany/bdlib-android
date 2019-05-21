package com.soybeany.bdlib.android.util.dialog;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.util.dialog.msg.IDialogMsg;
import com.soybeany.bdlib.core.util.notify.INotifyMsg;

import static com.soybeany.bdlib.android.util.dialog.DialogDismissReason.NORM;
import static com.soybeany.bdlib.android.util.dialog.msg.DialogCallbackMsg.TYPE_ON_DISMISS_DIALOG;
import static com.soybeany.bdlib.android.util.dialog.msg.DialogCallbackMsg.TYPE_ON_POP;
import static com.soybeany.bdlib.android.util.dialog.msg.DialogCallbackMsg.TYPE_ON_SHOW;
import static com.soybeany.bdlib.android.util.dialog.msg.DialogCallbackMsg.TYPE_ON_SHOW_DIALOG;
import static com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg.TYPE_DISMISS_DIALOG;
import static com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg.TYPE_POP_MSG;
import static com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg.TYPE_SHOW_MSG;
import static com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg.TYPE_TO_PROGRESS;

/**
 * 弹窗通知者代理，使用者需注意:
 * 2.弹窗关闭监听中调用{@link #invokeDismissDialog(DialogDismissReason)}
 * <br>Created by Soybeany on 2019/5/10.
 */
public class DialogNotifierDelegate implements DialogNotifier.IDialog, DialogNotifier.IProvider {
    private final IRealDialog mRealDialog;
    private DialogNotifier mNotifier;

    public DialogNotifierDelegate(@NonNull IRealDialog dialog) {
        mRealDialog = dialog;
    }

    // //////////////////////////////////重写区//////////////////////////////////

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

    @NonNull
    @Override
    public DialogNotifier getDialogNotifier() {
        return mNotifier;
    }

    @Override
    public void onBindNotifier(String type, DialogNotifier notifier) {
        mNotifier = notifier;
        mRealDialog.onObserveMsg(mNotifier.hint, mNotifier.cancelable);
    }

    // //////////////////////////////////调用区//////////////////////////////////

    public void showMsg(@Nullable IDialogMsg msg) {
        if (null == msg) {
            return;
        }
        // 不可取消列表按需设置
        if (msg.cancelable()) {
            mNotifier.unableCancelSet.remove(msg);
        } else {
            mNotifier.unableCancelSet.add(msg);
        }
        // 设置最后弹窗时间
        msg.showTime(System.currentTimeMillis());
        // 添加至信息集(可能覆盖)
        mNotifier.msgSet.add(msg);
        // 显示
        showNewestMsg(false);
    }

    public void popMsg(@Nullable IDialogMsg msg) {
        if (!mNotifier.msgSet.remove(msg)) {
            return;
        }
        mNotifier.unableCancelSet.remove(msg);
        notifyCallback(TYPE_ON_POP, msg);
        // 更改或关闭弹窗
        if (!mNotifier.msgSet.isEmpty()) {
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
        for (IDialogMsg msg : mNotifier.msgSet) {
            notifyCallback(TYPE_ON_POP, msg);
        }
        mNotifier.msgSet.clear();
        mNotifier.unableCancelSet.clear();
        if (mNotifier.isDialogShowing) {
            mRealDialog.realDismiss();
            mNotifier.isDialogShowing = false;
            notifyCallback(TYPE_ON_DISMISS_DIALOG, reason);
        }
    }

    public void invokeDismissDialog(DialogDismissReason reason) {
        mNotifier.invoker().notifyNow(mNotifier.invokerMsg.type(TYPE_DISMISS_DIALOG).data(reason));
    }

    // //////////////////////////////////内部区//////////////////////////////////

    private void showNewestMsg(boolean isReShow) {
        if (!mNotifier.isDialogShowing) {
            mRealDialog.realShow();
            mNotifier.isDialogShowing = true;
            notifyCallback(TYPE_ON_SHOW_DIALOG, null);
        }
        IDialogMsg msg = mNotifier.msgSet.last();
        if (!isReShow) {
            notifyCallback(TYPE_ON_SHOW, msg);
        }
        mNotifier.hint.setValue(msg.hint());
        mNotifier.cancelable.setValue(dialogCancelable());
    }

    private boolean dialogCancelable() {
        return mNotifier.unableCancelSet.isEmpty();
    }

    private void notifyCallback(@NonNull String type, @Nullable Object data) {
        mNotifier.callback().notifyNow(mNotifier.callbackMsg.type(type).data(data));
    }

    // //////////////////////////////////内部类区//////////////////////////////////

    public interface IRealDialog {
        void onObserveMsg(@NonNull LiveData<String> hint, @NonNull LiveData<Boolean> cancelable);

        void realShow();

        void realToProgress(float percent);

        void realCancel();

        void realDismiss();
    }
}
