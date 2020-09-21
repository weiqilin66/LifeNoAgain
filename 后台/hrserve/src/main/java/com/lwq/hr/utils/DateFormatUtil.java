package com.lwq.hr.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description:
 * @author: LinWeiQi
 */
public class DateFormatUtil {

    public static String formatStr(Date date, String type){
        SimpleDateFormat sdf;
        if ("yyyyMMdd".equalsIgnoreCase(type)) {
            return new SimpleDateFormat("yyyyMMdd").format(date);
        }else if ("yyyy-MM-dd".equalsIgnoreCase(type)){
            return new SimpleDateFormat("yyyy-MM-dd").format(date);
        }
        return null;
    }
}
