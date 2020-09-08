package com.lwq.hr.controller.config;

import com.lwq.hr.entity.Menu;
import com.lwq.hr.service.MenuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @author: LinWeiQi
 */
@RestController
@RequestMapping("/sysConfig")
public class SysConfigController {

    @Resource
    MenuService menuService;

    @GetMapping("/menu")
    public List<Menu> getMenuById(){
        return menuService.getMenuById();

    }
}
