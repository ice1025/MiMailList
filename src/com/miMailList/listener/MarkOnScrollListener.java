package com.miMailList.listener;

import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.TextView;
import com.miMailList.adapter.ListViewAdapter;
import com.miMailList.until.GetPYUnil;

import java.util.Map;

/**
 * Created by Administrator on 2016/5/18.
 */
public class MarkOnScrollListener implements OnScrollListener{
    private Map<String,TextView> tvMaps;//根据key获取TextView实例，key为首字母
    private TextView oldTv;//保存前次高亮的TextView的引用
    private TextView centerTextView;


    public MarkOnScrollListener(Map<String, TextView> tvMaps, TextView centerTextView) {
        this.tvMaps = tvMaps;
        this.centerTextView = centerTextView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //构造一个定时器，用于延时1000毫秒取消显示
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                centerTextView.setVisibility(View.INVISIBLE);
            }
        };
        Handler handler = new Handler();

        switch (scrollState){
            case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                //如果手指还接触界面，则取消定时，主要用于第二次后触发事件使用
                if(centerTextView.getVisibility() == View.VISIBLE)
                    handler.removeCallbacks(runnable);
                break;
            case OnScrollListener.SCROLL_STATE_FLING:
                //如果手指离开屏幕，屏幕滑动，设置centerTextView可见
                centerTextView.setVisibility(View.VISIBLE);
                //如果第二次手指离开屏幕，屏幕滑动，则取消定时，主要用于第二次后触发事件使用
                if(centerTextView.getVisibility() == View.VISIBLE)
                    handler.removeCallbacks(runnable);
                break;
            case OnScrollListener.SCROLL_STATE_IDLE:
                //启动定时任务，于1秒后取消显示
                handler.postDelayed(runnable, 1000);
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        ListViewAdapter adapter = (ListViewAdapter)(view.getAdapter());
        if(adapter != null){
            //在首次加载listview时会调用onScroll(),那时候的adapter为null,容错处理
            char c1 = adapter.getFirstWord(firstVisibleItem + 4);
            char c2 = GetPYUnil.getFirstWord(c1);
            TextView tv = tvMaps.get(c2+"");
            if(tv != null){
                if(oldTv != null){//如果前次已经有TextView高亮显示，取消高亮效果
                    oldTv.setTextColor(Color.GRAY);
                }
                tv.setTextColor(Color.RED);//设置高亮效果
                centerTextView.setText(c1+"");
                oldTv = tv;
            }
        }
    }
}
