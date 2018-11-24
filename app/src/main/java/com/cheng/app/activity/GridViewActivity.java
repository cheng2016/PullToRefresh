package com.cheng.app.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.cheng.app.NewsResponse;
import com.cheng.app.R;
import com.cheng.app.News;
import com.cheng.refresh.library.PullToRefreshBase;
import com.cheng.refresh.library.PullToRefreshGridView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author: chengzj
 * @CreateDate: 2018/11/24 13:53
 * @Version: 3.0.0
 */
public class GridViewActivity extends AppCompatActivity implements PullToRefreshBase.OnRefreshListener<GridView> {
    private PullToRefreshGridView pullToRefreshGridView;
    private GridView mGridView;
    private int REQUEST_NUM = 20;
    private String marktime = "";
    private boolean hasMoreData = false;
    private boolean isLoadMore = false;

    private List<News> mDataList = new ArrayList<>();
    private MyAdapter adapter;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridview);
        pullToRefreshGridView = (PullToRefreshGridView) findViewById(R.id.refresh_listView);
        pullToRefreshGridView.setPullLoadEnabled(false);
        pullToRefreshGridView.setScrollLoadEnabled(true);
        pullToRefreshGridView.setPullRefreshEnabled(true);
        pullToRefreshGridView.setOnRefreshListener(this);
        mGridView = pullToRefreshGridView.getRefreshableView();
        adapter = new MyAdapter();
        mGridView.setAdapter(adapter);
        mGridView.setNumColumns(2);
        int margin = dp2px(this, 10);
        mGridView.setHorizontalSpacing(margin);
        mGridView.setVerticalSpacing(margin);
    }

    /**
     * dp转px
     */
    public  int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
//                refreshView.setLastUpdatedLabel("2018-09-18 15:20");
        isLoadMore = false;
        marktime = "";
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RequestRefresh("onPullDownToRefresh userid", marktime, REQUEST_NUM);
            }
        }, 1200);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
        isLoadMore = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RequestMoreData("onPullUpToRefresh userid", marktime, REQUEST_NUM);
            }
        }, 1200);

    }

    //下拉刷新数据
    void RequestRefresh(String params, String marktime, int length) {
        NewsResponse response = requestData(params, marktime);
        onResponse(response);
    }

    //上拉加载更多数据
    void RequestMoreData(String params, String marktime, int length) {
        NewsResponse response = requestData(params, marktime);
        onResponse(response);
    }

    NewsResponse requestData(String params, String marktime) {
        NewsResponse response = new NewsResponse();
        ArrayList<News> list = new ArrayList<>();
        if (TextUtils.isEmpty(marktime)) {
            for (int i = 0; i < 20; i++) {
                News news = new News();
                news.icon = params;
                news.title = "title：" + i;
                news.content = "content：" + i;
                list.add(news);
            }
        } else {
            for (int i = 20; i < 49; i++) {
                News news = new News();
                news.icon = params;
                news.title = "title：" + i;
                news.content = "content：" + i;
                list.add(news);
            }
        }
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
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    mDataList.add(list.get(i));
                }
            }
            if (list.size() == REQUEST_NUM) {
                hasMoreData = true;
            } else {
                hasMoreData = false;
            }
            adapter.updataList(mDataList);
            pullToRefreshGridView.onPullDownRefreshComplete();
            pullToRefreshGridView.onPullUpRefreshComplete();
            pullToRefreshGridView.setHasMoreData(hasMoreData);
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
                view = LayoutInflater.from(GridViewActivity.this).inflate(
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
}
