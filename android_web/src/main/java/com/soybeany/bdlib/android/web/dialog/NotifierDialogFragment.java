package com.soybeany.bdlib.android.web.dialog;

import android.content.DialogInterface;

import com.soybeany.bdlib.android.util.dialog.DialogDismissReason;
import com.soybeany.bdlib.android.util.dialog.ProgressDialogFragment;
import com.soybeany.bdlib.android.web.msg.InfoMsg;
import com.soybeany.bdlib.android.web.notifier.InfoNotifier;

/**
 * <br>Created by Soybeany on 2020/4/2.
 */
public class NotifierDialogFragment extends ProgressDialogFragment implements INotifierRealDialog {

    private InfoNotifier mNotifier;

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        mNotifier.sendCMsgWithDefaultUid(new InfoMsg.OnDismissDialog(DialogDismissReason.CANCEL));
    }

    @Override
    public void onSetupNotifier(InfoNotifier notifier) {
        mNotifier = notifier;
    }
}
