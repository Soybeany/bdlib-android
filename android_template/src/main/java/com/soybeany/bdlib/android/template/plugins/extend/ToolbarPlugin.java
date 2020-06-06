package com.soybeany.bdlib.android.template.plugins.extend;

import android.app.Activity;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.soybeany.bdlib.android.template.interfaces.IExtendPlugin.invokeOnNotNull;

/**
 * 只在{@link Activity}中使用，需自行调用{@link #onOptionsItemSelected(MenuItem, ISuperOnOptionsItemSelected)}
 * <br><br>官方添加Item的方法：
 * <br>重写{@link Activity#onCreateOptionsMenu(Menu)}方法，然后调用{@link Menu#add(int, int, int, int)}进行item项的添加，
 * 最后在{@link Activity#onOptionsItemSelected(MenuItem)}中进行监听
 * <br><br>自定义添加Item的方法:
 * <br>在{@link ICallback#onInflateNewToolbar(LayoutInflater, ViewGroup)}中返回一个已设置好Item的Toolbar
 * <br>Created by Soybeany on 2019/4/29.
 */
public class ToolbarPlugin implements IExtendPlugin {
    private final AppCompatActivity mActivity;
    @Nullable
    private final ICallback mCallback;

    public ToolbarPlugin(@NonNull AppCompatActivity activity, @Nullable ICallback callback) {
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

    protected void onSetupToolbar(View rootV) {
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
        mActivity.setSupportActionBar(toolbar);
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
