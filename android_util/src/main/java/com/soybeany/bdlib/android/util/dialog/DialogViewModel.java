package com.soybeany.bdlib.android.util.dialog;

import android.arch.lifecycle.ViewModel;

import com.soybeany.bdlib.android.util.BDContext;
import com.soybeany.bdlib.core.util.storage.MessageCenter;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * 弹窗的VM(备忘录模式)
 * <br>Created by Soybeany on 2019/3/21.
 */
public class DialogViewModel extends ViewModel {
    /**
     * 可以在{@link MessageCenter}中监听该uid，以获取弹窗关闭时的通知
     */
    public final String key = "DialogVM-onDismiss:" + BDContext.getUID();

    /**
     * 收录的弹窗信息
     */
    private final SortedSet<DialogMsg> msgSet = new TreeSet<>();

    /**
     * 收录的弹窗信息(不可取消)
     */
    private final Set<DialogMsg> unableCancelSet = new HashSet<>();

    /**
     * 标识弹窗是否正在显示
     */
    public boolean isShowing;

    @Override
    protected void onCleared() {
        if (isShowing) {
            MessageCenter.notify(key, Reason.OTHER, 0);
        }
        super.onCleared();
    }

    /**
     * @return 是否添加成功
     */
    public boolean addMsg(DialogMsg msg) {
        if (null == msg) {
            return false;
        }
        // 不可取消列表按需设置
        if (msg.cancelable) {
            unableCancelSet.remove(msg);
        } else {
            unableCancelSet.add(msg);
        }
        // 设置最后弹窗时间
        msg.showTime = System.currentTimeMillis();
        // 添加至信息集(可能覆盖)
        msgSet.add(msg);
        return true;
    }

    /**
     * @return 是否移除成功
     */
    public boolean removeMsg(DialogMsg msg) {
        // 无效信息则不作处理
        if (null == msg || !msgSet.remove(msg)) {
            return false;
        }
        unableCancelSet.remove(msg);
        return true;
    }

    /**
     * 获取最新的信息
     */
    public DialogMsg getNewestMsg() {
        return !msgSet.isEmpty() ? msgSet.last() : null;
    }

    public String getHint(DialogMsg msg) {
        String mainHint = msgSet.size() > 1 ? msg.hintListener.getHint(msg.hint, msgSet.size(), cancelable()) : msg.hint;
        return msg.prefix + mainHint + msg.suffix;
    }

    public boolean cancelable() {
        return unableCancelSet.isEmpty();
    }

    public void clearMsgSet() {
        msgSet.clear();
        unableCancelSet.clear();
    }

    public enum Reason {
        CANCEL, NORM, OTHER
    }
}
