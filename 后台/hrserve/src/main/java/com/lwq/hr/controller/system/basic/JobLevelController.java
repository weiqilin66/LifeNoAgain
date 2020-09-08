package com.lwq.hr.controller.system.basic;

import com.lwq.hr.entity.Joblevel;
import lwq.returnbean.RespBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @author: LinWeiQi
 */
@RequestMapping("/system/basic/jl")
@RestController
public class JobLevelController {

    @Resource
    com.lwq.hr.mapper.joblevelMapper joblevelMapper;

    @GetMapping("/")
    public List<Joblevel> getJobLevel(){

        return joblevelMapper.selectAll();
    }

    @PostMapping("/")
    @Transactional
    public RespBean addJobLevel(@RequestBody Joblevel jl){
        jl.setCreateDate(new Date());
        jl.setEnabled(true);
        int res = joblevelMapper.insert(jl);
        if (res!=1) {
            return RespBean.error("添加失败");
        }
        return RespBean.ok("添加成功");
    }

    @PutMapping("/")
    @Transactional
    public RespBean updateJobLevel(@RequestBody Joblevel jl){
        int res = joblevelMapper.updateById(jl);
        if (res!=1) {
            return RespBean.error("修改失败");
        }
        return RespBean.ok("修改成功");
    }
    
    @DeleteMapping("/{id}")
    @Transactional
    public RespBean deleteById(@PathVariable int id){
        int res = joblevelMapper.deleteById(id);
        if (res!=1) {
            return RespBean.error("删除失败");

        }
        return RespBean.ok("删除成功");
    }
    @DeleteMapping("/")
    public RespBean batchDelById(@Param("ids") int[] ids){
        ArrayList<Serializable> idList = new ArrayList<>();

        for (int i = 0; i < ids.length; i++) {
            idList.add(ids[i]);
        }
        joblevelMapper.deleteBatchIds(idList);
        return RespBean.ok("批量删除成功");
    }
}
