package com.soybeany.bdlib.android.web.dialog;

import com.soybeany.bdlib.android.web.notifier.DVNotifier;

/**
 * <br>Created by Soybeany on 2020/4/2.
 */
public interface INotifierProvider {

    String TYPE_DEFAULT = "default";

    DVNotifier getDialogNotifier(String type);

}
