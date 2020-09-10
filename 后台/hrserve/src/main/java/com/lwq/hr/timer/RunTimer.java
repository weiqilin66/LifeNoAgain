package com.lwq.hr.timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Timer;

/**
 * 初始化servlet上下文时开始执行定时任务
 */
public class RunTimer implements ServletContextListener {
    private Timer timer;

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (timer!=null) {
            timer.cancel();
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        timer = new Timer();
    }
}
