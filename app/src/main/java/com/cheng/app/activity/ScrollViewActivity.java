package com.cheng.app.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cheng.app.R;
import com.cheng.refresh.library.PullToRefreshBase;
import com.cheng.refresh.library.PullToRefreshGridView;
import com.cheng.refresh.library.PullToRefreshScrollView;

/**
 * @Author: chengzj
 * @CreateDate: 2018/11/24 14:17
 * @Version: 3.0.0
 */
public class ScrollViewActivity extends AppCompatActivity implements PullToRefreshBase.OnRefreshListener<ScrollView> {
    private PullToRefreshScrollView pullToRefreshScrollView;
    private ScrollView mScrollView;

    View childView;

    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollview);
        pullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.refresh_scrollView);
//        pullToRefreshScrollView.setPullLoadEnabled(true);
//        pullToRefreshScrollView.setScrollLoadEnabled(true);
//        pullToRefreshScrollView.setPullRefreshEnabled(true);
        pullToRefreshScrollView.setOnRefreshListener(this);
        mScrollView = pullToRefreshScrollView.getRefreshableView();
        childView = LayoutInflater.from(this).inflate(R.layout.scroll_child_view,null);
        textView = childView.findViewById(R.id.t1);
        mScrollView.addView(childView);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        textView.setText("ScrollViewActivity");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                textView.setText("t1");
//                t2.setText("t2");
//                t3.setText("t3");
                pullToRefreshScrollView.onPullDownRefreshComplete();
            }
        }, 1600);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

    }
}
