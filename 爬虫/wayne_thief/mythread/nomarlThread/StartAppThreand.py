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
class StartAppThread(threading.Thread):
    def __init__(self, name, app):
        super(StartAppThread, self).__init__()
        self.name = name
        self.app = app

    # start() 自动调用run方法
    def run(self):
        print('[%s]--线程启动' % self.name)
        self.app.run(host="0.0.0.0", port=8383, debug=True)

