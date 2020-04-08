package com.soybeany.bdlib.android.web.dialog;

import com.soybeany.bdlib.android.util.dialog.DialogDismissReason;
import com.soybeany.bdlib.android.util.dialog.msg.IDialogHint;
import com.soybeany.bdlib.android.web.msg.DVMsg;
import com.soybeany.bdlib.android.web.notifier.DNotifier;
import com.soybeany.bdlib.android.web.notifier.DVNotifier;
import com.soybeany.connector.ITarget.MsgProcessor;
import com.soybeany.connector.MsgManager;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * <br>Created by Soybeany on 2020/4/7.
 */
public class DialogInfoManager {

    /**
     * 标识是否有弹窗正在显示
     */
    public boolean hasDialogShowing;

    /**
     * 当前的进度
     */
    public float toPercent;

    public final DVNotifier dvNotifier = new DVNotifier();
    public final DNotifier dNotifier = new DNotifier();

    /**
     * 入栈的弹窗信息
     */
    private final SortedSet<IDialogHint> mHintSet = new TreeSet<>();

    /**
     * 入栈的弹窗信息(不可取消)
     */
    private final Set<IDialogHint> mUnableCancelSet = new HashSet<>();

    private final MsgManager<DVMsg.Invoker, DVMsg.Callback> mDvManager = new MsgManager<>();

    // //////////////////////////////////公开方法区//////////////////////////////////

    public void bind() {
        mDvManager.bind(list -> {
            list.add(new MsgProcessor<>(DVMsg.PushMsg.class, msg -> pushMsg(msg.data)));
            list.add(new MsgProcessor<>(DVMsg.PopMsg.class, msg -> popMsg(msg.data)));
            list.add(new MsgProcessor<>(DVMsg.ClearMsg.class, msg -> clearMsg(msg.data)));
            list.add(new MsgProcessor<>(DVMsg.ToProgress.class, msg -> toPercent = msg.data));
        }, dvNotifier, false);
    }

    public void unbind() {
        mDvManager.unbind(false);
    }

    public void pushMsg(IDialogHint msg) {
        if (null == msg) {
            return;
        }
        NotifierDialogVM.exeInMainThread(() -> {
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
            showNewestMsg(false);
        });
    }

    public void popMsg(IDialogHint msg) {
        NotifierDialogVM.exeInMainThread(() -> {
            if (!mHintSet.remove(msg)) {
                return;
            }
            mUnableCancelSet.remove(msg);
            dvNotifier.sendCMsg(new DVMsg.OnPopMsg(msg));
            // 更改或关闭弹窗
            if (!mHintSet.isEmpty()) {
                showNewestMsg(true);
            } else {
                dvNotifier.sendIMsg(new DVMsg.ClearMsg(DialogDismissReason.NORM));
            }
        });
    }

    public void clearMsg(DialogDismissReason reason) {
        dvNotifier.sendCMsg(new DVMsg.OnClearMsg(reason));
        for (IDialogHint msg : mHintSet) {
            dvNotifier.sendCMsg(new DVMsg.OnPopMsg(msg));
        }
        mHintSet.clear();
        mUnableCancelSet.clear();
        // 关闭弹窗
        dvNotifier.sendCMsg(new DVMsg.OnNeedDismissDialog(reason));
        hasDialogShowing = false;
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

    // //////////////////////////////////内部方法//////////////////////////////////

    private void showNewestMsg(boolean isReshow) {
        IDialogHint hint = getCurDialogHint();
        if (!isReshow) {
            dvNotifier.sendCMsg(new DVMsg.OnPushMsg(hint));
        }

        // 若弹窗没有显示，则显示
        if (!hasDialogShowing) {
            dvNotifier.sendCMsg(new DVMsg.OnNeedShowDialog());
            hasDialogShowing = true;
        }

        dvNotifier.sendCMsg(new DVMsg.OnSelectMsg(hint));
        dvNotifier.sendCMsg(new DVMsg.OnSwitchCancelable(shouldDialogCancelable()));
    }

}
