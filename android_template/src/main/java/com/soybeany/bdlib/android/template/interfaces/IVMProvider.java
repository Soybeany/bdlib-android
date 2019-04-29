package com.soybeany.bdlib.android.template.interfaces;

import android.arch.lifecycle.ViewModel;

/**
 * ViewModel提供者
 * <br>Created by Soybeany on 2019/4/18.
 */
public interface IVMProvider {
    <T extends ViewModel> T getViewModel(Class<T> modelClass);
}
