package com.soybeany.bdlib.android.web.dialog;

import android.content.DialogInterface;

import com.soybeany.bdlib.android.util.dialog.DialogDismissReason;
import com.soybeany.bdlib.android.util.dialog.ProgressDialogFragment;
import com.soybeany.bdlib.android.web.msg.DVMsg;
import com.soybeany.bdlib.android.web.notifier.DVSender;

/**
 * <br>Created by Soybeany on 2020/4/2.
 */
public class NotifierDialogFragment extends ProgressDialogFragment implements INotifierRealDialog {

    private DVSender mSender;

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        mSender.sendIMsg(new DVMsg.ClearMsg(DialogDismissReason.CANCEL));
    }

    @Override
    public void onSetupDVNotifier(DVSender sender) {
        mSender = sender;
    }
}
