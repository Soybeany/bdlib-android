package com.soybeany.bdlib.android.web.dialog;

import com.soybeany.bdlib.android.util.dialog.IRealDialog;
import com.soybeany.bdlib.android.web.notifier.DVNotifier;

/**
 * <br>Created by Soybeany on 2020/4/2.
 */
public interface INotifierRealDialog extends IRealDialog {

    void onSetupDVNotifier(DVNotifier notifier);

}
