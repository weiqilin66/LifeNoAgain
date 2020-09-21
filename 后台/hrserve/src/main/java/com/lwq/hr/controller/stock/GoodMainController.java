package com.lwq.hr.controller.stock;

import com.alibaba.druid.sql.visitor.functions.If;
import com.lwq.hr.entity.GoodMain;
import com.lwq.hr.mapper.GoodMainMapper;
import com.lwq.hr.utils.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: goodManage
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
    @PostMapping("/")
    public RespBean add(@RequestBody GoodMain goodMain){
        final int res = goodMainMapper.insert(goodMain);
        if(res<1){
            return RespBean.error();
        }
        return RespBean.ok();
    }
    @DeleteMapping("/{id}")
    public RespBean del(@PathVariable int id){
        final int i = goodMainMapper.deleteById(id);
        if (i<1){
            return RespBean.error("删除失败");
        }
        return RespBean.ok();
    }
    @PutMapping("/")
    public RespBean update(@RequestBody GoodMain entity){
        final int i = goodMainMapper.updateById(entity);
        if(i<1){
            return RespBean.error("更新失败");
        }
        return RespBean.ok();
    }
}
