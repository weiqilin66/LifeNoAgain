import time

from service.taobao.bySearch import main, init

if __name__ == '__main__':
    # 登录等初始化
    taobao, chrome, mysql = init()

    # 遍历标题检索
    while 1 == 1:
        etl_date = time.strftime("%Y%m%d", time.localtime())
        etl_time = time.strftime("%H:%M:%S", time.localtime())
        res, chrome, mysql = main(taobao, chrome, mysql)
        if res == 'over':
            print('task： ', etl_date, '-', etl_time, ' 开始')
            etl_date = time.strftime("%Y%m%d", time.localtime())
            etl_time = time.strftime("%H:%M:%S", time.localtime())
            print(etl_date, '-', etl_time, ' 结束')
        print('任务执行间隔3小时')
        time.sleep(1 * 60 * 60 * 3)
