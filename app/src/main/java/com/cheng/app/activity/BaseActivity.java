package com.cheng.app.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ezy.ui.layout.LoadingLayout;

/**
 * @Author: chengzj
 * @CreateDate: 2018/11/26 15:38
 * @Version: 3.0.0
 */
public abstract class BaseActivity extends AppCompatActivity {
    LoadingLayout mLoadingLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mLoadingLayout = LoadingLayout.wrap(this);
        initView(savedInstanceState);
        initData();
    }

    protected abstract int getLayoutId();

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void initData();
}
