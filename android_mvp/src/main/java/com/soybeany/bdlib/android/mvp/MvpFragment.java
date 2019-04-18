package com.soybeany.bdlib.android.mvp;

import com.soybeany.bdlib.android.template.BaseFragment;

/**
 * <br>Created by Soybeany on 2019/4/18.
 */
public abstract class MvpFragment extends BaseFragment implements IMvpTemplate {
    @Override
    public void onSignalDevCallbacks() {
        onInitPresenters(new PresenterProviderImpl(this));
    }
}
