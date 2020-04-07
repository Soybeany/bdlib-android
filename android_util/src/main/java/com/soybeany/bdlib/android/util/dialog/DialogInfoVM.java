package com.soybeany.bdlib.android.util.dialog;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;

import com.soybeany.bdlib.android.util.BDContext;
import com.soybeany.bdlib.android.util.dialog.msg.IDialogHint;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <br>Created by Soybeany on 2020/4/2.
 */
@SuppressWarnings("WeakerAccess")
public class DialogInfoVM extends ViewModel {

    private static final Handler HANDLER = BDContext.MAIN_HANDLER;
    public final Map<String, Info> infoMap = new ConcurrentHashMap<>();

    /**
     * 将全部操作放到主线程进行，确保线程安全
     */
    public void exeInMainThread(Runnable task) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            task.run();
        } else {
            HANDLER.post(task);
        }
    }

    public static DialogInfoVM get(FragmentActivity activity) {
        return ViewModelProviders.of(activity).get(DialogInfoVM.class);
    }

    public DialogInfoVM.Info getInfo(String type) {
        DialogInfoVM.Info info = infoMap.get(type);
        if (null == info) {
            infoMap.put(type, info = new DialogInfoVM.Info());
        }
        return info;
    }

    public static class Info {

        /**
         * 入栈的弹窗信息
         * todo 在添加速度很快，即时间戳相同时，会存在消息丢失的问题
         */
        public final SortedSet<IDialogHint> hintSet = new TreeSet<>();

        /**
         * 入栈的弹窗信息(不可取消)
         */
        public final Set<IDialogHint> unableCancelSet = new HashSet<>();

        /**
         * 是否需要弹窗显示
         */
        public boolean needDialogShow;

        /**
         * 获得当前正在显示的信息
         */
        public IDialogHint getCurDialogHint() {
            return !hintSet.isEmpty() ? hintSet.last() : null;
        }

        /**
         * 弹窗是否应该能取消
         */
        public boolean shouldDialogCancelable() {
            return unableCancelSet.isEmpty();
        }
    }
}
