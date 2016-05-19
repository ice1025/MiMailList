package com.miMailList.activity;

import android.app.Activity;
import android.os.Bundle;
import com.miMailList.view.SortListView;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SortListView view = new SortListView(this);
        List<String> data = new ArrayList<String>();
        data.add("北京市");
        data.add("天津市");
        data.add("河北省");
        data.add("山西省");
        data.add("内蒙古自治区");
        data.add("辽宁省");
        data.add("吉林省");
        data.add("黑龙江省");
        data.add("上海市");
        data.add("江苏省");
        data.add("浙江省");
        data.add("安徽省");
        data.add("福建省");
        data.add("江西省");
        data.add("山东省");
        data.add("河南省");
        data.add("湖北省");
        data.add("湖南省");
        data.add("广东省");
        data.add("广西壮族自治区");
        data.add("海南省");
        data.add("重庆市");
        data.add("四川省");
        data.add("贵州省");
        data.add("云南省");
        data.add("西藏自治区");
        data.add("陕西省");
        data.add("甘肃省");
        data.add("青海省");
        data.add("宁夏回族自治区");
        data.add("新疆维吾尔自治区");
        data.add("香港特别行政区");
        data.add("澳门特别行政区");
        data.add("台湾省");
        view.setAdapterData(data);
        setContentView(view);
    }
}
