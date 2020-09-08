package com.lwq.hr.utils;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.util.Scanner;

/**
 * @Description: 控制台读取
 * @author: LinWeiQi
 */
public class scannerUtil {
    /**
     * @TODO   提问并返回下一行控制器输入值
     * @return java.lang.String
     * @param  [tip] 提示内容
     * @date   2020/1/7
     */
    public static String sc(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder str = new StringBuilder();
        str.append("请输入" + tip + "：");
        System.out.println(str.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }
}
