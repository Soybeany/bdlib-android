package com.soybeany.bdlib.android.template.plugins.extend;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.soybeany.bdlib.android.template.interfaces.IExtendPlugin;
import com.soybeany.bdlib.android.util.ViewTransferUtils;
import com.soybeany.bdlib.android.util.system.KeyboardUtils;
import com.soybeany.bdlib.core.java8.Optional;

import static com.soybeany.bdlib.android.template.interfaces.IExtendPlugin.invokeOnNotNull;

/**
 * 只在{@link Activity}中使用，需自行调用{@link #onOptionsItemSelected(MenuItem, ISuperOnOptionsItemSelected)}
 * <br>重写{@link Activity#onCreateOptionsMenu(Menu)}方法，然后调用{@link Menu#add(int, int, int, int)}进行item项的添加
 * <br>Created by Soybeany on 2019/4/29.
 */
public class ToolbarPlugin implements IExtendPlugin {
    private final Activity mActivity;
    @Nullable
    private final ICallback mCallback;

    public ToolbarPlugin(@NonNull Activity activity, @Nullable ICallback callback) {
        mActivity = activity;
        mCallback = callback;
    }

    @Override
    public void initAfterSetContentView() {
        Optional.ofNullable((ViewGroup) mActivity.getWindow().getDecorView().findViewById(android.R.id.content))
                .filter(v -> 0 != v.getChildCount())
                .map(v -> v.getChildAt(0))
                .ifPresent(this::onSetupToolbar);
    }

    @NonNull
    @Override
    public final String getGroupId() {
        return "Toolbar";
    }

    public boolean onOptionsItemSelected(MenuItem item, ISuperOnOptionsItemSelected callback) {
        if (android.R.id.home == item.getItemId()) {
            KeyboardUtils.closeKeyboard(mActivity);
            invokeOnNotNull(mCallback, ICallback::onBackItemPressed);
        }
        return callback.onInvoke(item);
    }

    public void onSetupToolbar(View rootV) {
        if (null == mCallback) {
            return;
        }

        LinearLayout newRootV = new LinearLayout(mActivity);
        newRootV.setOrientation(LinearLayout.VERTICAL);

        Toolbar toolbar = mCallback.onInflateNewToolbar(mActivity.getLayoutInflater(), newRootV);
        if (null == toolbar) {
            return;
        }

        newRootV.addView(toolbar);
        ViewTransferUtils.transfer(rootV, newRootV, 1);

        mCallback.onInitToolbar(toolbar);
    }

    public interface ISuperOnOptionsItemSelected {
        boolean onInvoke(MenuItem item);
    }

    public interface ICallback {
        void onBackItemPressed();

        @Nullable
        default Toolbar onInflateNewToolbar(LayoutInflater inflater, ViewGroup parent) {
            return null;
        }

        default void onInitToolbar(@NonNull Toolbar toolbar) {
        }
    }
}
