package com.soybeany.bdlib.android.mvp;

import com.soybeany.bdlib.android.template.interfaces.IBaseFunc;
import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;

/**
 * <br>Created by Soybeany on 2019/4/29.
 */
public class MvpPlugin<Ex extends IBaseFunc.IEx & MvpPlugin.ITemplate> implements IExtendPlugin {
    private Ex mEx;

    public MvpPlugin(Ex ex) {
        mEx = ex;
    }

    @Override
    public void signalAfterSetContentView() {
        mEx.onInitPresenters(new PresenterProviderImpl(mEx));
    }

    public interface ITemplate {
        /**
         * 初始化Presenter
         */
        default void onInitPresenters(IPresenterProvider provider) {
        }
    }
}
