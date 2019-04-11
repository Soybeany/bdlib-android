package com.soybeany.bdlib.android.mvp;

import com.soybeany.bdlib.android.util.BDContext;

/**
 * <br>Created by Soybeany on 2019/4/11.
 */
public interface IView {

    /**
     * 用于在{@link BasePresenter}中区分当前视图
     */
    default String getUid() {
        return BDContext.getUID();
    }

}
