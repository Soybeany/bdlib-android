package com.soybeany.bdlib.project;

import com.soybeany.bdlib.android.mvp.IPresenterView;

/**
 * <br>Created by Soybeany on 2019/4/16.
 */
public interface ITestView extends IPresenterView {
    void showMsg(String desc, String msg);
}
