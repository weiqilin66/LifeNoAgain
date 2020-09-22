package com.lwq.hr.controller.stock;

import com.lwq.hr.entity.GoodMain;
import com.lwq.hr.mapper.GoodKeyWordMapper;
import com.lwq.hr.utils.RespBean;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @Description:
 * @author: LinWeiQi
 */
@RestController
@RequestMapping("/noRight/keyWord")
public class KeyWordController {
    @Resource
    GoodKeyWordMapper goodKeyWordMapper;

    @GetMapping("/")
    public RespBean getAll(){
        List<GoodMain> list = goodKeyWordMapper.queryAll();
        return RespBean.ok(list);
    }
    @PostMapping("/")
    public RespBean add(@RequestBody GoodMain goodMain){
        final int res = goodKeyWordMapper.insert(goodMain);
        if(res<1){
            return RespBean.error();
        }
        return RespBean.ok();
    }
    @PostMapping("/check")
    public RespBean check(@RequestBody GoodMain goodMain){
        HashMap<String, Object> columnMap = new HashMap<>();
        columnMap.put("name",goodMain.getName());
        final List<GoodMain> goodMains = goodKeyWordMapper.selectByMap(columnMap);

        if(goodMains.size()>0){
            return RespBean.error("已存在商品："+goodMain.getName());
        }
        return RespBean.ok(goodMains);
    }
    @DeleteMapping("/{id}")
    public RespBean del(@PathVariable int id){
        final int i = goodKeyWordMapper.deleteById(id);
        if (i<1){
            return RespBean.error("删除失败");
        }
        return RespBean.ok();
    }
    @PutMapping("/")
    public RespBean update(@RequestBody GoodMain entity){
        final int i = goodKeyWordMapper.updateById(entity);
        if(i<1){
            return RespBean.error("更新失败");
        }
        return RespBean.ok();
    }
}
