package com.soybeany.bdlib.android.mvp;

import com.soybeany.bdlib.android.template.interfaces.IBaseFunc;

/**
 * <br>Created by Soybeany on 2019/4/18.
 */
class PresenterProviderImpl implements IPresenterProvider {
    private IBaseFunc.IEx mEx;

    PresenterProviderImpl(IBaseFunc.IEx ex) {
        mEx = ex;
    }

    @Override
    public <V extends IPresenterView, T extends BasePresenter<V>> T getPresenter(Class<T> clazz, V v) {
        T presenter = mEx.getViewModel(clazz);
        presenter.bindView(mEx.onGetLifecycle(), v);
        return presenter;
    }
}
