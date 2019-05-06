package com.soybeany.bdlib.android.template.plugins.extend;

import com.soybeany.bdlib.android.util.dialog.DialogKeyProvider;

/**
 * todo 使用liveData记录所需的弹窗信息
 * <br>Created by Soybeany on 2019/4/30.
 */
public class DialogPlugin {


    public interface ITemplate {
        DialogKeyProvider getDialogKeys();
    }
}