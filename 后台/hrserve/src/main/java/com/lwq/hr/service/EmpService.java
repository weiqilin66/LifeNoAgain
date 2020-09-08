package com.lwq.hr.service;

import com.lwq.hr.entity.Employee;
import com.lwq.hr.mapper.EmployeeMapper;
import lwq.returnbean.RespPageBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: select *from table limit 5,10  查询第6-15条数据
 * @author: LinWeiQi
 */
@Service
public class EmpService {

    @Resource
    EmployeeMapper employeeMapper;

    // 分页查询
    public RespPageBean getByPage(int page,int size,String keyWord,int id){
        RespPageBean respPageBean = new RespPageBean();
        if (page!=0 && size!=0) {
            page = (page-1)*size;
        }
        long total = employeeMapper.getTotal(null,null);
        respPageBean.setTotal(total);
        Employee employee = new Employee();
        employee.setName(keyWord);
        if(id != -1){
            employee.setId(id);
        }
        List<Employee> list = employeeMapper.getEmployeeByPage(page, size,employee,null);
        respPageBean.setData(list);
        return respPageBean;
    }
}
