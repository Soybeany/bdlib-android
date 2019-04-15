package com.soybeany.bdlib.project;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.view.View;

import com.soybeany.bdlib.android.template.BaseActivity;
import com.soybeany.bdlib.android.util.IObserver;
import com.soybeany.bdlib.android.util.LogUtils;
import com.soybeany.bdlib.android.util.ToastUtils;
import com.soybeany.bdlib.android.web.LifecycleCallback;

/**
 * <br>Created by Soybeany on 2019/4/15.
 */
public class SecondActivity extends BaseActivity {
    private TestPresenter pt = TestPresenter.INSTANCE;

    @Override
    public int setupLayoutResId() {
        return R.layout.activity_second;
    }

    @Override
    public LifecycleObserver[] setupObservers() {
        return new LifecycleObserver[]{new TestCallback()};
    }

    private class TestCallback extends LifecycleCallback<String> implements IObserver {
        public TestCallback() {
            super(SecondActivity.this);
        }

        @Override
        public void onCreate(@NonNull LifecycleOwner owner) {
            pt.callback.addUICallback(this);
        }

        @Override
        public void onDestroy(@NonNull LifecycleOwner owner) {
            pt.callback.removeCallback(this);
        }

        @Override
        public void onUISuccess(String s) {
            LogUtils.test("成功有效果");
            ToastUtils.show("成功:" + s);
        }

        @Override
        public void onUIFailure(boolean isCanceled, String msg) {
            LogUtils.test("失败有效果");
            ToastUtils.show("失败:" + msg);
        }
    }

    public void onClick(View view) {
        pt.testFile(getDialogKeys());
    }
}
