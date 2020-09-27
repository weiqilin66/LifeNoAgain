package com.lwq.hr.controller.stock;

import com.lwq.hr.entity.GoodStock;
import com.lwq.hr.entity.GoodStockVo;
import com.lwq.hr.mapper.GoodStockMapper;
import com.lwq.hr.utils.RespBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Description: 库存整理
 * @author: LinWeiQi
 */
@RestController
@RequestMapping("/stock/stock1")
public class StockController {
    @Resource
    GoodStockMapper stockMapper;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    String now = dateFormat.format(new Date());

    @GetMapping("/getTitle")
    public RespBean getTitle(String title) {
        List<String> list = stockMapper.chooseGoodTitle(title);
        return RespBean.ok(list);
    }
    /**
     * 返回所有商品
     */
    @GetMapping("/")
    public RespBean getAll(){
        return RespBean.ok(stockMapper.queryAll());
    }
    @PostMapping("/check")
    public RespBean check(@RequestBody GoodStockVo stock){
        //存在性验证
        List<GoodStock> myStocks = stockMapper.checkByStockId(stock);
        if (myStocks.size()>0) {
            StringBuilder msg= new StringBuilder("已存在类似商品: [");
            msg.append(myStocks.get(0).toString());
            msg.append("]");
            return RespBean.error(msg.toString());
        }

        return RespBean.ok();
    }
    //新增
    @PostMapping("/")
    @Transactional
    public RespBean add(@RequestBody GoodStock stock){
        int res = stockMapper.insert(stock);
        if (res<1) {
            return RespBean.error("插入失败");
        }
        return RespBean.ok();
    }
    //修改(库存/所有)
    @PutMapping("/")
    @Transactional
    public RespBean update(@RequestBody GoodStock stock){
        int res = stockMapper.updateById(stock);
        if (res!=1) {
            return RespBean.error("更新出错");
        }
        return RespBean.ok();
    }
    //删除
    @DeleteMapping("/{id}")
    @Transactional
    public RespBean del(@PathVariable int id){
        int res = stockMapper.deleteById(id);
        if (res!=1) {
            return RespBean.error("删除失败");
        }
        return RespBean.ok();
    }

}
