package com.soybeany.bdlib.project;

import com.soybeany.bdlib.android.mvp.BasePresenter;
import com.soybeany.bdlib.android.util.LogUtils;
import com.soybeany.bdlib.android.util.ToastUtils;
import com.soybeany.bdlib.android.util.dialog.msg.IDialogHint;
import com.soybeany.bdlib.android.util.dialog.msg.StdDialogHint;
import com.soybeany.bdlib.android.web.UICallback;
import com.soybeany.bdlib.android.web.msg.DVMsg;
import com.soybeany.bdlib.android.web.notifier.DNotifiers;
import com.soybeany.bdlib.android.web.notifier.RNotifier;
import com.soybeany.bdlib.android.web.okhttp.NotifierCall;
import com.soybeany.bdlib.core.util.file.IProgressListener;
import com.soybeany.bdlib.web.okhttp.core.OkHttpCallback;
import com.soybeany.bdlib.web.okhttp.core.OkHttpRequestBuilder;
import com.soybeany.bdlib.web.okhttp.core.OkHttpUtils;
import com.soybeany.bdlib.web.okhttp.parser.StringParser;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * <br>Created by Soybeany on 2019/2/19.
 */
public class TestPresenter extends BasePresenter<ITestView> {

    private String mUrl = "http://192.168.0.104:8080/test/test.do";

    public void testFile() {
        OkHttpClient client = OkHttpUtils.getNewClient(setter -> OkHttpUtils.IClientSetter.setupTimeout(setter, 5));
        RNotifier rNotifier = new RNotifier(new StdDialogHint().cancelable(true));
        Request request = new OkHttpRequestBuilder().url(mUrl).build();
        Call call = new NotifierCall(client.newCall(request), rNotifier, getTopDialogNotifier());
        call.enqueue(new OkHttpCallback<>(StringParser.get())
                .addCallback(new TestCallback())
                .addDownloadListener(getLogListener("测试"))
        );
//        RequestUtils.newRequest()
//                .showDialog(getTopDialogNotifier(), when -> new StdDialogHint().hint("测试").cancelable(true))
//                .newCall((builder, notifier) -> builder.url(new ParamAppender().add("test", "测试").toUrl(SERVER + "/mobile/auth//file")).build())
//                .enqueue(new OkHttpCallback<>(StringParser.get())
//                        .addCallback(new TestCallback())
//                        .addDownloadListener(getLogListener("测试")));
        invoke(v -> v.showToast("请求开始"));
    }

    public void testAsync() {
        new Thread(() -> {
            DNotifiers notifier = getTopDialogNotifier();
            uiInvoke(v -> v.showMsg("任务", "开始"));
            wrapDialog(notifier, new StdDialogHint().hint("测试异步"), () -> {
                try {
                    Thread.sleep(1000);
                    IDialogHint hint = new StdDialogHint().hint("第二条");
                    IDialogHint hint3 = new StdDialogHint().hint("第4条");
                    IDialogHint hint4 = new StdDialogHint().hint("第5条");
                    IDialogHint hint5 = new StdDialogHint().hint("第6条");
                    IDialogHint hint6 = new StdDialogHint().hint("第7条");
                    notifier.sender.sendIMsg(new DVMsg.PushMsg(hint));
                    notifier.sender.sendIMsg(new DVMsg.PushMsg(hint3));
                    notifier.sender.sendIMsg(new DVMsg.PushMsg(hint4));
                    notifier.sender.sendIMsg(new DVMsg.PushMsg(hint5));
                    notifier.sender.sendIMsg(new DVMsg.PushMsg(hint6));
                    Thread.sleep(1000);
                    IDialogHint hint2 = new StdDialogHint().hint("第三条");
                    notifier.sender.sendIMsg(new DVMsg.PushMsg(hint2));
                    Thread.sleep(1000);
                    notifier.sender.sendIMsg(new DVMsg.PopMsg(hint2));
                    Thread.sleep(1000);
                    notifier.sender.sendIMsg(new DVMsg.PopMsg(hint6));
                    notifier.sender.sendIMsg(new DVMsg.PopMsg(hint5));
                    notifier.sender.sendIMsg(new DVMsg.PopMsg(hint4));
                    notifier.sender.sendIMsg(new DVMsg.PopMsg(hint3));
                    notifier.sender.sendIMsg(new DVMsg.PopMsg(hint));

                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            uiInvoke(v -> v.showMsg("任务", "结束"));
        }).start();
    }

    private class TestCallback implements UICallback<String> {
        @Override
        public void onUISuccess(int id, String s) {
            showMsg("成功:", s);
        }

        @Override
        public void onUIFailure(int id, int type, String msg) {
            showMsg("失败:", msg);
        }

        private void showMsg(String desc, String msg) {
            if (!invoke(v -> v.showMsg(desc, msg))) {
                ToastUtils.show("View视图为空，未消费信息为:" + desc + msg);
            }
        }
    }

    private IProgressListener getLogListener(String desc) {
        return (percent, cur, total) -> {
            LogUtils.i(desc + "进度", "p:" + percent + " c:" + cur + " t:" + total);
        };
    }
}
