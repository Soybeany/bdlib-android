package com.soybeany.bdlib.android.util.dialog;

import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.util.HandlerThreadImpl;
import com.soybeany.bdlib.android.util.dialog.msg.DialogCallbackMsg;
import com.soybeany.bdlib.android.util.dialog.msg.DialogInvokerMsg;
import com.soybeany.bdlib.core.util.notify.Notifier;

/**
 * <br>Created by Soybeany on 2019/5/17.
 */
public class DialogNotifier extends Notifier<DialogInvokerMsg, DialogCallbackMsg> {

    public static DialogNotifier getNew() {
        return new DialogNotifier();
    }

    protected DialogNotifier() {
        super(HandlerThreadImpl.UI_THREAD);
    }

    public interface IProvider {
        String TYPE_DEFAULT = "default";

        @Nullable
        default DialogNotifier getDialogNotifier() {
            return getDialogNotifier(TYPE_DEFAULT);
        }

        @Nullable
        DialogNotifier getDialogNotifier(String type);
    }
}
