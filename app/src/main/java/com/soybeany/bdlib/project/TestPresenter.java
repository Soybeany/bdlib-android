package com.soybeany.bdlib.project;

import com.soybeany.bdlib.android.mvp.BasePresenter;
import com.soybeany.bdlib.android.util.LogUtils;
import com.soybeany.bdlib.android.util.ToastUtils;
import com.soybeany.bdlib.android.util.dialog.msg.StdDialogHint;
import com.soybeany.bdlib.android.web.DialogNotifier;
import com.soybeany.bdlib.android.web.UICallback;
import com.soybeany.bdlib.core.util.file.IProgressListener;

/**
 * <br>Created by Soybeany on 2019/2/19.
 */
public class TestPresenter extends BasePresenter<ITestView> {

//    public void testFile() {
//        RequestUtils.newRequest(setter -> setter.connectTimeout(5, TimeUnit.SECONDS))
//                .showDialog(getTopDialogNotifier(), when -> new StdDialogHint().hint("测试").cancelable(true))
//                .newCall((builder, notifier) -> builder.url(new ParamAppender().add("test", "测试").toUrl(SERVER + "/mobile/auth//file")).build())
//                .enqueue(new OkHttpCallback<>(StringParser.get())
//                        .addCallback(new TestCallback())
//                        .addDownloadListener(getLogListener("测试")));
//        invoke(v -> v.showToast("请求开始"));
//    }

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
