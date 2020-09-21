package com.lwq.hr.controller.stock;

import com.lwq.hr.entity.CoreCrawlTb;
import com.lwq.hr.mapper.CoreCrawlTbMapper;
import com.lwq.hr.mapper.StockCrawlMapper;
import com.lwq.hr.utils.DateFormatUtil;
import com.lwq.hr.utils.RespBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Description:
 * @author: LinWeiQi
 */
@RestController
@RequestMapping("/stock/crawl")
public class CoreCrawlController {
    @Resource
    StockCrawlMapper stockCrawlMapper;
    @Resource
    CoreCrawlTbMapper coreCrawlTbMapper;
    String now = DateFormatUtil.formatStr(new Date(),"yyyyMMdd");


    @GetMapping("/")
    public RespBean getAll(){
        List<HashMap> list = coreCrawlTbMapper.queryAll();
        return RespBean.ok(list);
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
    public RespBean changeStatus(@RequestBody CoreCrawlTb coreCrawlTb){
        int res = coreCrawlTbMapper.updateById(coreCrawlTb);
        if (res!=1) {
            return RespBean.error("修改失败");
        }
        return RespBean.ok();
    }
    //新增
    @PostMapping("/")
    @Transactional
    public RespBean add(@RequestBody CoreCrawlTb coreCrawlTb){
        int res = coreCrawlTbMapper.insert(coreCrawlTb);
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
            StringBuilder msg= new StringBuilder("已存在类似商品");
            return RespBean.build().setMessage(msg.toString());
        }
        return RespBean.ok();
    }

}
