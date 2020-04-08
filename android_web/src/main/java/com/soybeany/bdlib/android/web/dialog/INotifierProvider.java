package com.soybeany.bdlib.android.web.dialog;

import com.soybeany.bdlib.android.web.notifier.DNotifiers;

/**
 * <br>Created by Soybeany on 2020/4/2.
 */
public interface INotifierProvider {

    String TYPE_DEFAULT = "default";

    DNotifiers getDialogNotifier(String type);

}
