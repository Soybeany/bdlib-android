package com.soybeany.bdlib.android.util.dialog;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.soybeany.bdlib.android.util.BDContext;
import com.soybeany.bdlib.android.util.IObserver;
import com.soybeany.bdlib.android.util.dialog.msg.DialogCallbackMsg;
import com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg;
import com.soybeany.bdlib.android.util.dialog.msg.IDialogMsg;
import com.soybeany.bdlib.core.util.notify.INotifyMsg;
import com.soybeany.bdlib.core.util.notify.IOnCallListener;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
 * 弹窗通知者代理，使用者需注意:
 * 1.使用{@link #getNew(FragmentActivity, String)}获得实例，而不要直接new
 * 2.弹窗关闭监听中调用{@link #invokeDismissDialog(DialogDismissReason)}
 * <br>Created by Soybeany on 2019/5/10.
 */
public class DialogNotifierDelegate extends ViewModel implements IOnCallListener, IObserver, DialogNotifier.IProvider {
    /**
     * 标识此delegate的uid
     */
    public final String uid = BDContext.getUID();

    private final SortedSet<IDialogMsg> mMsgSet = new TreeSet<>(); // 收录的弹窗信息
    private final Set<IDialogMsg> mUnableCancelSet = new HashSet<>(); // 收录的弹窗信息(不可取消)
    private final DialogNotifier mNotifier = DialogNotifier.getNew();

    private final DialogInvokerMsg mInvokerMsg = new DialogInvokerMsg();
    private final DialogCallbackMsg mCallbackMsg = new DialogCallbackMsg();

    private final MutableLiveData<String> mHint = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mCancelable = new MutableLiveData<>();

    @Nullable
    private IRealDialog mRealDialog;
    private boolean mIsDialogShowing;

    public static Unbind getNew(FragmentActivity activity, String type) {
        DialogNotifierDelegate delegate = ViewModelProviders.of(activity).get(type, DialogNotifierDelegate.class);
        activity.getLifecycle().addObserver(delegate); // 引入生命周期监听
        return new Unbind(delegate);
    }

    {
        mNotifier.invoker().addListener(this);
    }

    // //////////////////////////////////重写区//////////////////////////////////

    @Override
    protected void onCleared() {
        super.onCleared();
        if (null != mRealDialog && mRealDialog.shouldCancelOnClear()) {
            dismiss(OTHER);
            mRealDialog = null;
        }
        mNotifier.invoker().removeListener(this);
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

    @Nullable
    @Override
    public DialogNotifier getDialogNotifier(String type) {
        return mNotifier;
    }

    // //////////////////////////////////调用区//////////////////////////////////

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
        if (null != mRealDialog) {
            mRealDialog.realToProgress(percent);
        }
    }

    public void cancel() {
        if (null != mRealDialog && dialogCancelable()) {
            mRealDialog.realCancel();
        }
    }

    public void dismiss(DialogDismissReason reason) {
        if (null == mRealDialog) {
            return;
        }
        for (IDialogMsg msg : mMsgSet) {
            notifyCallback(TYPE_ON_POP, msg);
        }
        mMsgSet.clear();
        mUnableCancelSet.clear();
        if (mIsDialogShowing) {
            mRealDialog.realDismiss();
            mIsDialogShowing = false;
            notifyCallback(TYPE_ON_DISMISS_DIALOG, reason);
        }
    }

    public void invokeDismissDialog(DialogDismissReason reason) {
        mNotifier.invoker().notifyNow(mInvokerMsg.type(TYPE_DISMISS_DIALOG).data(reason));
    }

    // //////////////////////////////////内部区//////////////////////////////////

    private DialogNotifierDelegate bindRealDialog(@NonNull IRealDialog realDialog) {
        if (mRealDialog != realDialog) {
            mRealDialog = realDialog;
            mRealDialog.onInit(mIsDialogShowing);
            mRealDialog.onObserveMsg(mHint, mCancelable);
        }
        return this;
    }

    private void showNewestMsg(boolean isReShow) {
        if (null == mRealDialog) {
            return;
        }
        if (!mIsDialogShowing) {
            mRealDialog.realShow();
            mIsDialogShowing = true;
            notifyCallback(TYPE_ON_SHOW_DIALOG, null);
        }
        IDialogMsg msg = mMsgSet.last();
        if (!isReShow) {
            notifyCallback(TYPE_ON_SHOW, msg);
        }
        mHint.setValue(msg.hint());
        mCancelable.setValue(dialogCancelable());
    }

    private boolean dialogCancelable() {
        return mUnableCancelSet.isEmpty();
    }

    private void notifyCallback(@NonNull String type, @Nullable Object data) {
        mNotifier.callback().notifyNow(mCallbackMsg.type(type).data(data));
    }

    // //////////////////////////////////内部类区//////////////////////////////////

    public static class Unbind {
        private DialogNotifierDelegate mDelegate;

        Unbind(DialogNotifierDelegate delegate) {
            mDelegate = delegate;
        }

        public DialogNotifierDelegate bind(@NonNull IRealDialog realDialog) {
            return mDelegate.bindRealDialog(realDialog);
        }

        public String uid() {
            return mDelegate.uid;
        }
    }

    public interface IRealDialog extends DialogNotifier.IProvider {
        default void onInit(boolean isDialogShowing) {
        }

        void onObserveMsg(@NonNull LiveData<String> hint, @NonNull LiveData<Boolean> cancelable);

        void realShow();

        void realToProgress(float percent);

        void realCancel();

        void realDismiss();

        default boolean shouldCancelOnClear() {
            return true;
        }
    }
}
