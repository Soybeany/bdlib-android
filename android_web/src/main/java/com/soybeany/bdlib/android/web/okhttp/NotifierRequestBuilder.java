package com.soybeany.bdlib.android.web.okhttp;

import com.soybeany.bdlib.android.web.notifier.RequestNotifier;
import com.soybeany.bdlib.web.okhttp.core.OkHttpRequestBuilder;
import com.soybeany.bdlib.web.okhttp.counting.CountingRequestBody;

import okhttp3.RequestBody;

/**
 * <br>Created by Soybeany on 2019/5/29.
 */
public class NotifierRequestBuilder extends OkHttpRequestBuilder {
    private RequestNotifier mNotifier;

    public NotifierRequestBuilder(RequestNotifier notifier) {
        mNotifier = notifier;
    }

    @Override
    protected CountingRequestBody getNewCountingRequestBody(RequestBody body) {
        return new NotifierCountingBody.Request(body, mNotifier);
    }
}
