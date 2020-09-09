package com.lwq.hr.timer;

import java.util.Timer;

/**
 * 初始化servlet上下文时开始执行定时任务
 */
public class RunTimer implements ServletContextListener {
    private Timer timer;
}
