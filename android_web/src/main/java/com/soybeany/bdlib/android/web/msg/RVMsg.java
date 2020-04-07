package com.soybeany.bdlib.android.web.msg;

import com.soybeany.connector.Msg;

/**
 * <br>Created by Soybeany on 2020/4/7.
 */
public class RVMsg {

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
     * 需要取消
     */
    public static class NeedCancel extends Invoker<Object> {
        public NeedCancel() {
            super(null);
        }
    }

    // //////////////////////////////////Callback区//////////////////////////////////

    /**
     * 收到取消指令时通知
     */
    public static class OnNeedCancel extends Callback<Object> {
        public OnNeedCancel() {
            super(null);
        }
    }

}
