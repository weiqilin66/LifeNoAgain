package com.lwq.hr.service;

import com.lwq.hr.entity.Hr;
import com.lwq.hr.mapper.HrMapper;
import com.lwq.hr.mapper.RoleMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description: SpringSecurity接入数据库 查询用户
 * @author: LinWeiQi
 */
@Service
public class UserService implements UserDetailsService {

    @Resource
    HrMapper hrMapper;

    @Resource
    RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        Hr hr = hrMapper.getHr(s);
        if (hr == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        hr.setRoles(roleMapper.queryRoles(hr.getId()));
        return hr;
    }
}
