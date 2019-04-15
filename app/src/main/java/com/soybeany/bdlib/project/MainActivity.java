package com.soybeany.bdlib.project;

import android.view.View;

import com.soybeany.bdlib.android.template.BaseActivity;
import com.soybeany.bdlib.android.util.ToastUtils;
import com.soybeany.bdlib.android.web.LifecycleCallback;


public class MainActivity extends BaseActivity {

    private TestPresenter pt = new TestPresenter();

    @Override
    public int setupLayoutResId() {
        return R.layout.activity_main;
    }

    public void onClick(View view) {
        pt.testFile(getDialogKeys(), new LifecycleCallback<String>(this) {
            @Override
            public void onUISuccess(String s) {
                ToastUtils.show("成功:" + s);
            }

            @Override
            public void onUIFailure(boolean isCanceled, String msg) {
                ToastUtils.show("失败:" + msg);
            }
        });
    }
}
