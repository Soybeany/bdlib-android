package com.soybeany.bdlib.android.web;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;

import com.soybeany.bdlib.android.util.BDContext;
import com.soybeany.bdlib.android.util.IObserver;
import com.soybeany.bdlib.android.util.dialog.AbstractDialog;
import com.soybeany.bdlib.android.util.dialog.DialogMsg;
import com.soybeany.bdlib.android.util.dialog.IMsgCenterKeyProvider;
import com.soybeany.bdlib.core.util.storage.KeyValueStorage;

/**
 * <br>Created by Soybeany on 2019/4/14.
 */
class DialogProvider implements IObserver {
    private KeyValueStorage<String, AbstractDialog> mStorage = new KeyValueStorage<>();
    private final String mKey = BDContext.getUID();

    public DialogProvider(LifecycleOwner owner, AbstractDialog dialog) {
        owner.getLifecycle().addObserver(this);
        mStorage.put(mKey, dialog);
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        owner.getLifecycle().removeObserver(this);
        mStorage.remove(mKey);
    }

    public void showMsg(DialogMsg msg) {
        mStorage.invoke(mKey, dialog -> dialog.showMsg(msg));
    }

    public void popMsg(DialogMsg msg) {
        mStorage.invoke(mKey, dialog -> dialog.popMsg(msg));
    }

    public IMsgCenterKeyProvider getKeyProvider() {
        AbstractDialog dialog = mStorage.get(mKey);
        return null != dialog ? dialog.getKeyProvider() : null;
    }
}
