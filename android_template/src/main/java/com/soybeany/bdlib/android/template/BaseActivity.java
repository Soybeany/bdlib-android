package com.soybeany.bdlib.android.template;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;

import com.soybeany.bdlib.android.template.lifecycle.ButterKnifeObserver;
import com.soybeany.bdlib.android.util.IObserver;
import com.soybeany.bdlib.android.util.dialog.AbstractDialog;
import com.soybeany.bdlib.android.util.dialog.DialogKeyProvider;
import com.soybeany.bdlib.android.util.dialog.ProgressDialogImpl;
import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.util.storage.MessageCenter;

/**
 * <br>Created by Soybeany on 2019/2/1.
 */
public abstract class BaseActivity extends AppCompatActivity implements IInitialHelper, ButterKnifeObserver.ICallback<Activity> {
    private final BaseFuncHelper mFuncHelper = new BaseFuncHelper();
    private AbstractDialog mDialog;

    // //////////////////////////////////方法重写//////////////////////////////////

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setupLayoutResId());
        mFuncHelper.init(getLifecycle(), getViewModel(BaseFuncHelper.VM.class))
                .addObservers(setupObservers(), new ButterKnifeObserver(this),
                        new CallbackObserver(), mDialog = onGetNewDialog());
    }

    // //////////////////////////////////子类回调//////////////////////////////////

    @Override
    public Activity onGetButterKnifeSource() {
        return this;
    }

    protected AbstractDialog onGetNewDialog() {
        return new ProgressDialogImpl(this);
    }

    // //////////////////////////////////便捷方法//////////////////////////////////

    protected AbstractDialog getDialog() {
        return mDialog;
    }

    protected DialogKeyProvider getDialogKeys() {
        return mDialog.getKeyProvider();
    }

    protected void startNewActivity(Class<? extends Activity> activityClazz) {
        startActivity(new Intent(this, activityClazz));
    }

    protected void startNewActivity(Class<? extends Activity> activityClazz, IOptionsSetter setter) {
        Options options = new Options();
        Optional.ofNullable(setter).ifPresent(s -> s.onSetup(options));
        startActivity(new Intent(this, activityClazz), options.getAnimationBundle());
        if (options.autoFinish) {
            finish();
        }
    }

    protected <T extends ViewModel> T getViewModel(Class<T> modelClass) {
        return ViewModelProviders.of(this).get(modelClass);
    }

    /**
     * 注册回调，自动注销
     *
     * @return 用于监听的uid
     */
    protected String autoRegister(String key, MessageCenter.ICallback callback) {
        return mFuncHelper.autoRegister(key, callback);
    }

    // //////////////////////////////////内部实现//////////////////////////////////


    // //////////////////////////////////内部类//////////////////////////////////

    public static class Options {
        boolean autoFinish;
        ActivityOptionsCompat animation;

        public Options autoFinish(boolean flag) {
            autoFinish = flag;
            return this;
        }

        public Options animation(ActivityOptionsCompat optionsCompat) {
            animation = optionsCompat;
            return this;
        }

        Bundle getAnimationBundle() {
            return null != animation ? animation.toBundle() : null;
        }
    }

    protected interface IOptionsSetter {
        void onSetup(Options options);
    }

    private class CallbackObserver implements IObserver {
        private boolean mSignaledDoBusiness;

        @Override
        public void onCreate(@NonNull LifecycleOwner owner) {
            onInit();
        }

        @Override
        public void onResume(@NonNull LifecycleOwner owner) {
            if (!mSignaledDoBusiness) {
                doBusiness();
                mSignaledDoBusiness = true;
            }
        }
    }
}
