package com.cheng.app.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cheng.app.News;
import com.cheng.app.NewsResponse;
import com.cheng.app.R;
import com.cheng.refresh.library.PullToRefreshBase;
import com.cheng.refresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ezy.ui.layout.LoadingLayout;

/**
 * 下拉刷新，上拉加载更多activity
 */
public class ListViewActivity extends AppCompatActivity implements PullToRefreshBase.OnRefreshListener<ListView> {
    public static final String TAG = "ListViewActivity";
    private PullToRefreshListView pullToRefreshListView;
    private ListView mListView;

    private int REQUEST_NUM = 10;
    private String marktime = "";
    private boolean hasMoreData = false;
    private boolean isLoadMore = false;

    private List<News> mDataList = new ArrayList<>();
    private MyAdapter adapter;

    private Handler handler = new Handler();

    LoadingLayout mLoadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        mLoadingLayout = LoadingLayout.wrap(this);

        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.refresh_listView);
        pullToRefreshListView.setPullLoadEnabled(false);
        pullToRefreshListView.setScrollLoadEnabled(true);
        pullToRefreshListView.setPullRefreshEnabled(true);
        mListView = pullToRefreshListView.getRefreshableView();

        adapter = new MyAdapter();
        mListView.setAdapter(adapter);

        pullToRefreshListView.setOnRefreshListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG,"onclick");
            }
        });

        mLoadingLayout.showLoading();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoadingLayout.showError();
            }
        }, 2000);
        mLoadingLayout.setRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestRefresh("onPullDownToRefresh userid", marktime, REQUEST_NUM);
            }
        });
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
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
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
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
            for (int i = 0; i < 10; i++) {
                News news = new News();
                news.icon = params;
                news.title = "title：" + i;
                news.content = "content：" + i;
                list.add(news);
            }
        } else {
            for (int i = 10; i < 19; i++) {
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
            pullToRefreshListView.onPullDownRefreshComplete();
            pullToRefreshListView.onPullUpRefreshComplete();
            pullToRefreshListView.setHasMoreData(hasMoreData);
            mLoadingLayout.showContent();
        }
    }

    class MyAdapter extends BaseAdapter {
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
                view = LayoutInflater.from(ListViewActivity.this).inflate(
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

    public class ViewHolder {
        TextView titleTv, contentTv;
    }
}
