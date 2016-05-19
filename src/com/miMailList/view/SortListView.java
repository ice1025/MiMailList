package com.miMailList.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.miMailList.R;
import com.miMailList.adapter.ListViewAdapter;
import com.miMailList.listener.MarkOnScrollListener;
import com.miMailList.listener.QueryTextWatcher;
import com.miMailList.until.GetPYUnil;

import java.util.*;

/**
 * Created by Administrator on 2016/5/12.
 */
public class SortListView extends FrameLayout{
    /**----------------------私有变量-------------------------**/
    private Context context= null;
    private LayoutInflater inflater = null;
    private ListViewAdapter adapter = null;

    private String[] firstWords = null;//可控查询的首字母序列数组
    private View rootView = null;//UI的根View,也就是最顶层的界面
    private Map<String,TextView> markTvMap = null;//用于保存markList的子View的索引，用于根据key获取实例
    private List<String> showData = null;//保存外面传入的需要显示的数据，用于在contentList显示
    private QueryTextWatcher textWatcherNew = null;//EditView的监听事件类


    private ListView contentList;//用于显示list内容的部分
    private LinearLayout markList;//首字母序列显示部分
    private EditText queryText;//顶层输入查询编辑框
    private TextView centerTextView;
    /**-------------------------默认构造方法--------------------**/
    //用于XML调用
    public SortListView(Context context, AttributeSet attrs){
        super(context,attrs);
        this.context=context;
        init();
    }
    //用于硬编码调用
    public SortListView(Context context) {
        super(context);
        this.context=context;
        init();
    }

    /**************************内部逻辑代码****************************************/
    /**
     * 初始化操作，进行变量的实例化与调用UI加载函数
     */
    private void init() {
        //从资源文件arrays中读取首字母序列资源
        firstWords = context.getResources().getStringArray(R.array.frist_wrod);
        //获取视图填充器，用于根据xml生成对应的View实例
        inflater = LayoutInflater.from(context);
        //实例化其他变量
        markTvMap=new HashMap<String, TextView>();
        adapter = new ListViewAdapter(context);
        //加载初始UI
        loadView();
        //加载监听事件
        setListener();
    }

    /**
     * 加载初始的UI界面大框架，并不进行加载数据，供构造函数初始化使用
     */
    private void loadView() {
        //根据XML生成View,并将其加载到当前界面(this)的子节点下
        rootView = inflater.inflate(R.layout.overall_list_layout,this,true);
        //获取布局组件实例引用
        contentList = (ListView) rootView.findViewById(R.id.overall_list_layout_contentlist);
        markList = (LinearLayout) rootView.findViewById(R.id.overall_list_layout_marklist);
        queryText = (EditText) rootView.findViewById(R.id.overall_list_layout_querytext);
        centerTextView = (TextView) rootView.findViewById(R.id.overall_list_layout_centertext);
        //对首字母序列显示部分进行处理
        setMarkList();
    }

    /**
     * 根据资源文件生成的firstWords数组，生成对应内容的TextView，将其附加到markList上显示
     * 并存入一个Map集合保持引用索引
     */
    private void setMarkList() {
        TextView tv = null;
        //配置TextView的显示格式，设置为适配宽高，并weight权重为1
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,1.0f);
        for (int i = 0; i < firstWords.length; i++) {
            tv= new TextView(context);
            tv.setBackgroundColor(Color.alpha(0));//设置背景为透明
            tv.setText(firstWords[i]);//设置其显示的首字母
            tv.setLayoutParams(params);
            markList.addView(tv);
            //将其索引保存到Map中，注意key为当前的首字母，
            //并于根据首字母快速获取对应的textView
            markTvMap.put(firstWords[i],tv);
        }
    }

    /**
     * 设置各个组件的监听事件
     */
    private void setListener() {
        //监听editText内容变更事件，查询时根据输入内容将contentList匹配结果显示的监听事件
        textWatcherNew = new QueryTextWatcher(adapter,showData,contentList);
        queryText.addTextChangedListener(textWatcherNew);
        //监听list滚动事件，滚动时实现标记右端首字母与快速滚动出现提示框
        MarkOnScrollListener markOnScrollListener = new MarkOnScrollListener(markTvMap,centerTextView);
        contentList.setOnScrollListener(markOnScrollListener);
    }

    /**----------------------对外公布调用方法----------------------------**/
    /**
     * 提供外部传入显示数据的接口，传入后会将data刷新到contentList,并更新界面
     * @param data 传入的需要显示的数据
     */
    public void setAdapterData(List<String> data){
        showData=data;
        //对list进行排序，排序规则为Comparator所实现的compare()
        //这里是根据首字母升序次序排序
        Collections.sort(showData, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return GetPYUnil.Compare(lhs,rhs);
            }
        });
        adapter.setData(showData,null);//传入显示数据，不设置querry查询内容
        textWatcherNew.setData(showData);//通知事件监听类刷新数据
        contentList.setAdapter(adapter);
    }

}
