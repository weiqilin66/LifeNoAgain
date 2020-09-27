package com.lwq.hr.controller.warn;

import com.lwq.hr.entity.WarnLowerPrice;
import com.lwq.hr.service.WarningService;
import com.lwq.hr.utils.RespBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description:
 * @author: LinWeiQi
 */
@RestController
@RequestMapping("/price")
public class PriceController {
    @Resource
    WarningService warningService;

    @GetMapping("/")
    public RespBean get(String msg) throws Exception {
        if ("setLow".equals(msg)) {
            warningService.computeLowerPrice();
            return RespBean.ok("执行预警底价成功");
        }
        return RespBean.error("msg:["+msg+"]不存在");
    }
}
