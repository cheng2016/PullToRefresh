package com.cheng.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.cheng.app.refresh.PullToRefreshBase;
import com.cheng.app.refresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PullToRefreshListView pullToRefreshListView;
    private ListView mListView;


    private int REQUEST_NUM = 10;
    private String marktime = "";
    private boolean hasMoreData = false;
    private boolean isLoadMore = false;

    private List<News> mDataList = new ArrayList<>();
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.refresh_listView);
        pullToRefreshListView.setPullLoadEnabled(false);
        pullToRefreshListView.setScrollLoadEnabled(true);
        pullToRefreshListView.setPullRefreshEnabled(true);
        mListView = pullToRefreshListView.getRefreshableView();

        adapter = new MyAdapter();
        mListView.setAdapter(adapter);

        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isLoadMore = false;
                RequestRefresh("userid","",REQUEST_NUM);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isLoadMore = true;
                RequestMoreData("userid","",REQUEST_NUM);
            }
        });
    }

    //下拉刷新数据
    void RequestRefresh(String params,String marktime,int length){

    }

    //上拉加载更多数据
    void RequestMoreData(String params,String marktime,int length){

    }

    //模拟http请求放回的方法
    public void onResponse(Object object){
        if(object instanceof NewsResponse){
            NewsResponse mResponse = (NewsResponse) object;
            marktime = mResponse.marktime;//获取标记时间，标记时间为空则刷新数据，不为空则加载更多
            if (!isLoadMore) {
                mDataList.clear();

                ArrayList<News> list = mResponse.newsList;
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        mDataList.add(list.get(i));
                    }
                }
                if(mDataList.size() == REQUEST_NUM)
                {
                    hasMoreData = true;
                }else{
                    hasMoreData = false;
                }

                adapter.updataList(mDataList);
                pullToRefreshListView.onPullDownRefreshComplete();
                pullToRefreshListView.onPullUpRefreshComplete();
                pullToRefreshListView.setHasMoreData(hasMoreData);
            }
        }
    }


    class MyAdapter extends BaseAdapter {
        List<News> newsList = new ArrayList<>();

        void updataList(List<News> list){
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
        public View getView(int i, View view, ViewGroup viewGroup) {
            return view;
        }
    }

    class News {
        public String title;
        public String content;
        public String icon;

    }

    class NewsResponse {
        public String marktime;
        public ArrayList<News> newsList;
    }

}
