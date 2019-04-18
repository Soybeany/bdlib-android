package com.soybeany.bdlib.project;

import android.content.Intent;
import android.view.View;

import com.soybeany.bdlib.android.template.BaseActivity;


public class MainActivity extends BaseActivity {

    @Override
    public int setupLayoutResId() {
        return R.layout.activity_main;
    }

    public void onClick(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }
}
