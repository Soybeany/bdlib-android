package com.soybeany.bdlib.android.template;

import android.arch.lifecycle.ViewModel;

/**
 * <br>Created by Soybeany on 2019/4/18.
 */
public interface IVMProvider {
    <T extends ViewModel> T getViewModel(Class<T> modelClass);
}
