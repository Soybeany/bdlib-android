package com.soybeany.bdlib.android.ui.bnv;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * 固定数目的BNV辅助器
 * <br>Created by Soybeany on 2019/4/22.
 */
public class FixedBNVHelper {
    private final List<Integer> mIdList = new ArrayList<>(); // 下标转id
    private final SparseIntArray mIndexArr = new SparseIntArray(); // id转下标
    private final SparseArray<QBadgeView> badgeViews = new SparseArray<>(); // 角标视图

    private BottomNavigationView mBNV;
    private BottomNavigationView.OnNavigationItemSelectedListener mListener;

    public FixedBNVHelper(@NonNull BottomNavigationView view) {
        // 设值
        mBNV = view;
        // 存储Id
        storeIds();
    }

    public void bindViewPager(@NonNull ViewPager vp) {
        vp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mBNV.setSelectedItemId(mIdList.get(position));
            }
        });
        mBNV.setOnNavigationItemSelectedListener(item -> {
            vp.setCurrentItem(mIndexArr.get(item.getItemId()), false);
            return null == mListener || mListener.onNavigationItemSelected(item);
        });
    }

    public void setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener listener) {
        mListener = listener;
    }

    public Badge getBadge(int id) {
        return badgeViews.get(id);
    }

    private void storeIds() {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) mBNV.getChildAt(0);
        int count = menuView.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = menuView.getChildAt(i);
            int id = view.getId();
            mIdList.add(id);
            mIndexArr.put(id, i);
            badgeViews.put(id, getNewBadgeV(view));
        }
    }

    private QBadgeView getNewBadgeV(View target) {
        QBadgeView v = new QBadgeView(target.getContext());
        v.bindTarget(target).setBadgeGravity(Gravity.END | Gravity.TOP).setBadgeTextSize(8, true);
        v.post(() -> v.setGravityOffset((int) (v.getMeasuredWidth() * 0.3), (int) (v.getMeasuredHeight() * 0.1), false));
        return v;
    }
}
