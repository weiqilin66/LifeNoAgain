package com.lwq.hr.controller.system.basic;

import com.lwq.hr.entity.Department;
import com.lwq.hr.mapper.DepartmentMapper;
import lwq.returnbean.RespBean;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @author: LinWeiQi
 */
@RestController
@RequestMapping("/system/basic/dep")
public class DepController {

    @Resource
    DepartmentMapper departmentMapper;
    /**
     * @TODO   根据父ID递归 获取所有部门
     * @param  -1 最上级的id
     * @date   2020/2/13
     */
    @GetMapping("/")
    public List<Department> getAllDepByParentId(){
        return departmentMapper.getAllDepByParentId(-1);
    }
    /** 
     * @TODO   调用存储过程批量执行sql 
     * 优点 : 当分开部署时 数据库一个服务器,应用多次调用sql 存在网络问题
     * 缺点 : 数据库耦合 不好做数据库迁移
     * @date   2020/2/13 
     */
    @PostMapping("/")
    public RespBean addDepByProcess(@RequestBody Department dep){
        dep.setEnabled(true);
        departmentMapper.addDepByProcess(dep);
        if (dep.getResult()==1) {

            return RespBean.ok("添加成功",dep);
        }
        return RespBean.error("添加失败");
    }
    @DeleteMapping("/{id}")
    public RespBean delByProcess(@PathVariable int id){
        // 存储过程out的值存在实体中
        Department dep = new Department();
        dep.setId(id);
        departmentMapper.delByProcess(dep);
        if (dep.getResult()==-2) {
            return RespBean.error("该部门下有子部门删除失败");
        }else if(dep.getResult()==-1){
            return RespBean.error("该部门下有员工删除失败!");
        }else if(dep.getResult()==1){
            return RespBean.ok("删除成功");
        }
        return RespBean.error();// 由全局异常捕捉返回失败信息
    }
}
