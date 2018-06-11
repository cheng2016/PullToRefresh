# PullToRefresh
下拉刷新，上拉加载更多的库。



## Code Review

```
pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.refresh_listView);
pullToRefreshListView.setPullLoadEnabled(false);
pullToRefreshListView.setScrollLoadEnabled(true);//设置可滑动加载更多
pullToRefreshListView.setPullRefreshEnabled(true);//设置可下拉刷新
mListView = pullToRefreshListView.getRefreshableView();
adapter = new MyAdapter();
mListView.setAdapter(adapter);
pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        isLoadMore = false;
        marktime = "";
        RequestRefresh("onPullDownToRefresh userid", marktime, REQUEST_NUM);

    }
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        isLoadMore = true;
        RequestMoreData("onPullUpToRefresh userid", marktime, REQUEST_NUM);
    }
});
```





## 效果图

   ![](screenshot/screenshot_1528566778.png)	   ![](screenshot/screenshot_1528566767.png)

### Contact Me

- Github: github.com/cheng2016
- Email: mitnick.cheng@outlook.com
- QQ: 1102743539


# License

    Copyright 2016 cheng2016,Inc.
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
