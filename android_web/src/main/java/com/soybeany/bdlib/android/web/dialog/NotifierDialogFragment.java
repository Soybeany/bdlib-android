package com.soybeany.bdlib.android.web.dialog;

import android.content.DialogInterface;

import com.soybeany.bdlib.android.util.dialog.DialogDismissReason;
import com.soybeany.bdlib.android.util.dialog.ProgressDialogFragment;
import com.soybeany.bdlib.android.web.msg.DVMsg;
import com.soybeany.bdlib.android.web.notifier.DVNotifier;

/**
 * <br>Created by Soybeany on 2020/4/2.
 */
public class NotifierDialogFragment extends ProgressDialogFragment implements INotifierRealDialog {

    private DVNotifier mNotifier;

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        mNotifier.sendIMsg(new DVMsg.ClearMsg(DialogDismissReason.CANCEL));
    }

    @Override
    public void onSetupDVNotifier(DVNotifier notifier) {
        mNotifier = notifier;
    }
}
