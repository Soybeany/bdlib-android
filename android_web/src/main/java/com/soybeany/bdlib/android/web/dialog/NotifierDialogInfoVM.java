package com.soybeany.bdlib.android.web.dialog;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;

import com.soybeany.bdlib.android.util.dialog.DialogInfoVM;
import com.soybeany.bdlib.android.web.DialogNotifier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <br>Created by Soybeany on 2020/4/2.
 */
// TODO: 2020/4/6
//  1.将Manager也放入这里管理，以避免Activity切换期间，请求完成了，但没有监听器接收到信息
//  2.若中途直接关闭Activity，剩余的msg未pop，dismissDialog未调用
public class NotifierDialogInfoVM extends DialogInfoVM {

    public final Map<String, DialogNotifier> notifiers = new ConcurrentHashMap<>();

    public static NotifierDialogInfoVM get(FragmentActivity activity) {
        return ViewModelProviders.of(activity).get(NotifierDialogInfoVM.class);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
//        for (DialogNotifier notifier : notifiers.values()) {
//            notifier.sendCMsgWithDefaultUid(new DialogMsg.OnDismissDialog(DialogDismissReason.DESTROY));
//        }
    }

    public DialogNotifier getNotifier(String type) {
        DialogNotifier notifier = notifiers.get(type);
        if (null == notifier) {
            notifiers.put(type, notifier = new DialogNotifier());
        }
        return notifier;
    }
}
