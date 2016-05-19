package com.miMailList.listener;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ListView;
import com.miMailList.adapter.ListViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/18.
 */
public class QueryTextWatcher implements TextWatcher{

    private ListViewAdapter adapter;//传入的adapter实例，用于在此调控内容显示
    private List<String> data;//传入的全部显示的数据
    private ListView list;//contentList的引用
    private List<String> dealData = null;//如有查询操作，则保存对应query后的数据

    public QueryTextWatcher(ListViewAdapter adapter, List<String> showData, ListView contentList) {
        this.adapter = adapter;
        this.data = showData;
        this.list = contentList;
        dealData = new ArrayList<String>();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        dealData.clear();//清空上次的query查询处理后的数据，用于加载新一次数据
        if(data!=null&&!"".equals(s.toString())){//query有内容，显示根据query处理后的数据
            for (int i = 0; i < data.size(); i++) {//编译显示内容，将符合的数据收集到dealData
                String str = data.get(i);
                //判断s是否为str的子字符串，如果是，则是需要收集的数据，s为query当前输入的内容
                boolean find = str.contains(s.toString());
                if(find){
                    dealData.add(str);
                }
            }
            //更新listView的数据源
            adapter.setData(dealData,s.toString());
            //刷新listView
            list.invalidateViews();
        }else{//数据集合为空，或者不存在查询的query请求
            adapter.setData(data,s.toString());
            list.invalidateViews();//刷新listView为初始状态
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
    /**
     * 对外提供更改全部数据的接口
     * @param data
     */
    public void setData(List<String> data){
        this.data=data;
    }
}
