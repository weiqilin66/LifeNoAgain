package com.lwq.hr.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Description:
 * @author: LinWeiQi
 */
public class DateFormatUtil {
    /**
     * 日期转字符串
     * @param date
     * @param type
     * @return
     */
    public static String formatStr(Date date, String type){
        SimpleDateFormat sdf;
        if ("yyyyMMdd".equalsIgnoreCase(type)) {
            return new SimpleDateFormat("yyyyMMdd").format(date);
        }else if ("yyyy-MM-dd".equalsIgnoreCase(type)){
            return new SimpleDateFormat("yyyy-MM-dd").format(date);
        }
        return null;
    }
    /**
     * @TODO   获得前后day天的日期Date
     * @return java.util.Date
     * @date   2020/10/9
     */
    public static Date getOtherDate(Date date ,int day){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE,day);
        return c.getTime();
    }

    public static void main(String[] args) {
        System.out.println(getOtherDate(new Date(), 0));
    }
}
