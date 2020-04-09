package com.soybeany.bdlib.android.web.okhttp;

import com.soybeany.bdlib.android.web.notifier.RSender;
import com.soybeany.bdlib.web.okhttp.core.OkHttpRequestBuilder;
import com.soybeany.bdlib.web.okhttp.counting.CountingRequestBody;

import okhttp3.RequestBody;

/**
 * <br>Created by Soybeany on 2019/5/29.
 */
public class NotifierRequestBuilder extends OkHttpRequestBuilder {
    private RSender mSender;

    public NotifierRequestBuilder(RSender sender) {
        mSender = sender;
    }

    @Override
    protected CountingRequestBody getNewCountingRequestBody(RequestBody body) {
        return new NotifierCountingBody.Request(body, mSender);
    }
}
