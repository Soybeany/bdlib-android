package com.soybeany.bdlib.project;

import com.soybeany.bdlib.android.mvp.BasePresenter;
import com.soybeany.bdlib.android.util.LogUtils;
import com.soybeany.bdlib.android.util.dialog.DialogKeyProvider;
import com.soybeany.bdlib.android.web.UICallback;
import com.soybeany.bdlib.core.util.file.IProgressListener;

/**
 * <br>Created by Soybeany on 2019/2/19.
 */
public class TestPresenter extends BasePresenter<ITestView> {

    public void testFile(DialogKeyProvider provider) {
//        RequestUtils.client(new DialogInfo(provider, new DialogMsg("测试").cancelable(true)), null)
//                .newCall(OkHttpRequestFactory.get(SERVER + "/mobile/auth//file").param("test", "测试").build())
//                .enqueue(new OkHttpCallback<>(StringParser.get())
//                        .addCallback(new TestCallback())
//                        .downloadListener(getLogListener("测试")));
        invoke(v -> v.showToast("有反应"));
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
