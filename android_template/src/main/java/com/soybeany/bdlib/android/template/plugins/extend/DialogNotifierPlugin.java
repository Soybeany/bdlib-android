package com.soybeany.bdlib.android.template.plugins.extend;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;
import com.soybeany.bdlib.android.util.dialog.DialogNotifier;

/**
 * <br>Created by Soybeany on 2019/4/30.
 */
public class DialogNotifierPlugin implements IExtendPlugin, DialogNotifier.MultiTypeProvider {
    private final DialogNotifier.VM mVM;

    public DialogNotifierPlugin(@NonNull FragmentActivity activity, @Nullable DialogNotifier.IDialogProvider provider) {
        mVM = DialogNotifier.VM.initAndGet(activity, provider);
    }

    @NonNull
    @Override
    public final String getGroupId() {
        return "Dialog";
    }

    @Nullable
    @Override
    public DialogNotifier getDialogNotifier(String type) {
        return mVM.getNotifier(type);
    }
}
