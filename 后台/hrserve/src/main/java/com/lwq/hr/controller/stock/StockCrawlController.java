package com.lwq.hr.controller.stock;

import com.lwq.hr.entity.MyStock;
import com.lwq.hr.mapper.MyStockMapper;
import com.lwq.hr.mapper.StockCrawlMapper;
import com.lwq.hr.utils.BaseUtil;
import com.lwq.hr.utils.RespBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @author: LinWeiQi
 */
@RestController
@RequestMapping("/stock/crawl")
public class StockCrawlController {
    @Resource
    StockCrawlMapper stockCrawlMapper;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    String now = dateFormat.format(new Date());

    @GetMapping("/")
    public RespBean getAll(){
        return RespBean.build().setData(stockCrawlMapper.select(null));
    }
    //删除
    @DeleteMapping("/{id}")
    @Transactional
    public RespBean del(@PathVariable int id){
        final HashMap<String, Object> map = new HashMap<>();
        map.put("id",id);
        int res = stockCrawlMapper.delete(map);
        if (res!=1) {
            return RespBean.error("删除失败");
        }
        return RespBean.ok();
    }
    //修改
    @PutMapping("/")
    @Transactional
    public RespBean changeStatus(@RequestBody HashMap map){
        int res = stockCrawlMapper.update(map);
        if (res!=1) {
            return RespBean.error("修改失败");
        }
        return RespBean.ok();
    }
    //新增
    @PostMapping("/")
    @Transactional
    public RespBean add(@RequestBody HashMap map){//@RequestBody映射body中的json不加这个映射的是请求行中的key value
        int res = stockCrawlMapper.insert(map);
        if (res<1) {
            return RespBean.error("插入失败");
        }
        return RespBean.ok();
    }
    @PostMapping("/check")
    public RespBean check(@RequestBody  HashMap map){
        //存在性验证
        final List list = stockCrawlMapper.select(map);
        if (list.size()>0) {
            StringBuilder msg= new StringBuilder("已存在类似商品: [");
            msg.append(map.get("title"));
            msg.append("]");
            return RespBean.error(msg.toString());
        }
        return RespBean.ok();
    }

}
