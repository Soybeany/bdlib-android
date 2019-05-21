package com.soybeany.bdlib.project;

import com.soybeany.bdlib.android.mvp.BasePresenter;
import com.soybeany.bdlib.android.util.LogUtils;
import com.soybeany.bdlib.android.util.dialog.DialogNotifier;
import com.soybeany.bdlib.android.util.dialog.msg.StdDialogMsg;
import com.soybeany.bdlib.android.web.UICallback;
import com.soybeany.bdlib.core.util.file.IProgressListener;
import com.soybeany.bdlib.web.okhttp.core.OkHttpCallback;
import com.soybeany.bdlib.web.okhttp.core.OkHttpRequestFactory;
import com.soybeany.bdlib.web.okhttp.parser.StringParser;

import static com.soybeany.bdlib.project.RequestUtils.SERVER;

/**
 * <br>Created by Soybeany on 2019/2/19.
 */
public class TestPresenter extends BasePresenter<ITestView> {

    public void testFile() {
        RequestUtils.newClient(null).showDialog(getTopDialogNotifier(), when -> new StdDialogMsg().hint("测试").cancelable(true))
                .newCall(requestNotifier -> OkHttpRequestFactory.get(SERVER + "/mobile/auth//file").param("test", "测试").build(requestNotifier))
                .enqueue(new OkHttpCallback<>(StringParser.get())
                        .addCallback(new TestCallback())
                        .addDownloadListener(getLogListener("测试")));
//        invoke(v -> v.showToast("有反应"));
    }

    public void testAsync() {
        DialogNotifier notifier = getTopDialogNotifier();
        new Thread(() -> {
            LogUtils.test("任务开始");
            wrapDialog(notifier, new StdDialogMsg().hint("测试异步"), () -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            LogUtils.test("任务结束");
        }).start();
    }

    private class TestCallback implements UICallback<String> {
        @Override
        public void onUISuccess(String s) {
            invoke(v -> v.showMsg("成功:", s));
            LogUtils.test("成功:" + s);
        }

        @Override
        public void onUIFailure(boolean isCanceled, String msg) {
            invoke(v -> v.showMsg("失败:", msg));
            LogUtils.test("失败:" + msg);
        }
    }

    private IProgressListener getLogListener(String desc) {
        return (percent, cur, total) -> {
            LogUtils.i(desc + "进度", "p:" + percent + " c:" + cur + " t:" + total);
        };
    }
}
