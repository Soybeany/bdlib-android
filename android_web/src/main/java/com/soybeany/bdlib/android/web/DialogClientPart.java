package com.soybeany.bdlib.android.web;

import android.support.annotation.Nullable;

import com.soybeany.bdlib.web.okhttp.OkHttpUtils;
import com.soybeany.bdlib.web.okhttp.core.OkHttpClientFactory;

/**
 * <br>Created by Soybeany on 2019/4/16.
 */
public class DialogClientPart extends OkHttpUtils.DefaultClientPart {

    @Override
    public DialogRequestPart newRequest(@Nullable OkHttpClientFactory.IClientSetter setter) {
        return newRequest(setter, null);
    }

    public DialogRequestPart newRequest(OkHttpClientFactory.IClientSetter cs, IDialogSetter ds) {
        return new DialogRequestPart(null != ds ? ds.onSetup(new DialogInfo()) : null, OkHttpClientFactory.getNewClient(cs));
    }

    public interface IDialogSetter {
        DialogInfo onSetup(DialogInfo info);
    }
}
