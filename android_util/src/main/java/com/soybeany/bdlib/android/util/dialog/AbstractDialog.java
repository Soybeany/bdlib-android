package com.soybeany.bdlib.android.util.dialog;

import com.soybeany.bdlib.android.util.HandlerThreadImpl;
import com.soybeany.bdlib.core.util.storage.MessageCenter;

/**
 * 抽象弹窗，用一个弹窗管理多条信息
 * <br>Created by Soybeany on 2019/3/21.
 */
public abstract class AbstractDialog {
    protected DialogViewModel mVM;

    private MessageCenter.ICallback mCallback = data -> onDismissDialog();

    public AbstractDialog(DialogViewModel vm) {
        mVM = vm;
    }

    public void showMsg(DialogMsg msg) {
        if (mVM.addMsg(msg)) {
            showNewestMsg(false);
        }
    }

    public void cancelMsg(DialogMsg msg) {
        popMsg(msg, this::cancelDialog);
    }

    public void popMsg(DialogMsg msg) {
        popMsg(msg, this::dismissDialog);
    }

    public void cancelDialog() {
        onRealCancel();
    }

    public void dismissDialog() {
        MessageCenter.notify(mVM.getOnDismissDialogKey(), DialogViewModel.Reason.NORM, 0);
    }

    /**
     * 获得用于在{@link MessageCenter}中监听的Key
     *
     * @return
     */
    public IMsgCenterKeyProvider getKeyProvider() {
        return mVM;
    }

    public DialogMsg curMsg() {
        return mVM.getNewestMsg();
    }

    /**
     * 显示最新的信息
     */
    protected void showNewestMsg(boolean isReShow) {
        if (!mVM.isShowing) {
            MessageCenter.register(HandlerThreadImpl.UI_THREAD, mVM.getOnDismissDialogKey(), mCallback);
            MessageCenter.notify(mVM.getOnShowDialogKey(), null, 0);
            onRealShow();
            mVM.isShowing = true;
        }
        DialogMsg msg = mVM.getNewestMsg();
        MessageCenter.notify(isReShow ? mVM.getOnReShowMsgKey() : mVM.getOnShowMsgKey(), msg, 0);
        onSetupDialog(mVM.getHint(msg), mVM.cancelable());
    }

    /**
     * 关闭弹窗的回调
     */
    private void onDismissDialog() {
        if (!mVM.isShowing) {
            return;
        }
        onRealDismiss();
        MessageCenter.unregister(mCallback);
        mVM.clearMsgSet();
        mVM.isShowing = false;
    }

    private void popMsg(DialogMsg msg, IDismissListener listener) {
        // 若移除失败则不作处理
        if (!mVM.removeMsg(msg)) {
            return;
        }
        MessageCenter.notify(mVM.getOnPopMsgKey(), msg, 0);
        // 更改或关闭弹窗
        if (null != mVM.getNewestMsg()) {
            showNewestMsg(true);
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
