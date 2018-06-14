package com.cheng.app;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cheng.app.refresh.PullToRefreshBase;
import com.cheng.app.refresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 下拉加载更多activity
 */
public class PullLoadMoreActivity extends AppCompatActivity {
    private PullToRefreshListView pullToRefreshListView;
    private ListView mListView;


    private int REQUEST_NUM = 10;
    private String marktime = "";
    private boolean hasMoreData = false;
    private boolean isLoadMore = false;

    private List<News> mDataList = new ArrayList<>();
    private MyAdapter adapter;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_refresh);
        //设置标题栏
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle("PullLoadMore");
        //添加导航栏返回按钮
        actionBar.setDisplayHomeAsUpEnabled(true);

        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.refresh_listView);
        pullToRefreshListView.setPullLoadEnabled(false);
        pullToRefreshListView.setScrollLoadEnabled(false);//设置是否能上拉
        pullToRefreshListView.setPullRefreshEnabled(true);//设置是否能下拉
        mListView = pullToRefreshListView.getRefreshableView();

        adapter = new MyAdapter();
        mListView.setAdapter(adapter);

        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isLoadMore = true;
                marktime = "123";
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RequestRefresh("onPullDownToRefresh userid", marktime, REQUEST_NUM);
                    }
                }, 1000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isLoadMore = true;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RequestMoreData("onPullUpToRefresh userid", marktime, REQUEST_NUM);
                    }
                }, 1000);

            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RequestRefresh("onPullDownToRefresh userid", marktime, REQUEST_NUM);
            }
        }, 500);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //下拉刷新数据
    private void RequestRefresh(String params, String marktime, int length) {
        NewsResponse response = requestData(params, marktime);
        onResponse(response);
    }

    //上拉加载更多数据
    private void RequestMoreData(String params, String marktime, int length) {
        NewsResponse response = requestData(params, marktime);
        onResponse(response);
    }

    private NewsResponse requestData(String params, String marktime) {
        NewsResponse response = new NewsResponse();
        ArrayList<News> list = new ArrayList<>();
        if (TextUtils.isEmpty(marktime)) {
            for (int i = 20; i > 10; i--) {
                News news = new News();
                news.icon = params;
                news.title = "title：" + i;
                news.content = "content：" + i;
                list.add(news);
            }
        } else {
            for (int i = 10; i > 0; i--) {
                News news = new News();
                news.icon = params;
                news.title = "title：" + i;
                news.content = "content：" + i;
                list.add(news);
            }
        }
        Collections.reverse(list);//倒序排列
        response.marktime = "" + new Random().nextLong();
        response.newsList = list;
        return response;
    }

    //模拟http请求放回的方法
    public void onResponse(Object object) {
        if (object instanceof NewsResponse) {
            NewsResponse mResponse = (NewsResponse) object;
            marktime = mResponse.marktime;//获取标记时间，标记时间为空则刷新数据，不为空则加载更多
            if (!isLoadMore) {
                mDataList.clear();
            }
            ArrayList<News> list = mResponse.newsList;
            if (mDataList != null && mDataList.size() > 0) {
                for (int i = 0; i < mDataList.size(); i++) {
                    list.add(mDataList.get(i));
                }
            }
            if (list.size() == REQUEST_NUM) {
                hasMoreData = true;
            } else {
                hasMoreData = false;
            }
            mDataList = list;
            adapter.updataList(mDataList);
            pullToRefreshListView.onPullDownRefreshComplete();
            pullToRefreshListView.onPullUpRefreshComplete();
            pullToRefreshListView.setHasMoreData(hasMoreData);//设置是否还能上拉加载更多
            pullToRefreshListView.setPullRefreshEnabled(hasMoreData);//设置是否还能下拉刷新
        }
    }


    private class MyAdapter extends BaseAdapter {
        List<News> newsList = new ArrayList<>();

        void updataList(List<News> list) {
            newsList = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return newsList.size();
        }

        @Override
        public Object getItem(int i) {
            return newsList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup parent) {
            ViewHolder holder = null;
            if (view == null) {
                holder = new ViewHolder();
                view = LayoutInflater.from(PullLoadMoreActivity.this).inflate(
                        R.layout.item_news, parent, false);
                holder.titleTv = view.findViewById(R.id.title_tv);
                holder.contentTv = view.findViewById(R.id.content_tv);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            if (!newsList.isEmpty()) {
                News bean = newsList.get(i);
                holder.titleTv.setText(bean.title);
                holder.contentTv.setText(bean.content);
            }
            return view;
        }
    }

    private class ViewHolder {
        TextView titleTv, contentTv;
    }

    private class News {
        public String title;
        public String content;
        public String icon;

    }

    private class NewsResponse {
        public String marktime;
        public ArrayList<News> newsList;
    }
}
