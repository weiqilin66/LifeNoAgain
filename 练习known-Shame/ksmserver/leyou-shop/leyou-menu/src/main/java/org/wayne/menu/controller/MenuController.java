package org.wayne.menu.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wayne.menu.entity.Menu;
import org.wayne.menu.service.MenuService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @author: LinWeiQi
 */
@RestController
public class MenuController {

    @Resource
    MenuService menuService;

    @GetMapping("/list")
    public List<Menu> getMenuList(){
        return menuService.getMenuById();
    }
}
