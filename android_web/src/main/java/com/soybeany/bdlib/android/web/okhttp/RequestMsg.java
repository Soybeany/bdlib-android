package com.soybeany.bdlib.android.web.okhttp;

import com.soybeany.connector.Msg;

/**
 * <br>Created by Soybeany on 2019/5/8.
 */
@SuppressWarnings("WeakerAccess")
public class RequestMsg {

    // //////////////////////////////////模板区//////////////////////////////////

    public static class Invoker<Data> extends Msg.I<Data> {
        public Invoker(Data data) {
            super(data);
        }
    }

    public static class Callback<Data> extends Msg.C<Data> {
        public Callback(Data data) {
            super(data);
        }
    }

    // //////////////////////////////////Invoker区//////////////////////////////////

    /**
     * 取消请求，data为null
     */
    public static class Cancel extends Invoker<Object> {
        public Cancel() {
            super(null);
        }
    }

    // //////////////////////////////////Callback区//////////////////////////////////

    /**
     * 请求开始，data为null
     */
    public static class OnStart extends Callback<Object> {
        public OnStart() {
            super(null);
        }
    }

    /**
     * 请求完成，data为{@link RequestFinishReason}
     */
    public static class OnFinish extends Callback<RequestFinishReason> implements Msg.EndFlag {
        public OnFinish(RequestFinishReason reason) {
            super(reason);
        }
    }

    /**
     * 上传，data为float的进度(正常 0~1，缺失-1)
     */
    public static class OnUpload extends Callback<Float> {
        public OnUpload() {
            super(null);
        }
    }

    /**
     * 下载，data为float的进度(正常 0~1，缺失-1)
     */
    public static class OnDownload extends Callback<Float> {
        public OnDownload() {
            super(null);
        }
    }
}
