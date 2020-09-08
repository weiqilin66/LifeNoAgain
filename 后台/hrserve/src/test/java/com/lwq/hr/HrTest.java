package com.lwq.hr;


import com.lwq.hr.entity.Hr;
import com.lwq.hr.mapper.GoodsMapper;
import com.lwq.hr.mapper.HrMapper;
import com.lwq.hr.mapper.MyFocusMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SpringBootTest
class HrTest {

    @Resource
    HrMapper hrMapper;
    @Resource
    GoodsMapper goodsMapper;
    @Resource
    MyFocusMapper myFocusMapper;
    @Test
    void t1(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String now = dateFormat.format(new Date());
        System.out.println(now);

    };
    @Test
    void t2(){
        List<Hr> all = hrMapper.getAllHrs(-1);
        System.out.println(all);
    }

    public static void main(String[] args) {
        AntPathMatcher antPathMatcher =new AntPathMatcher();
        boolean match = antPathMatcher.match("/statistics/chart/**", "/statistics/chart/");
        System.out.println(match);
    }
}

