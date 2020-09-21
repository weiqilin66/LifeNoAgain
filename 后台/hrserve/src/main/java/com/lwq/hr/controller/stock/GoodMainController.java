package com.lwq.hr.controller.stock;

import com.lwq.hr.entity.GoodMain;
import com.lwq.hr.mapper.GoodMainMapper;
import com.lwq.hr.utils.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @author: LinWeiQi
 */
@RestController
@RequestMapping("/noRight/goodMain")
public class GoodMainController {
    @Resource
    GoodMainMapper goodMainMapper;

    @GetMapping("/")
    public RespBean getAll(){
        List<GoodMain> list = goodMainMapper.queryAll();
        return RespBean.ok(list);
    }
}
