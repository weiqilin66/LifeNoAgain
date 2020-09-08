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


# 解析线程
class ParseThread(threading.Thread):
    def __init__(self, name, data_que, fp, lock,page_que):
        super(ParseThread, self).__init__()
        self.name = name
        self.data_que = data_que
        self.fp = fp
        self.lock = lock
        self.page_que = page_que

    def run(self):
        print('[%s]线程启动' % self.name)
        while 1:
            if self.page_que.empty():
                time.sleep(10)
            if self.page_que.empty() & self.data_que.empty():
                break
            # data_que取数据
            data = self.data_que.get()
            self.parse_content(data)

        print('[%s]线程结束' % self.name)

    def parse_content(self, data):
        tree = etree.HTML(data)
        items = []
        # 获取标题,图片
        title_list = tree.xpath('//h2/a/@title')
        url_list = tree.xpath('//div[@class=\'cont-list-main\']//p/img/@data-src')
        for i in range(0, len(title_list)):
            item = {
                '标题': title_list[i],
                '图片url': url_list[i]
            }
            items.append(item)
        print(items)
        # 写入文件中
        # self.lock.acquire()   锁
        # self.lock.release()

        self.lock.acquire()
        self.fp.write(json.dumps(items, ensure_ascii=False) +'\n')
        self.lock.release()


def create_queue():
    # 创建页码队列
    page_que = Queue()
    for page in range(1, 11):
        page_que.put(page)
    # 创建内容队列
    data_que = Queue()
    return data_que, page_que

# 批量生产采集线程
def create_crawl_threads(data_que, page_que):
    crawl_name = ['采集线程1号', '采集线程2号', '采集线程3号']
    for name in crawl_name:
        t_crawl = CrawlThread(name, data_que, page_que)
        crawl_list.append(t_crawl)


def create_parse_threads(data_que, fp, lock,page_que):
    parse_name = ['解析线程1号', '解析线程2号', '解析线程3号']
    for name in parse_name:
        t_parse = ParseThread(name, data_que, fp, lock,page_que)
        parse_list.append(t_parse)


def main():
    # 1. 创建队列
    data_que, page_que = create_queue()
    # 创建文件, 锁
    fp = open('jian.json', 'a')  # a 追加写入
    lock = threading.Lock()
    # 2. 创建采集线程
    create_crawl_threads(data_que, page_que)
    # 3. 创建解析线程
    create_parse_threads(data_que, fp, lock,page_que)
    # 4. 启动所有线程
    for t_crawl in crawl_list:
        t_crawl.start()
    for t_parse in parse_list:
        t_parse.start()

    # 5. 主线程等待子线程结束
    for t_crawl in crawl_list:
        t_crawl.join()
    for t_parse in parse_list:
        t_parse.join()

    fp.close()
    print('---------- done! --------------')


if __name__ == '__main__':
    main()
