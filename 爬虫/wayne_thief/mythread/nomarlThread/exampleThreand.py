import threading
import time
from queue import Queue
import requests
from lxml import etree
import json
"""两类线程 下载/解析  队列/线程/等待"""
"""面向对象"""
crawl_list = []
parse_list = []


# 采集线程 继承threading
class CrawlThread(threading.Thread):
    def __init__(self, name, data_que, page_que):
        super(CrawlThread, self).__init__()
        self.name = name
        self.data_que = data_que
        self.page_que = page_que
        self.url = 'http://www.ifanjian.net/jiantu-{}'
        self.headers = {
            'user-agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36'
        }

    # start() 自动调用run方法
    def run(self):
        print('[%s]--线程启动' % self.name)
        while 1:  # 一直跑
            # 从队列取出页码, 拼接url发送请求, 将响应内容存放到data_queue中
            if self.page_que.empty():
                break
            page = self.page_que.get()
            url = self.url.format(page)
            r = requests.get(url, headers=self.headers)
            self.data_que.put(r.text)

        print('[%s]--线程结束' % self.name)

