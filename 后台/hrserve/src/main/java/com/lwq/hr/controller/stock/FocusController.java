package com.lwq.hr.controller.stock;

import com.lwq.hr.entity.MyFocus;
import com.lwq.hr.mapper.MyFocusMapper;
import lwq.returnbean.RespBean;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @author: LinWeiQi
 */
@RequestMapping("/stock/focus")
@RestController
public class FocusController {

    @Resource
    MyFocusMapper myFocusMapper;

    @GetMapping("/")
    public List<MyFocus> getList() {

        return myFocusMapper.queryAll();
    }

    @PutMapping("/")
    public RespBean update(@RequestBody MyFocus myFocus){

        int res = myFocusMapper.updateById(myFocus);

        return res==1?RespBean.ok():RespBean.error();
    }
    @PostMapping("/")
    public RespBean add(@RequestBody MyFocus myFocus){
        int res = myFocusMapper.insert(myFocus);
        return res==1?RespBean.ok():RespBean.error();
    }
    @DeleteMapping("/{id}")
    public RespBean del(@PathVariable int id){
        int res = myFocusMapper.deleteById(id);
        return res==1?RespBean.ok():RespBean.error();
    }
}
