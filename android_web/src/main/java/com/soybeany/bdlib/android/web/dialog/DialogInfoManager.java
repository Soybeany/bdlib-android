package com.soybeany.bdlib.android.web.dialog;

import com.soybeany.bdlib.android.util.dialog.DialogDismissReason;
import com.soybeany.bdlib.android.util.dialog.msg.IDialogHint;
import com.soybeany.bdlib.android.web.msg.DialogMsg;
import com.soybeany.bdlib.android.web.notifier.DialogNotifier;
import com.soybeany.connector.ITarget;
import com.soybeany.connector.MsgManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * <br>Created by Soybeany on 2020/4/7.
 */
public class DialogInfoManager implements ITarget<DialogMsg.Invoker> {

    /**
     * 标识是否有弹窗正在显示
     */
    public boolean hasDialogShowing;

    /**
     * 当前的进度
     */
    public float toPercent;

    public final DialogNotifier notifier = new DialogNotifier();

    /**
     * 入栈的弹窗信息
     */
    private final SortedSet<IDialogHint> mHintSet = new TreeSet<>();

    /**
     * 入栈的弹窗信息(不可取消)
     */
    private final Set<IDialogHint> mUnableCancelSet = new HashSet<>();

    private final MsgManager<DialogMsg.Invoker, DialogMsg.Callback> mManager = new MsgManager<>();

    // //////////////////////////////////方法重写//////////////////////////////////

    @Override
    public void onSetupMsgProcessors(List<MsgProcessor<? extends DialogMsg.Invoker>> list) {
        list.add(new MsgProcessor<>(DialogMsg.PushMsg.class, msg -> pushMsg(msg.senderUid, msg.data)));
        list.add(new MsgProcessor<>(DialogMsg.PopMsg.class, msg -> popMsg(msg.senderUid, msg.data)));
        list.add(new MsgProcessor<>(DialogMsg.ToProgress.class, msg -> toPercent = msg.data));
    }

    // //////////////////////////////////公开方法区//////////////////////////////////

    public void bind() {
        mManager.bind(this, notifier, false);
    }

    public void unbind() {
        mManager.unbind(false);
    }

    public void pushMsg(String uid, IDialogHint msg) {
        if (null == msg) {
            return;
        }
        DialogInfoVM.exeInMainThread(() -> {
            // 不可取消列表按需设置
            if (msg.cancelable()) {
                // 有可能是已有消息的修改，故尝试移除
                mUnableCancelSet.remove(msg);
            } else {
                mUnableCancelSet.add(msg);
            }
            // 添加至信息集(可能覆盖)
            mHintSet.add(msg);
            // 显示
            showNewestMsg(uid, false);
        });
    }

    public void popMsg(String uid, IDialogHint msg) {
        DialogInfoVM.exeInMainThread(() -> {
            if (!mHintSet.remove(msg)) {
                return;
            }
            mUnableCancelSet.remove(msg);
            notifier.sendCMsg(uid, new DialogMsg.OnPopMsg(msg));
            // 更改或关闭弹窗
            if (!mHintSet.isEmpty()) {
                showNewestMsg(uid, true);
            } else {
                clearMsg(uid, DialogDismissReason.NORM);
            }
        });
    }

    public void clearMsg(String uid, DialogDismissReason reason) {
        for (IDialogHint msg : mHintSet) {
            notifier.sendCMsg(uid, new DialogMsg.OnPopMsg(msg));
        }
        mHintSet.clear();
        mUnableCancelSet.clear();
        // 若弹窗没有关闭，则关闭
//        if (hasDialogShowing) {
        notifier.sendCMsg(uid, new DialogMsg.OnNeedDismissDialog(reason));
        hasDialogShowing = false;
//        }
    }

    /**
     * 获得当前正在显示的信息
     */
    public IDialogHint getCurDialogHint() {
        return hasHint() ? mHintSet.last() : null;
    }

    /**
     * 栈中是否还有信息
     */
    public boolean hasHint() {
        return !mHintSet.isEmpty();
    }

    /**
     * 弹窗是否应该能取消
     */
    public boolean shouldDialogCancelable() {
        return mUnableCancelSet.isEmpty();
    }

    /**
     * 是否需要显示弹窗
     */
    public boolean needShowDialog() {
        return hasHint() && !hasDialogShowing;
    }

    // //////////////////////////////////内部方法//////////////////////////////////

    private void showNewestMsg(String uid, boolean isReshow) {
        // 若弹窗没有显示，则显示
        if (!hasDialogShowing) {
            notifier.sendCMsg(uid, new DialogMsg.OnNeedShowDialog());
            hasDialogShowing = true;
        }

        IDialogHint hint = getCurDialogHint();
        if (!isReshow) {
            notifier.sendCMsg(uid, new DialogMsg.OnPushMsg(hint));
        }

        notifier.sendCMsg(uid, new DialogMsg.OnSelectMsg(hint));
        notifier.sendCMsg(uid, new DialogMsg.OnSwitchCancelable(shouldDialogCancelable()));
    }

}
