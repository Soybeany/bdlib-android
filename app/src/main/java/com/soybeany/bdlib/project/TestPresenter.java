package com.soybeany.bdlib.project;

import com.soybeany.bdlib.android.util.LogUtils;
import com.soybeany.bdlib.android.util.dialog.DialogKeyProvider;
import com.soybeany.bdlib.android.util.dialog.DialogMsg;
import com.soybeany.bdlib.android.web.LifecycleCallback;
import com.soybeany.bdlib.android.web.OkHttpUICallback;
import com.soybeany.bdlib.core.util.file.IProgressListener;
import com.soybeany.bdlib.web.okhttp.core.ICallback;
import com.soybeany.bdlib.web.okhttp.core.OkHttpRequestFactory;
import com.soybeany.bdlib.web.okhttp.parser.StringParser;

/**
 * <br>Created by Soybeany on 2019/2/19.
 */
public class TestPresenter {
    public void testFile(DialogKeyProvider provider, LifecycleCallback<String> callback) {
        RequestUtils.client(null).newDialogCall(provider, () -> OkHttpRequestFactory.get("http://192.168.137.232:8080/mobile/auth//file").param("test", "测试").build())
                .enqueue(new DialogMsg("测试").cancelable(true),
                        new OkHttpUICallback<>(StringParser.get()).addUICallback(callback)
                                .addNonUICallback(new TestCallback()).downloadListener(getLogListener("测试"))
                );
    }

    private class TestCallback implements ICallback<String> {
        @Override
        public void onSuccess(String s) {
            LogUtils.test("成功:" + s);
        }

        @Override
        public void onFailure(boolean b, String s) {
            LogUtils.test("失败:" + s);
        }
    }

    private IProgressListener getLogListener(String desc) {
        return (percent, cur, total) -> {
            LogUtils.i(desc + "进度", "p:" + percent + " c:" + cur + " t:" + total);
        };
    }
}
