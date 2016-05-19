package com.miMailList.until;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016/5/12.
 * 提供根据汉字获取首字母的功能
 * 仅支持gb2312简体汉字
 */
public class GetPYUnil {
    //简体中文的编码范围从B0A1(45217)一直到F7FE(63486)
    private static final int BEGIN = 45217;
    private static final int END = 63486;
    //按照声母表示，这个表示在GB2312中出现的第一个汉字，也就是说“啊”是代表首字母的第一个汉字
    //i,u,v都不做声母，自定规则跟随前面的字母
    //最后保持空格
    private static final char[] charTable = new char[]{'啊', '芭', '擦', '搭', '蛾', '发',
            '噶', '哈', '哈', '击', '喀', '垃', '妈',
            '拿', '哦', '啪', '期', '然', '撒', '塌',
            '塌', '塌', '挖', '昔', '压', '匝'};

    //对象首字母区间表
    private static final char[] initialtable = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'H', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'T', 'T', 'W', 'X', 'Y',
            'Z'};

    //二十六个字母区间对应二十七个端点
    //GB2312码汉字区间十进制表示
    private static int[] table = new int[27];

    //工具类初始化代码块，进行初始化操作
    static {
        for (int i = 0; i < 26; i++) {
            table[i] = getGBValue(charTable[i]);//得到GB2312码的首字母区间端点表，十进制。
        }
        table[26]=END;
    }

    /**
     * 根据汉字c获取其对应的编码
     * 将一个汉字（GB2312）转换为十进制表示
     */
    private static int getGBValue(char c){
        String str = c+"";
        try {
            byte[] bytes = str.getBytes("GB2312");
            if(bytes.length<2){
                return 0;
            }
            return (bytes[0]<<8 & 0xff00) + (bytes[1] & 0xff);
        } catch (UnsupportedEncodingException e) {
            return 0;
        }
    }


    /**----------------对外调用方法区-----------------------------**/
    /**
     * 根据输入的单汉字，返回首字母
     * @param ch 所需要查询的汉字
     * @return 返回对应输入汉字的首字母
     */
    public static char getFirstWord(char ch){
        //ch为英文字符，小写转为大写，大写直接返回
        if(ch>='a' && ch<='z'){
            return (char) (ch -'a' +'A');
        }
        if(ch>='A' && ch<='Z'){
            return ch;
        }
        //若为汉字，获取其区间值，并判断是否在码表的范围中
        //若不是，返回&
        //若是，在码表中对其进行判断
        int gb = getGBValue(ch);
        
        if(gb < BEGIN || gb > END){
            return '&';
        }
        int i;
        for ( i = 0; i < 26; i++) {//判断匹配码表区间，匹配到就break，判断区间形如“[,)”
            if(gb >= table[i] && gb < table[i+1]){
                break;
            }
        }
        if(gb == END){//补齐gb2312区间最右端，就是首字母编码值为END
            i=25;
        }
        return initialtable[i];//在码表区间中，返回首字母
    }


    /**
     * 根据输入的汉字字符串，返回首字母字符串，内部机制依赖调用 getFirstWord(char ch)方法。
     * @param str 所需要查询的汉字字符串
     * @return 返回对应输入汉字字符串的首字母字符串
     */
    public static String getFirstWord(String str){
        String result = "";
        if(str!=null || str!= ""){//容错操作，校验输入的字符串
            int length = str.length();
            for (int i = 0; i < length; i++) {//分别对str的各个字符进行取首字母操作
                result += getFirstWord(str.charAt(i));
            }
        }
        return result;
    }

    /**
     * 提供两个字符串，根据其首字母字符串的排列顺序
     * @param str1
     * @param str2
     * @return  比较的前后两个字符串的asc码差值
     * true： str1位于str2前面
     * false：str1位于str2后面
     */
    public static int Compare(String str1,String str2){
        String cstr1 = getFirstWord(str1);
        String cstr2 = getFirstWord(str2);
        return cstr1.compareTo(cstr2);
    }

}
