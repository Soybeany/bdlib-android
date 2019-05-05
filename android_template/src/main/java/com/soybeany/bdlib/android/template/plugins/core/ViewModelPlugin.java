package com.soybeany.bdlib.android.template.plugins.core;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;
import com.soybeany.bdlib.android.template.interfaces.IVMProvider;

/**
 * ViewModel
 * <br>Created by Soybeany on 2019/4/30.
 */
public class ViewModelPlugin implements IExtendPlugin {
    private ITemplate mTemplate;

    public ViewModelPlugin(ITemplate template) {
        mTemplate = template;
    }

    @Override
    public void initBeforeSetContentView() {
        mTemplate.onInitViewModels(mTemplate);
    }

    public interface ITemplate extends IVMProvider {

        /**
         * 初始化ViewModel
         */
        default void onInitViewModels(IVMProvider provider) {
        }

        @Override
        default <T extends ViewModel> T getViewModel(Class<T> modelClass) {
            if (this instanceof FragmentActivity) {
                return ViewModelProviders.of((FragmentActivity) this).get(modelClass);
            } else if (this instanceof Fragment) {
                return ViewModelProviders.of((Fragment) this).get(modelClass);
            }
            throw new RuntimeException("请在FragmentActivity或Fragment中使用");
        }
    }
}
