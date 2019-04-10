package com.soybeany.bdlib.android.util.dialog;

import com.soybeany.bdlib.android.util.HandlerThreadImpl;
import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.util.thread.MessageCenter;

/**
 * <br>Created by Soybeany on 2019/3/21.
 */
public abstract class AbstractDialog {
    protected DialogViewModel mVM;

    private MessageCenter.ICallback mCallback = data -> cleanup(null);

    public AbstractDialog(DialogViewModel vm) {
        mVM = vm;
    }

    public void show(DialogMsg msg) {
        if (mVM.addMsg(msg)) {
            showDialog(msg);
        }
    }

    public void cancelAll() {
        onRealCancel();
    }

    public void cancel(DialogMsg msg) {
        popMsg(msg, this::cancelAll);
    }

    public void dismissAll() {
        onRealDismiss();
        cleanup(DialogViewModel.Reason.NORM);
    }

    public void dismiss(DialogMsg msg) {
        popMsg(msg, this::dismissAll);
    }

    public String getMsgCenterKey() {
        return mVM.key;
    }

    public DialogMsg curMsg() {
        return mVM.getNewestMsg();
    }

    protected void showDialog(DialogMsg msg) {
        if (!mVM.isShowing) {
            MessageCenter.register(HandlerThreadImpl.UI_THREAD, getMsgCenterKey(), mCallback);
            onRealShow();
            mVM.isShowing = true;
        }
        onSetupDialog(mVM.getHint(msg), mVM.cancelable());
    }

    protected void cleanup(DialogViewModel.Reason reason) {
        if (!mVM.isShowing) {
            return;
        }
        MessageCenter.unregister(mCallback);
        Optional.ofNullable(reason).ifPresent(r -> MessageCenter.notify(getMsgCenterKey(), r, 0));
        mVM.clearMsgSet();
        mVM.isShowing = false;
    }

    private void popMsg(DialogMsg msg, IDismissListener listener) {
        // 若移除失败则不作处理
        if (!mVM.removeMsg(msg)) {
            return;
        }
        // 更改或关闭弹窗
        DialogMsg newestMsg = mVM.getNewestMsg();
        if (null != newestMsg) {
            showDialog(newestMsg);
        } else {
            listener.onRealDismiss();
        }
    }

    protected abstract void onSetupDialog(String hint, boolean cancelable);

    protected abstract void onRealShow();

    protected abstract void onRealCancel();

    protected abstract void onRealDismiss();

    private interface IDismissListener {
        void onRealDismiss();
    }
}
