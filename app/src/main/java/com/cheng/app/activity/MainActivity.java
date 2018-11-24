package com.cheng.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cheng.app.R;

/**
 * @Author: chengzj
 * @CreateDate: 2018/11/24 14:04
 * @Version: 3.0.0
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.list_view_refresh).setOnClickListener(this);
        findViewById(R.id.list_view_pull_down_refresh).setOnClickListener(this);
        findViewById(R.id.grid_view_refresh).setOnClickListener(this);
        findViewById(R.id.scroll_view_refresh).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.list_view_refresh:
                intent.setClass(MainActivity.this, ListViewActivity.class);
                startActivity(intent);
                break;
            case R.id.list_view_pull_down_refresh:
                intent.setClass(MainActivity.this, PullDownRefreshActivity.class);
                startActivity(intent);
                break;
            case R.id.grid_view_refresh:
                intent.setClass(MainActivity.this, GridViewActivity.class);
                startActivity(intent);
                break;
            case R.id.scroll_view_refresh:
                intent.setClass(MainActivity.this, ScrollViewActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
