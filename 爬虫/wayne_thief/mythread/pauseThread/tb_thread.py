import threading
import time
from service.taobao.bySearch import main


class tbThread(threading.Thread):
    def __init__(self,chrome, *args, **kwargs):
        super(tbThread, self).__init__(*args, **kwargs)
        self.__flag = threading.Event()     # 用于暂停线程的标识
        self.__flag.set()       # 设置为True
        self.__running = threading.Event()      # 用于停止线程的标识
        self.__running.set()      # 将running设置为True
        self.chrome = chrome
        self.task_name=''

    # start 和 pause 生效任务
    def run(self):
        while self.__running.isSet():
            self.__flag.wait()      # 为True时立即返回, 为False时阻塞直到内部的标识位为True后返回
            # 业务
            goodName = self.task_name
            if goodName != '':
                pass
            print(time.time())
            print(self.chrome)
            time.sleep(1)
            self.__flag.clear()     # 搜索完一次设置为False, 让线程阻塞

    def pause(self):
        self.__flag.clear()     # 设置为False, 让线程阻塞

    def restart(self):
        self.__flag.set()    # 设置为True, 让线程停止阻塞


    def stop(self):
        self.__flag.set()       # 将线程从暂停状态恢复, 如何已经暂停的话
        self.__running.clear()        # 设置为False