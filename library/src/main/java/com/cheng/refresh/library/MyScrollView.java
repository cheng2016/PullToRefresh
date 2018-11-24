package com.cheng.refresh.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * @Author: chengzj
 * @CreateDate: 2018/11/24 15:43
 * @Version: 3.0.0
 *
 * 解决控件中与父控件滑动冲突的问题
 */
public class MyScrollView extends ScrollView {

    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context) {
        super(context);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        super.onInterceptTouchEvent(ev);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        return true;
    }
}
