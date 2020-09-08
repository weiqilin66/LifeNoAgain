package org.wayne.commons.utils;

/**
 * @Description: 字符串操作
 * @author: LinWeiQi
 *
 * 常用方法
 * s.trim() 清除空格
 * s.startWith(x)  endWith(x)  以x开头/结尾
 * s.length() 长度
 * getBytes() 返回字节数组
 * equals()和equalsIgnoreCase()
 * toUpperCase()和toLowerCase()
 * substring(a,b) 左开右闭 不包含b索引的字符
 * s.indexOf(x)和lastIndexOf(x)  x第一次和最后一次出现的位置
 * s.replace(a,b) s中a字符串换成b字符串
 */
public class ChangeStr {
    /**
     * 转字符数组
     */
    public static char[] toCharArray(String str) {
        return str.toCharArray();
    }

}
