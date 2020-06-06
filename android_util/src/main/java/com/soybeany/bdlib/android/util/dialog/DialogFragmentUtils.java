package com.soybeany.bdlib.android.util.dialog;

import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.java8.function.Supplier;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

/**
 * <br>Created by Soybeany on 2020/4/3.
 */
public class DialogFragmentUtils {

    /**
     * 获得指定uid的实例，创建或者重用
     *
     * @param uid DialogFragment的唯一标识，用于系统重建时查找
     */
    @SuppressWarnings("unchecked")
    public static <T extends BaseDialogFragment> IRealDialog getInstance(@NonNull FragmentActivity activity, @NonNull String uid, @NonNull Supplier<? extends T> supplier) {
        T fragment = Optional.ofNullable((T) activity.getSupportFragmentManager().findFragmentByTag(uid)).orElseGet(supplier);
        fragment.mActivity = activity;
        fragment.mUid = uid;
        return fragment;
    }

}
