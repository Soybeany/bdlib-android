package com.soybeany.bdlib.android.web;

import android.support.annotation.Nullable;

import com.soybeany.bdlib.android.util.dialog.DialogKeyProvider;
import com.soybeany.bdlib.android.util.dialog.DialogMsg;

/**
 * <br>Created by Soybeany on 2019/4/16.
 */
public class DialogInfo {
    @Nullable
    DialogKeyProvider provider;
    @Nullable
    DialogMsg msg;

    public DialogInfo(@Nullable DialogKeyProvider provider, @Nullable DialogMsg msg) {
        this.provider = provider;
        this.msg = msg;
    }
}