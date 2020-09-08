package com.lwq.hr.utils;

/**
 * "CLIENT_NO" 转为驼峰命名：clientNo
 * "clientNo" 转为下划线命名：CLIENT_NO
 */
public class CamelFormatUtil {
    /*
    public static void main(String[] args) {
        System.out.println( UnderlineToCamel("CLIENT_NO"));
    }
    */

    //下划线命名转为驼峰命名
    public static String ToCamel(String param){
        StringBuilder result=new StringBuilder();
        String arr[]=param.split("_");
        for(String s:arr){
            if (!param.contains("_")) { // 传入的值不包含_ 返回原值
                result.append(s);
                continue;
            }
            if(result.length()==0){
                result.append(s.toLowerCase());
            }else{
                result.append(s.substring(0, 1).toUpperCase());// 字符大写
                result.append(s.substring(1).toLowerCase());// 余下小写
            }
        }
        return result.toString();
    }

    //驼峰命名转为下划线命名
    public static String ToSqlStyle(String para){
        StringBuilder sb=new StringBuilder(para);
        int temp=0;//定位
        if (!para.contains("_")) {
            for(int i=0;i<para.length();i++){
                if(Character.isUpperCase(para.charAt(i))){
                    sb.insert(i+temp, "_");
                    temp+=1;
                }
            }
        }
        return sb.toString().toUpperCase();
    }

}
