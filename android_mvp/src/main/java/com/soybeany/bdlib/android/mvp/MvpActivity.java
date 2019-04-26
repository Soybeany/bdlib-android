package com.soybeany.bdlib.android.mvp;

import com.soybeany.bdlib.android.template.BaseActivity;

/**
 * <br>Created by Soybeany on 2019/4/18.
 */
public abstract class MvpActivity extends BaseActivity implements IMvpTemplate {
    @Override
    public void signalAfterSetContentView() {
        onInitPresenters(new PresenterProviderImpl(this));
    }
}
