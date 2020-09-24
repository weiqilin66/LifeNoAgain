package com.lwq.hr.controller.warn;

import com.lwq.hr.service.WarningService;
import com.lwq.hr.utils.RespBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description: 预警
 * @author: LinWeiQi
 */
@RestController
@RequestMapping("/warning")
public class WarningController {
    @Resource
    WarningService warningService;
    /**
     * 接受crawl结束信号
     * msg: 信号消息
     * level: 预警等级
     */
    @GetMapping("/accept")
    public RespBean accept(String msg,int level){
        warningService.othersLower();
        return RespBean.ok("Hr收到!");
    }
}
