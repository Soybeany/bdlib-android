package com.soybeany.bdlib.android.web.okhttp;

import com.soybeany.bdlib.android.web.msg.RMsg;
import com.soybeany.bdlib.android.web.notifier.RNotifier;
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
        public Request(RequestBody delegate, RNotifier notifier) {
            super(delegate);
            if (null != notifier) {
                listeners(set -> set.add(new NotifierListener(notifier, new RMsg.OnUpload())));
            }
        }
    }

    public static class Response extends CountingResponseBody {
        public Response(ResponseBody target, RNotifier notifier) {
            super(target);
            if (null != notifier) {
                listeners(set -> set.add(new NotifierListener(notifier, new RMsg.OnDownload())));
            }
        }
    }

    private static class NotifierListener implements IProgressListener {
        private final RNotifier mNotifier;
        private final RMsg.Callback<Float> mMsg;

        NotifierListener(RNotifier notifier, RMsg.Callback<Float> msg) {
            mNotifier = notifier;
            mMsg = msg;
        }

        @Override
        public void inProgress(float percent, long cur, long total) {
            mMsg.data = percent;
            mNotifier.sendCMsg(mMsg);
        }
    }
}
