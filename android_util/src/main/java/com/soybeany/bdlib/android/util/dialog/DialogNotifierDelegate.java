package com.soybeany.bdlib.android.util.dialog;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.util.dialog.msg.DialogNotifierMsg;
import com.soybeany.bdlib.android.util.dialog.msg.IDialogMsg;
import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.util.notify.INotifyMsg;
import com.soybeany.bdlib.core.util.notify.IOnCallListener;

import java.util.HashMap;
import java.util.Map;

import static com.soybeany.bdlib.android.util.dialog.DialogDismissReason.NORM;

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

    private Map<Class<? extends INotifyMsg>, IOnCallListener> mListeners = new HashMap<Class<? extends INotifyMsg>, IOnCallListener>() {{
        put(DialogNotifierMsg.ShowMsg.class, msg -> showMsg((IDialogMsg) msg.getData()));
        put(DialogNotifierMsg.PopMsg.class, msg -> popMsg((IDialogMsg) msg.getData()));
        put(DialogNotifierMsg.ToProgress.class, msg -> toProgress((Float) msg.getData()));
        put(DialogNotifierMsg.DismissDialog.class, msg -> dismiss((DialogDismissReason) msg.getData()));
    }};

    // //////////////////////////////////重写区//////////////////////////////////

    @Override
    public void onCall(INotifyMsg msg) {
        Optional.ofNullable(mListeners.get(msg.getClass())).ifPresent(listener -> listener.onCall(msg));
    }

    @Nullable
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
        notifyCallback(new DialogNotifierMsg.OnPop(msg));
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
            notifyCallback(new DialogNotifierMsg.OnPop(msg));
        }
        mNotifier.msgSet.clear();
        mNotifier.unableCancelSet.clear();
        if (mNotifier.isDialogShowing) {
            mRealDialog.realDismiss();
            mNotifier.isDialogShowing = false;
            notifyCallback(new DialogNotifierMsg.OnDismissDialog(reason));
        }
    }

    public void invokeDismissDialog(DialogDismissReason reason) {
        mNotifier.invoker().notifyNow(new DialogNotifierMsg.DismissDialog(reason));
    }

    // //////////////////////////////////内部区//////////////////////////////////

    private void showNewestMsg(boolean isReShow) {
        if (!mNotifier.isDialogShowing) {
            mRealDialog.realShow();
            mNotifier.isDialogShowing = true;
            notifyCallback(new DialogNotifierMsg.OnShowDialog());
        }
        IDialogMsg msg = mNotifier.msgSet.last();
        if (!isReShow) {
            notifyCallback(new DialogNotifierMsg.OnShow(msg));
        }
        mNotifier.hint.setValue(msg.hint());
        mNotifier.cancelable.setValue(dialogCancelable());
    }

    private boolean dialogCancelable() {
        return mNotifier.unableCancelSet.isEmpty();
    }

    private void notifyCallback(@NonNull DialogNotifierMsg.Callback msg) {
        mNotifier.callback().notifyNow(msg);
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
