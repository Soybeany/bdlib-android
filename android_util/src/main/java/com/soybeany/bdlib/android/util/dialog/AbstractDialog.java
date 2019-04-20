package com.soybeany.bdlib.android.util.dialog;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.soybeany.bdlib.android.util.HandlerThreadImpl;
import com.soybeany.bdlib.android.util.IObserver;
import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.util.storage.MessageCenter;

/**
 * 抽象弹窗，用一个弹窗管理多条信息
 * <br>Created by Soybeany on 2019/3/21.
 */
public abstract class AbstractDialog implements IObserver {
    protected DialogViewModel mVM;

    private MessageCenter.ICallback mShowMsgCallback = data -> showMsg((DialogMsg) data);
    private MessageCenter.ICallback mPopMsgCallback = data -> popMsg((DialogMsg) data);
    private MessageCenter.ICallback mCancelMsgCallback = data -> cancelMsg((DialogMsg) data);
    private MessageCenter.ICallback mDismissDialogCallback = data -> mVM.notifyDialogToDismiss((DialogViewModel.Reason) data);
    private MessageCenter.ICallback mOnDismissCallback = data -> onDismissDialog();

    /**
     * 使用弹窗进行包裹
     */
    public static void wrap(@Nullable DialogKeyProvider provider, @Nullable DialogMsg msg, @WorkerThread Runnable runnable) {
        Optional.ofNullable(provider).ifPresent(p -> MessageCenter.notifyNow(p.showMsgKey, msg));
        runnable.run();
        Optional.ofNullable(provider).ifPresent(p -> MessageCenter.notifyNow(p.popMsgKey, msg));
    }

    public AbstractDialog(DialogViewModel vm) {
        mVM = vm;
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        register(getKeyProvider().showMsgKey, mShowMsgCallback);
        register(getKeyProvider().popMsgKey, mPopMsgCallback);
        register(getKeyProvider().cancelMsgKey, mCancelMsgCallback);
        register(getKeyProvider().dismissDialogKey, mDismissDialogCallback);
        register(getKeyProvider().onDismissDialogKey, mOnDismissCallback);
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        MessageCenter.unregister(mShowMsgCallback);
        MessageCenter.unregister(mPopMsgCallback);
        MessageCenter.unregister(mCancelMsgCallback);
        MessageCenter.unregister(mDismissDialogCallback);
        MessageCenter.unregister(mOnDismissCallback);
    }

    public void showMsg(DialogMsg msg) {
        if (mVM.addMsg(msg)) {
            showNewestMsg(false);
        }
    }

    public void cancelMsg(DialogMsg msg) {
        innerPopMsg(msg, this::cancelDialog);
    }

    public void popMsg(DialogMsg msg) {
        innerPopMsg(msg, this::dismissDialog);
    }

    public void cancelDialog() {
        onRealCancel();
    }

    public void dismissDialog() {
        mVM.notifyDialogToDismiss(DialogViewModel.Reason.NORM);
    }

    /**
     * 获得用于在{@link MessageCenter}中监听的Key
     *
     * @return
     */
    public DialogKeyProvider getKeyProvider() {
        return mVM.keyProvider;
    }

    public DialogMsg curMsg() {
        return mVM.getNewestMsg();
    }

    /**
     * 显示最新的信息
     */
    protected void showNewestMsg(boolean isReShow) {
        if (!mVM.isShowing) {
            MessageCenter.notifyNow(getKeyProvider().onShowDialogKey, null);
            onRealShow();
            mVM.isShowing = true;
        }
        DialogMsg msg = mVM.getNewestMsg();
        MessageCenter.notifyNow(isReShow ? getKeyProvider().onReShowMsgKey : getKeyProvider().onShowMsgKey, msg);
        onSetupDialog(mVM.getHint(msg), mVM.cancelable());
    }

    private void register(String key, MessageCenter.ICallback callback) {
        MessageCenter.register(HandlerThreadImpl.UI_THREAD, key, callback);
    }

    /**
     * 关闭弹窗的回调
     */
    private void onDismissDialog() {
        if (!mVM.isShowing) {
            return;
        }
        onRealDismiss();
        mVM.clearMsgSet();
        mVM.isShowing = false;
    }

    private void innerPopMsg(DialogMsg msg, IDismissListener listener) {
        // 若移除失败则不作处理
        if (!mVM.removeMsg(msg)) {
            return;
        }
        MessageCenter.notifyNow(getKeyProvider().onPopMsgKey, msg);
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
