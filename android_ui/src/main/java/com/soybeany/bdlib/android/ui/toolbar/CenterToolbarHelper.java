package com.soybeany.bdlib.android.ui.toolbar;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.soybeany.bdlib.android.ui.R;
import com.soybeany.bdlib.android.util.BDContext;

/**
 * <br>Created by Soybeany on 2019/4/26.
 */
public class CenterToolbarHelper extends ActionBarHelper {
    private TextView mTitleView;
    private TextView mSubtitleView;

    private Resources mResources = BDContext.getResources();

    public CenterToolbarHelper(AppCompatActivity activity, @NonNull Toolbar toolbar) {
        super(activity);
        activity.getLayoutInflater().inflate(R.layout.bd_toolbar_center_title_view, toolbar);
        mTitleView = toolbar.findViewById(R.id.toolbar_title);
        mSubtitleView = toolbar.findViewById(R.id.toolbar_subtitle);
    }

    @Override
    protected void onSetupActionBar(@NonNull ActionBar actionBar) {
        super.onSetupActionBar(actionBar);
        // 禁用默认标题
        actionBar.setDisplayShowTitleEnabled(false);
    }

    public void setTitle(@StringRes int res) {
        setTitle(mResources.getString(res));
    }

    public void setTitle(String title) {
        if (null != title) {
            mTitleView.setText(title);
            mTitleView.setVisibility(View.VISIBLE);

            // 设置标题显示的行数
            if (View.GONE == mSubtitleView.getVisibility()) {
                mTitleView.setMaxLines(2);
            } else {
                mTitleView.setSingleLine();
            }
        } else {
            mTitleView.setVisibility(View.GONE);
        }
    }

    public void setSubtitleView(@StringRes int res) {
        setSubtitle(mResources.getString(res));
    }

    public void setSubtitle(String subtitle) {
        if (null != subtitle) {
            mSubtitleView.setText(subtitle);
            mSubtitleView.setVisibility(View.VISIBLE);
        } else {
            mSubtitleView.setVisibility(View.GONE);
        }
    }

    public void setupTitleV(IViewSetter setter) {
        setter.onSetup(mTitleView);
    }

    public void setupSubtitleV(IViewSetter setter) {
        setter.onSetup(mSubtitleView);
    }

    public interface IViewSetter {
        void onSetup(TextView v);
    }
}
