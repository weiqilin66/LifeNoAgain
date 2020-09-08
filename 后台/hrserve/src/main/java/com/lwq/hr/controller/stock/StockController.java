package com.lwq.hr.controller.stock;

import com.lwq.hr.entity.MyStock;
import com.lwq.hr.mapper.MyStockMapper;
import lwq.returnbean.RespBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @author: LinWeiQi
 */
@RestController
@RequestMapping("/stock/stock1")
public class StockController {
    @Resource
    MyStockMapper stockMapper;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    String now = dateFormat.format(new Date());

    @GetMapping("/getTitle")
    public RespBean getTitle(String kw){
        List<String> list = stockMapper.getTitles(kw);
        return RespBean.ok(list);
    }
    // 分页查询
    /*public RespPageBean getByPage(int page, int size, String keyWord, int id){
        RespPageBean respPageBean = new RespPageBean();
        if (page!=0 && size!=0) {
            page = (page-1)*size;
        }
        long total = stockMapper.getTotal(now);
        respPageBean.setTotal(total);
        Employee employee = new Employee();
        employee.setName(keyWord);
        if(id != -1){
            employee.setId(id);
        }
        List<Employee> list = stockMapper.getByPage(page, size,employee,null);
        respPageBean.setData(list);
        return respPageBean;
    }*/
    
    @GetMapping("/")
    public RespBean getAll(){
        return RespBean.build().setData(stockMapper.getAllWithHunter(now));
//        return RespBean.build().setData(stockMapper.getAll());
    }
    @PostMapping("/check")
    public RespBean check(@RequestBody MyStock stock){
        //存在性验证
        List<MyStock> myStocks = stockMapper.selStock(stock.getTitle());
        StringBuilder msg= new StringBuilder("已存在类似商品: [");
        for (MyStock myStock : myStocks) {
            msg.append(myStock.getTitle());
            msg.append(" ");
        }
        msg.append("]");
        if (myStocks.size()>0) {
            return RespBean.build().setData(msg.toString());
        }
        return RespBean.ok();
    }
    //新增
    @PostMapping("/")
    @Transactional
    public RespBean add(@RequestBody MyStock stock){
        String title = stock.getTitle();
        int res = stockMapper.insert(stock);
        if (res<1) {
            return RespBean.error("插入失败");
        }
        return RespBean.ok();
    }
    //修改(库存/所有)
    @PutMapping("/")
    @Transactional
    public RespBean update(@RequestBody MyStock stock){
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
