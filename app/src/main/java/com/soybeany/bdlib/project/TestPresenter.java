package com.soybeany.bdlib.project;

import com.soybeany.bdlib.android.mvp.BasePresenter;
import com.soybeany.bdlib.android.util.LogUtils;
import com.soybeany.bdlib.android.util.ToastUtils;
import com.soybeany.bdlib.android.util.dialog.msg.StdDialogHint;
import com.soybeany.bdlib.android.web.UICallback;
import com.soybeany.bdlib.android.web.notifier.DialogNotifier;
import com.soybeany.bdlib.android.web.notifier.RequestNotifier;
import com.soybeany.bdlib.android.web.okhttp.NotifierCall;
import com.soybeany.bdlib.core.util.file.IProgressListener;
import com.soybeany.bdlib.web.okhttp.core.OkHttpCallback;
import com.soybeany.bdlib.web.okhttp.core.OkHttpRequestBuilder;
import com.soybeany.bdlib.web.okhttp.core.OkHttpUtils;
import com.soybeany.bdlib.web.okhttp.parser.StringParser;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * <br>Created by Soybeany on 2019/2/19.
 */
public class TestPresenter extends BasePresenter<ITestView> {

    private String mUrl = "http://192.168.0.104:8080/test/test.do";

    public void testFile() {
        OkHttpClient client = OkHttpUtils.getNewClient(setter -> OkHttpUtils.IClientSetter.setupTimeout(setter, 3));
        RequestNotifier rNotifier = new RequestNotifier(new StdDialogHint());
        Request request = new OkHttpRequestBuilder().url(mUrl).build();
        NotifierCall call = new NotifierCall(client.newCall(request), rNotifier, getTopDialogNotifier());
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
        DialogNotifier notifier = getTopDialogNotifier();
        new Thread(() -> {
            uiInvoke(v -> v.showMsg("任务", "开始"));
            wrapDialog(notifier, new StdDialogHint().hint("测试异步"), () -> {
                try {
                    Thread.sleep(5000);
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
