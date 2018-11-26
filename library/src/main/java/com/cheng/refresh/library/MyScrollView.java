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

    /**
     * onInterceptTouchEvent()是用于处理事件（类似于预处理，当然也可以不处理）并改变事件的传递方向，
     * 也就是决定是否允许Touch事件继续向下（子控件）传递，一但返回True（代表事件在当前的viewGroup中会被处理）
     * ，则向下传递之路被截断（所有子控件将没有机会参与Touch事件），同时把事件传递给当前的控件的onTouchEvent()处理；
     * 返回false，则把事件交给子控件的onInterceptTouchEvent()
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
//        return true;
    }

    /**
     * onTouchEvent()用于处理事件，返回值决定当前控件是否消费（consume）了这个事件，
     * 也就是说在当前控件在处理完Touch事件后，是否还允许Touch事件继续向上（父控件）传递，
     * 一但返回True，则父控件不用操心自己来处理Touch事件。返回true，
     * 则向上传递给父控件（注：可能你会觉得是否消费了有关系吗，反正我已经针对事件编写了处理代码？
     * 答案是有区别！比如ACTION_MOVE或者ACTION_UP发生的前提是一定曾经发生了ACTION_DOWN，如果你没有消费ACTION_DOWN，
     * 那么系统会认为ACTION_DOWN没有发生过，所以ACTION_MOVE或者ACTION_UP就不能被捕获。
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
        //        return true;
    }
}
