package com.soybeany.bdlib.android.web.okhttp;

import com.soybeany.bdlib.android.web.msg.RMsg;
import com.soybeany.bdlib.android.web.notifier.RSender;
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
        public Request(RequestBody delegate, RSender sender) {
            super(delegate);
            if (null != sender) {
                listeners(set -> set.add(new NotifierListener(sender, new RMsg.OnUpload())));
            }
        }
    }

    public static class Response extends CountingResponseBody {
        public Response(ResponseBody target, RSender sender) {
            super(target);
            if (null != sender) {
                listeners(set -> set.add(new NotifierListener(sender, new RMsg.OnDownload())));
            }
        }
    }

    private static class NotifierListener implements IProgressListener {
        private final RSender mSender;
        private final RMsg.Callback<Float> mMsg;

        NotifierListener(RSender sender, RMsg.Callback<Float> msg) {
            mSender = sender;
            mMsg = msg;
        }

        @Override
        public void inProgress(float percent, long cur, long total) {
            mMsg.data = percent;
            mSender.sendCMsg(mMsg);
        }
    }
}
