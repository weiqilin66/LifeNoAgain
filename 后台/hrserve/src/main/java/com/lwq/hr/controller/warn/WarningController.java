package com.lwq.hr.controller.warn;

import com.lwq.hr.mapper.WarnHunterMapper;
import com.lwq.hr.service.WarningService;
import com.lwq.hr.utils.RespBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.Method;

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
    public RespBean accept(String msg,@RequestParam(defaultValue = "3") int level){
        try {
            if ("all".equalsIgnoreCase(msg)){
                for (Method method : WarningService.class.getDeclaredMethods()) {
                    method.invoke(warningService);
                }
            }else if ("warning001".equalsIgnoreCase(msg)) {
                warningService.warning001();
            }else if("ping".equalsIgnoreCase(msg)){
                System.out.println("pong");
            }else {
                return RespBean.error("["+msg+"]预警存在");
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
            return RespBean.error("["+msg+"]预警执行失败!");
        }
        return RespBean.ok("["+msg+"]预警执行结束!");
    }
    /**
     * @TODO   预警信息查询
     * @return
     * @param
     * @date   2020/9/26
     */
    @GetMapping("/")
    public RespBean get(String msg){
        if ("hunter".equalsIgnoreCase(msg)) {
            return RespBean.ok(warningService.getHunter());
        }else if ("othersLower".equalsIgnoreCase(msg)){
            return RespBean.ok(warningService.getOthersLower());
        }
        return RespBean.error("msg["+msg+"]不存在");
    }
}
