package com.soybeany.bdlib.android.mvp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;

import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;
import com.soybeany.bdlib.android.template.interfaces.IVMProvider;

/**
 * <br>Created by Soybeany on 2019/4/29.
 */
public class MvpPlugin implements IExtendPlugin {
    private Lifecycle mLifecycle;
    private IVMProvider mVMProvider;
    private ITemplate mTemplate;

    public MvpPlugin(LifecycleOwner owner, IVMProvider vMProvider, ITemplate template) {
        mLifecycle = owner.getLifecycle();
        mVMProvider = vMProvider;
        mTemplate = template;
    }

    @Override
    public void initAfterSetContentView() {
        mTemplate.onInitPresenters(new PresenterProviderImpl());
    }

    private class PresenterProviderImpl implements IPresenterProvider {
        @Override
        public <V extends IPresenterView, T extends BasePresenter<V>> T getPresenter(Class<T> clazz, V v) {
            T presenter = mVMProvider.getViewModel(clazz);
            presenter.bindView(mLifecycle, v);
            return presenter;
        }
    }

    public interface ITemplate {
        /**
         * 初始化Presenter
         */
        default void onInitPresenters(IPresenterProvider provider) {
        }
    }
}
