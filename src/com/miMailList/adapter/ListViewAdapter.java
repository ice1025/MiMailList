package com.miMailList.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.miMailList.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/12.
 */
public class ListViewAdapter extends BaseAdapter{

    private Context context;
    private List<String> data = null;//listView所需要显示的数据集合
    private char[] query = null;//需要匹配查询的输入字符内容
    private List<Character> firstWord = null;//用户保存对应的View实例的首字母内容，提供外部查询

    private LayoutInflater inflater;



    /**---------------------------原生方法区----------------------**/

    public ListViewAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        firstWord = new ArrayList<Character>();
    }

    public ListViewAdapter(Context context, List<String> data, String query){
        this.context=context;
        inflater = LayoutInflater.from(context);
        firstWord = new ArrayList<Character>();
        //设置传入变量
        setData(data,query);
    }

    /**----------------------------------------------------------------**/
                             /**对外方法调用区**/
    /**
     * 根据输入的索引寻找对应的data，并将其首字母返回，如果不存在则返回‘&’
     * @param position  view的索引值
     * @return 首字母
     */
    public char getFirstWord(int position){
        if(firstWord!=null){
            return firstWord.get(position);
        }
        return '&';
    }



    /**
     * 提供外面数据输入接口，用于更新Adapter的数据内容
     * 接收null输入，需要注意规避null风险
     * @param data 需要显示的全部数据内容集合
     * @param query 需要根据query配置的字符串
     */
    public void setData(List<String> data, String query) {
        this.data = data;
        if(query!=null){
            this.query = query.toCharArray();
        }
    }
    /**----------------------------------------------------------------**/
    @Override
    public int getCount() {//注意规避data为null的风险
        return data==null ? 0 :data.size();
    }

    @Override
    public Object getItem(int position) {
           return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        List<TextView> cache = null;
        //进行convertView的复用，如果第一次使用，则实例化该View
        if(convertView==null){
            convertView = inflater.inflate(R.layout.mark_list_item,null,false);
            //申请一个缓冲区cache用于保存其子TextView的引用
            cache = new ArrayList<TextView>();

            TextView tv = null;
            if(query==null || "".equals(query)){//如果query为null，或者为空字符串，则直接下载
                tv = new TextView(context);
                tv.setText(data.get(position));//将整个字符串设置到TextView里面，而不单字加载
                setTextParmas(tv);
                ((LinearLayout)convertView).addView(tv);//加载到convertView节点下
                cache.add(tv);//缓存集合中保存tv的引用，便于下次复用该TextView
            }else {//query不为null，其有query内容，我们需要将该查询内容对应的字符高亮显示
                  //将显示内容的字符串拆分为字符数组，用于单字加载
                char[] words = data.get(position).toCharArray();
                for (int i = 0; i < words.length; i++) {
                    tv=new TextView(context);
                    tv.setText(words[i]+"");//单字加载，即一个字用一个TextView显示
                    setTextParmas(tv);
                    //查询是否在query的内容之中，如果为是则将该TextView高亮显示
                    for (char c : query) {
                        if(c==words[i]){
                            tv.setTextColor(Color.RED);
                        }
                    }
                    ((LinearLayout)(convertView)).addView(tv);
                    cache.add(tv);
                }
            }
        }else{//如果不是第一次使用，则该convertView可以被复用
            cache = (List<TextView>) convertView.getTag();//获取缓存cache集合
            TextView tv;
            int usingSize = 0;//记录当前conventView所使用的TextView个数
            int length = cache.size();
            if(query==null||"".equals(query)){//如果query为null或者空字符串，则直接加载
                if (length>0){//检测cache是否存在可复用的view
                    tv= cache.get(0);//取首个textView复用
                    tv.setTextColor(Color.GRAY);
                }else{//如果没有可复用的textView,实例化一个TextView并加入缓存中
                    tv=new TextView(context);
                    setTextParmas(tv);
                    ((LinearLayout)convertView).addView(tv);
                    cache.add(tv);
                }
                tv.setText(data.get(position));
                usingSize++;
            }else{//query不为null，其有query内容，我们需要将该查询内容对应的字符高亮显示
                //将显示内容的字符串拆分为字符数组，用于单字加载
                char[] words = data.get(position).toCharArray();
                for (int i = 0; i < words.length; i++) {
                    if(i<length){//获取第i个缓存的textView
                        tv=cache.get(i);
                        tv.setTextColor(Color.GRAY);
                    }else{//如果已经没有缓存textView可获取了
                        tv=new TextView(context);
                        setTextParmas(tv);
                        ((LinearLayout)convertView).addView(tv);
                        cache.add(tv);
                    }
                    //查询是否在query的内容中，如果为是则将该textView高亮显示
                    for (char c : query) {
                        if(c==words[i]){
                            tv.setTextColor(Color.RED);
                        }
                    }
                    tv.setText(words[i]+"");//单字加载，即一个字用一个textView显示
                    usingSize++;
                }
            }
            clearCache(convertView,cache,usingSize);
        }
        convertView.setTag(cache);//保存缓存cache集合
        firstWord.add(position,cache.get(0).getText().toString().charAt(0));
        return convertView;
    }

    /**
     * 清理多余的缓存数据，减少系统资源占用，移除不需要显示的视图
     * @param convertView
     * @param cache
     * @param usingSize
     */
    private void clearCache(View convertView, List<TextView> cache, int usingSize) {
        int allSize = cache.size();//记录cache的元素总数
        int nowSize = allSize;//记录当前cache的元素数
        if(usingSize<allSize){//如果有空闲的缓存没被使用
            while (nowSize>usingSize){//从最后一个开始清理
                ((LinearLayout)convertView).removeView(cache.get(nowSize-1));
                cache.remove(nowSize-1);
                nowSize--;
            }
        }
    }

    /**
     * 设置TextView的默认显示格式
     * @param tv 需要设置的TextView
     */
    private void setTextParmas(TextView tv) {
        tv.setTextSize(20);
        tv.setHeight(88);
        tv.setTextColor(Color.GRAY);
        tv.setGravity(Gravity.CENTER_VERTICAL);
    }
}
