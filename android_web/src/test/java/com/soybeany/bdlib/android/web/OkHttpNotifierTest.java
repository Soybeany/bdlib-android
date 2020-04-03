//package com.soybeany.bdlib.android.web;
//
//import com.soybeany.bdlib.core.util.notify.IConnectLogic;
//import com.soybeany.bdlib.core.util.notify.INotifyMsg;
//import com.soybeany.bdlib.core.util.notify.Notifier;
//import com.soybeany.bdlib.web.okhttp.core.ICallback;
//import com.soybeany.bdlib.web.okhttp.core.ParamAppender;
//import com.soybeany.bdlib.web.okhttp.counting.CountingRequestBody;
//import com.soybeany.bdlib.web.okhttp.counting.CountingResponseBody;
//import com.soybeany.bdlib.web.okhttp.notify.NotifierCallback;
//import com.soybeany.bdlib.web.okhttp.notify.OkHttpNotifierUtils;
//import com.soybeany.bdlib.web.okhttp.notify.RequestNotifier;
//import com.soybeany.bdlib.web.okhttp.notify.RequestNotifierMsg.OnFinish;
//import com.soybeany.bdlib.web.okhttp.notify.RequestNotifierMsg.OnStart;
//import com.soybeany.bdlib.web.okhttp.parser.StringParser;
//
//import org.junit.Test;
//
//import java.io.IOException;
//import java.util.concurrent.locks.LockSupport;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.FormBody;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import okhttp3.ResponseBody;
//import okhttp3.internal.annotations.EverythingIsNonNull;
//
///**
// * <br>Created by Soybeany on 2019/5/27.
// */
//public class OkHttpNotifierTest {
//
//    private String mUrl = "http://192.168.137.78:8080/mobile/auth/file";
//    private Thread mMainThread = Thread.currentThread();
//
//    @Test
//    public void test1() {
//        OkHttpNotifierUtils.newClient(Notifier.class).connector(new OkHttpNotifierUtils.IConnectorSetter<Notifier>() {
//            @Override
//            public Notifier getNewNotifier() {
//                return new Notifier();
//            }
//
//            @Override
//            public Class<? extends INotifyMsg> getDMClass() {
//                return EndMsg.class;
//            }
//
//            @Override
//            public void onSetupLogic(IConnectLogic.IApplier<RequestNotifier, Notifier> applier) {
//                applier.addLogic(OnStart.class, (msg, n1, n2) -> System.out.println("逻辑:" + msg + " n1:" + n1 + " n2:" + n2));
//                applier.addLogic(OnFinish.class, (msg, n1, n2) -> System.out.println("逻辑2:" + msg + " n1:" + n1 + " n2:" + n2));
//            }
//        }).newNotifierRequest().newCall((builder, notifier) -> {
//            notifier.callback().addListener(msg -> {
//                System.out.println("收到通知:" + msg);
//            });
//            return builder.addUploadListener((percent, cur, total) -> System.out.println("上传进度:" + percent)).url(mUrl)
//                    .post(new ParamAppender().add("what", "new Day").toNewFormBody().build()).build();
//        }).enqueue(new NotifierCallback<>(StringParser.get()).addCallback(new ICallback<String>() {
//            @Override
//            public void onSuccess(int id, String s) {
//                System.out.println("成功:" + s);
//            }
//
//            @Override
//            public void onFailure(int id, boolean isCanceled, String msg) {
//                System.out.println("失败:" + msg);
//            }
//
//            @Override
//            public void onFinal(int id, boolean isCanceled) {
//                LockSupport.unpark(mMainThread);
//            }
//        }));
//
//        LockSupport.park();
//    }
//
//    @Test
//    @EverythingIsNonNull
//    public void test2() throws Exception {
//        FormBody formBody = new FormBody.Builder().add("test", "success").build();
//        Request request = new Request.Builder().url(mUrl).post(new CountingRequestBody(formBody).listeners(set ->
//                set.add((percent, cur, total) -> System.out.println("request进度:" + percent)))
//        ).build();
//
//        OkHttpClient client = new OkHttpClient();
//        for (int i = 0; i < 20; i++) {
//            int finalI = i;
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    System.out.println("异常" + finalI + ":" + e.getMessage());
//                    LockSupport.unpark(mMainThread);
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    try (ResponseBody body = new CountingResponseBody(response.body())
//                            .listeners(set -> set.add((percent, cur, total) -> System.out.println("response进度" + finalI + ":" + percent)))
//                    ) {
//                        System.out.println("结果" + finalI + ":" + body.string());
//                    }
//                    LockSupport.unpark(mMainThread);
//                }
//            });
//        }
//        LockSupport.park();
//    }
//
//    private void test3() {
////        OkHttpNotifierUtils.newClient(DiyNotifier.class).connector(new DefaultConnector()).newNotifierRequest()
//    }
//
//    public static class DefaultConnector implements OkHttpNotifierUtils.IConnectorSetter<DiyNotifier> {
//        @Override
//        public DiyNotifier getNewNotifier() {
//            return null;
//        }
//
//        @Override
//        public Class<? extends INotifyMsg> getDMClass() {
//            return null;
//        }
//
//        @Override
//        public void onSetupLogic(IConnectLogic.IApplier<RequestNotifier, DiyNotifier> applier) {
//
//        }
//    }
//
//    public static class DiyNotifier extends Notifier {
//
//    }
//
//    private static class EndMsg implements INotifyMsg {
//        @Override
//        public Object getData() {
//            return null;
//        }
//    }
//}
