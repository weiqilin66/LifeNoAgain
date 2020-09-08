package com.lwq.hr.controller.system.basic;

import com.lwq.hr.entity.Position;
import com.lwq.hr.mapper.PositionMapper;
import lwq.returnbean.RespBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: rest风格接口 get del都是url传参 post put 是传递的JSON
 * @author: LinWeiQi
 */
@RestController
@RequestMapping("/system/basic/pos")
public class PositionController {

    @Resource
    PositionMapper positionMapper;

    @GetMapping("/")
    public List<Position> queryAll() {
        return positionMapper.queryAll();
    }

    @PostMapping("/")
    public RespBean add(@RequestBody Position position) {
        position.setEnabled(true);
        position.setCreateDate(new Date());
        if (positionMapper.insert(position) != 1) {
            return RespBean.error("添加失败");
        }
        return RespBean.ok("添加成功");
    }

    @PutMapping("/")
    public RespBean update(@RequestBody Position position) {
        int res = positionMapper.updateById(position);
        if (res != 1) {
            return RespBean.error("更新失败");
        }
        return RespBean.ok("更新成功");
    }

    @DeleteMapping("/{id}")
    public RespBean del(@PathVariable int id) {
        int res = positionMapper.deleteById(id);
        if (res != 1) {
            return RespBean.error("删除失败");
        }
        return RespBean.ok("删除成功");
    }

    /**
     * @TODO  批量删除
     * @param [ids] 传入的id数组
     * @date 2020/2/4
     */
    @DeleteMapping("/")
    public RespBean delByIds(@Param("ids") int[] ids) {
        List<Integer> idsList = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            idsList.add(ids[i]);
        }
        int res = positionMapper.deleteBatchIds(idsList);
        if (res != idsList.size()) {
            return RespBean.error();
        }
        return RespBean.ok("批量删除成功");
    }
}
