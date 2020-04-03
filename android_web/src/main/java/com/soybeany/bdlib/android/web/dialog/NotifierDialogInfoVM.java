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
public class NotifierDialogInfoVM extends DialogInfoVM {

    public final Map<String, DialogNotifier> notifiers = new ConcurrentHashMap<>();

    public static NotifierDialogInfoVM get(FragmentActivity activity) {
        return ViewModelProviders.of(activity).get(NotifierDialogInfoVM.class);
    }

    public DialogNotifier getNotifier(String type) {
        DialogNotifier notifier = notifiers.get(type);
        if (null == notifier) {
            notifiers.put(type, notifier = new DialogNotifier());
        }
        return notifier;
    }
}
