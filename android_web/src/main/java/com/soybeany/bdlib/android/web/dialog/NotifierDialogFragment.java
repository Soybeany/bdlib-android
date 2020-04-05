package com.soybeany.bdlib.android.web.dialog;

import android.content.DialogInterface;

import com.soybeany.bdlib.android.util.dialog.DialogDismissReason;
import com.soybeany.bdlib.android.util.dialog.ProgressDialogFragment;
import com.soybeany.bdlib.android.web.DialogNotifier;

/**
 * <br>Created by Soybeany on 2020/4/2.
 */
public class NotifierDialogFragment extends ProgressDialogFragment implements INotifierRealDialog {

    private DialogNotifier mNotifier;

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        mNotifier.sendCMsgWithDefaultUid(new DialogMsg.OnDismissDialog(DialogDismissReason.CANCEL));
    }

    @Override
    public void onSetupNotifier(DialogNotifier notifier) {
        mNotifier = notifier;
    }
}
