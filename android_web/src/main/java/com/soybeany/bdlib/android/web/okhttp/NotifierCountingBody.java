package com.soybeany.bdlib.android.web.okhttp;

import com.soybeany.bdlib.android.web.RequestNotifier;
import com.soybeany.bdlib.core.util.file.IProgressListener;
import com.soybeany.bdlib.web.okhttp.counting.CountingRequestBody;
import com.soybeany.bdlib.web.okhttp.counting.CountingResponseBody;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * <br>Created by Soybeany on 2019/5/26.
 */
@SuppressWarnings("WeakerAccess")
public class NotifierCountingBody {

    public static class Request extends CountingRequestBody {
        public Request(RequestBody delegate, RequestNotifier notifier) {
            super(delegate);
            if (null != notifier) {
                listeners(set -> set.add(new NotifierListener(notifier, new RequestMsg.OnUpload())));
            }
        }
    }

    public static class Response extends CountingResponseBody {
        public Response(ResponseBody target, RequestNotifier notifier) {
            super(target);
            if (null != notifier) {
                listeners(set -> set.add(new NotifierListener(notifier, new RequestMsg.OnDownload())));
            }
        }
    }

    private static class NotifierListener implements IProgressListener {
        private final RequestNotifier mNotifier;
        private final RequestMsg.Callback<Float> mMsg;

        NotifierListener(RequestNotifier notifier, RequestMsg.Callback<Float> msg) {
            mNotifier = notifier;
            mMsg = msg;
        }

        @Override
        public void inProgress(float percent, long cur, long total) {
            mMsg.data = percent;
            mNotifier.sendCMsgWithDefaultUid(mMsg);
        }
    }
}
