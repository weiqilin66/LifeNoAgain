import time
from service.taobao import db
from service.taobao.data_by_search import main, init
import requests
import tkinter.messagebox  # 弹窗库

if __name__ == '__main__':
    base = 'http://localhost:8081/warning/accept?'

    # 登录等初始化
    taobao, chrome, mysql = init()

    # 遍历标题检索
    try:
        # 第一遍为全部爬取
        etl_date = time.strftime("%Y%m%d", time.localtime())
        etl_time = time.strftime("%H:%M:%S", time.localtime())
        res, chrome, mysql = main(taobao, chrome, mysql, db.all_goods())
        if res == 'over':
            print('task全额爬取： ', etl_date, '-', etl_time, ' 开始', '-' * 30)
            etl_date = time.strftime("%Y%m%d", time.localtime())
            etl_time = time.strftime("%H:%M:%S", time.localtime())
            print('task全额爬取： ', etl_date, '-', etl_time, ' 结束', '-' * 30)
            # 执行预警任务
            url = base + 'msg=warning001'
            r = requests.get(url)
            print(r.text)
        print('任务执行休息间隔3小时')
        time.sleep(1 * 60 * 60 * 3)
        while 1 == 1:
            for i in range(3):   # 0 1 2
                main(taobao, chrome, mysql, db.t_goods(3))
                print('T3卡爬取休息间隔3小时', '-' * 30)
                time.sleep(1 * 60 * 60 * 3)
            main(taobao, chrome, mysql, db.t_goods(2))
            print('T2卡爬取休息间隔2小时', '-' * 30)
            time.sleep(1 * 60 * 60 * 2)
    except Exception as e:
        mysql.closeDb()
        tkinter.messagebox.showerror('提示', '请登录')
        chrome.quit()
        print("爬取出错:", e, '#' * 30)
