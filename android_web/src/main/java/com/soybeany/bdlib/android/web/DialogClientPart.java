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
        return newRequest(null, setter);
    }

    public DialogRequestPart newRequest(@Nullable DialogInfo info, @Nullable OkHttpClientFactory.IClientSetter setter) {
        return new DialogRequestPart(info, OkHttpClientFactory.getNewClient(setter));
    }
}
