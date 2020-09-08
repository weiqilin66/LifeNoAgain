package com.lwq.hr.controller.system.hr;

import com.lwq.hr.entity.Hr;
import com.lwq.hr.entity.Role;
import com.lwq.hr.mapper.HrMapper;
import com.lwq.hr.mapper.HrRoleMapper;
import com.lwq.hr.mapper.RoleMapper;
import com.lwq.hr.utils.HrUtils;
import lwq.returnbean.RespBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @Description: 操作员管理
 * @author: LinWeiQi
 */
@RestController
@RequestMapping("/system/hr")
public class HrController {
    @Resource
    HrMapper hrMapper;
    @Resource
    RoleMapper roleMapper;
    @Resource
    HrRoleMapper hrRoleMapper;

    // 获取除自己之外的管理员
    @GetMapping("/")
    public List<Hr> getAllHrs() {
        return hrMapper.getAllHrs(HrUtils.getCurrentHr().getId());
    }

    // 状态修改
    @PutMapping("/")
    public RespBean updateStatus(@RequestBody Hr hr) {
        int res = hrMapper.updateById(hr);
        if (res != 1) {
            return RespBean.error();
        }
        if (hr.isEnabled()) {
            return RespBean.ok("启用成功");
        }
        return RespBean.ok("禁用成功");
    }

    // 获取所拥有角色
    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleMapper.getRoles();
    }

    // 更新所拥有的角色
    @PutMapping("/roles")
    @Transactional
    public RespBean updateRoles(int hrid, int[] rids) {
        HashMap<String, Object> columnMap = new HashMap<>();
        columnMap.put("hrid", hrid);
        hrRoleMapper.deleteByMap(columnMap);
        int res = hrRoleMapper.addRoles(hrid, rids);
        if (res < 1) {
            return RespBean.error();
        }
        return RespBean.ok("更新角色成功");
    }

    // 删除用户
    @DeleteMapping("/{hrid}")
    public RespBean del(@PathVariable int hrid) {
        Hr hr = new Hr();
        hr.setId(hrid);
        int res = hrMapper.deleteById(hr);
        HashMap<String, Object> columnMap = new HashMap<>();
        columnMap.put("hrid", hrid);
        hrRoleMapper.deleteByMap(columnMap);
        if (res != 1) {
            return RespBean.error();
        }
        return RespBean.ok("删除角色成功");
    }

}
