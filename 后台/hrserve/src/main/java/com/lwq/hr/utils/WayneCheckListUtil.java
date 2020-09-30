package com.lwq.hr.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 操作集合工具
 * @author: LinWeiQi
 */
public class WayneCheckListUtil {
    /**
     * @TODO   判断集合中有没有重复的字符串
     * @return
     * @param
     * @date   2020/9/30
     */
    public static boolean checkRepeat(List<String> list){
        final long count = list.stream().distinct().count();
        if (list.size()==count) {
            return false;
        }
        return true;
    }
}
