import time
import tkinter.messagebox  # 弹窗库
import re
import json
import random
from selenium import common
from util.mysql_util import MySql
from bean.my_selenium import MySelenium

""" 半自动实现 """
mp3_path = r'd:/菊花台.mp3'


# noinspection DuplicatedCode
class TaoBao(object):
    def __init__(self):  # 初始化参数
        self.chrome = MySelenium().chrome
        self.mysql = MySql('localhost', 'root', 'root', 'vhr')

    # 初始化随机搜索宝贝
    def start(self, random_good):
        chrome = self.chrome
        url = 'https://s.taobao.com/'
        chrome.get(url)
        # chrome.maximize_window()  # 窗口最大化方便扫码
        ser_input = chrome.find_elements_by_xpath("//input[@name='q']")[0]
        ser_input.send_keys(random_good)
        time.sleep(1)
        ser_btn = chrome.find_elements_by_xpath("//button")[0]
        ser_btn.click()
        return chrome

    # 二手栏位
    def start2(self):
        chrome = self.chrome
        url = 'https://s.taobao.com/search?tab=old'
        chrome.get(url)
        # chrome.maximize_window()  # 窗口最大化方便扫码
        return chrome
    # 回调登录
    def login(self):
        chrome = self.chrome
        if chrome.current_url.startswith('https://login'):
            # tkinter.messagebox.showerror('提示', '请登录')
            # print('未登录...')
            time.sleep(1)
            self.login()

    # 点击二手标签
    def tapSecondHand(self):
        try:
            btton = self.chrome.find_elements_by_id("//ul[@class='tabs']/li/a[text()='二手']")
            print(len(btton))
            btton = self.chrome.find_elements_by_id("//a[text()='二手']")
            print(len(btton))
            btton = self.chrome.find_elements_by_id("//ul[@class='tabs']/li/a")
            print(len(btton))
            btton.click()
            time.sleep(random.randint(10, 15))
        except Exception as e:
            print("找不到二手按钮", e)

    # 插入数据库
    def info2mysql(self, good_list, etl_date, etl_time, kw, count_index_total):
        index_total = 0
        # 一个good_list是一个网页数据
        for good in good_list:
            shop = good['shop']  # 店铺名
            title = good['title']  # 宝贝标题
            price = float(good['price'])  # 价格
            if '万' in good['sales']:
                sales = float(good['sales'].strip('+万人付款收货')) * 10000  # 销量
            else:
                sales = int(good['sales'].strip('+人付款收货'))  # 销量
            index_total = index_total + sales
            if good['freight'] == '':  # 运费
                freight = 0
            else:
                freight = float(good['freight'])
            detail_url = good['detail_url']  # 详情页
            pic_url = good['pic_url']
            # sql语句
            del_sql = "delete from goods where title = '%s' and etl_date = '%s' and shop ='%s'" % (
                title, etl_date, shop)
            self.mysql.delete(del_sql)
            insert_sql = """
            insert into goods(shop,title,price,sales,freight,etl_date,etl_time,kw,detail_url,img_url) VALUES(
            '%s','%s',%d,%d,%d,'%s','%s','%s','%s','%s')
            """ % (shop, title, price, sales, freight, etl_date, etl_time, kw, detail_url, pic_url)
            self.mysql.insert(insert_sql)
        # 统计首页总量
        if count_index_total == 1:
            self.mysql.update("update core_crawl_tb set total_sales = %d where id in (select id from("
                              "select t1.id from core_crawl_tb t1 inner join good_main t2 on t1.gid = t2.id "
                              "where concat(label,name)='%s')a) " % (index_total, kw))

    # 核心方法 关键词搜索爬取数据
    def data_by_search(self, etl_date, etl_time, chrome, search_good, crawl_type, pages):
        try:
            tb_input = chrome.find_elements_by_xpath("//input[@name='q']")[0]
            tb_btn = chrome.find_elements_by_xpath("//button")[0]
            tb_input.clear()
            tb_input.send_keys(search_good)
            tb_btn.click()
            time.sleep(random.randint(10, 15))
            # 排序按钮     综合排序/销量/信用/价格从低到高/价格从高到低/总价从低到高/总价从高到低
            sort_btn = chrome.find_elements_by_xpath("//li/a[@data-key='sort']")
            # 销量排序
            if crawl_type == 1:  # 下一页不用再按销量排行和搜索
                sort_btn[1].click()
                time.sleep(random.randint(10, 15))
                # 增加筛选条件 价格100+
                # chrome.find_elements_by_xpath("//input[@class='J_SortbarPriceInput input']")[0].send_keys(100)
                # time.sleep(1)
                # chrome.find_elements_by_xpath("//button")[1].click()
                # time.sleep(random.randint(10, 15))
        except common.exceptions.WebDriverException as e:
            print('--webdriver异常: ', e)
            # playsound(mp3_path)
            tkinter.messagebox.showinfo('tip', 'webdriver异常')
            print("手动滑块验证over")
            tb_input = chrome.find_elements_by_xpath("//input[@name='q']")[0]
            tb_btn = chrome.find_elements_by_xpath("//button")[0]
            tb_input.clear()
            tb_input.send_keys(search_good)
            tb_btn.click()
            time.sleep(random.randint(10, 15))
            sort_btn = chrome.find_elements_by_xpath("//li/a[@data-key='sort']")
            if crawl_type == 1:
                sort_btn[1].click()
                time.sleep(random.randint(10, 15))
        # 下拉到底部
        chrome.execute_script('window.scrollTo(0, document.body.scrollHeight)')
        time.sleep(2)
        # 拉取html
        html = chrome.page_source
        json_ = re.findall(r'g_page_config = (.*?)}};', html)[0]
        if json_ == '':
            print('未搜索到: ', search_good)
            return
        json_ = json_ + '}}'
        # 分析json 写入数据库
        print("crawl: %s" % search_good)
        good_list = json2info(json_)
        if good_list == 1:
            return
        print('首页数量: ', len(good_list))
        self.info2mysql(good_list, etl_date, etl_time, search_good, 1)
        # 下一页
        if pages == 1:
            return
        for page in range(0, pages - 1):
            # 下一页按钮不可用 返回
            totalPage = chrome.find_elements_by_xpath(
                "//div[@class='pager']//ul[@class='items']//li/a[@class='link']/span[@class='icon "
                "icon-btn-next-2-disable']")  # 只有一页
            if len(totalPage) > 0:
                return
            try:
                next_btn = chrome.find_element_by_xpath('//li[@class="item next"]//a')
                next_btn.click()
                time.sleep(random.randint(10, 15))
            except common.exceptions.WebDriverException as e:
                print('--webdriver异常: ', e)
                # playsound(mp3_path)
                tkinter.messagebox.showinfo('tip', 'webdriver异常')
                # print("关闭弹窗休息200s")
                # time.sleep(200)
                next_btn = chrome.find_element_by_xpath('//li[@class="item next"]//a')  # 窗口长度不能缩小变短 加载不了下一页js
                next_btn.click()
                time.sleep(random.randint(10, 15))

            # 下拉到底部
            chrome.execute_script('window.scrollTo(0, document.body.scrollHeight)')
            # 拉取html
            html = chrome.page_source
            json_ = re.findall(r'g_page_config = (.*?)}};', html)[0]
            if json_ == '':
                print('翻页后 未搜索到: ', search_good)
                return
            json_ = json_ + '}}'
            # 分析json
            good_list = json2info(json_)
            print('第', page + 2, '页数量: ', len(good_list))
            # 写入数据库
            self.info2mysql(good_list, etl_date, etl_time, search_good, 0)


# 解析JS的json数据 return列表
def json2info(json_):
    json_dic = json.loads(json_)
    try:
        good_items = json_dic['mods']['itemlist']['data']['auctions']
    except:
        print('筛选条件过多,搜索不到该商品!')
        return 1
    good_list = []
    for good_item in good_items:
        goods = {
            'shop': good_item['nick'],  # 店铺名
            'title': good_item['raw_title'],  # 宝贝标题
            'price': good_item['view_price'],  # 价格
            'sales': good_item['view_sales'],  # 销量
            'freight': good_item['view_fee'],  # 运费
            'detail_url': good_item['detail_url'],  # 详情页
            'pic_url': good_item['pic_url']
        }
        good_list.append(goods)
    return good_list


def init():
    taobao = TaoBao()
    mysql = taobao.mysql
    init_goods = mysql.select(
        "select concat(label,name) from core_crawl_tb t1 inner join good_main t2 on t1.gid = t2.id where enabled =1 "
        "limit 30")
    random_good_index = random.randint(0, len(init_goods) - 1)
    chrome = taobao.start(init_goods[random_good_index])
    # chrome = taobao.start2()
    # 15s手动扫码
    taobao.login()
    print('登录成功:', chrome.current_url)
    # time.sleep(3)
    # 点击二手标签
    # taobao.tapSecondHand()
    # 最小化
    chrome.minimize_window()
    return taobao, chrome, mysql


def main(taobao, chrome, mysql, search_goods):
    # 数据日期
    etl_date = time.strftime("%Y%m%d", time.localtime())
    etl_time = time.strftime("%H:%M:%S", time.localtime())
    last_update = etl_date + ' ' + etl_time
    # 遍历宝贝标题检索数据
    for search_good in search_goods:
        pages = 2  # 按销量爬取2页
        taobao.data_by_search(etl_date, etl_time, chrome, search_good[0], 1, pages)
        mysql.update("update core_crawl_tb set finished = 0, last_update = '%s' where id in (select id from("
                     "select t1.id from core_crawl_tb t1 inner join good_main t2 on t1.gid = t2.id where concat("
                     "label,name)='%s')a) " % (last_update, search_good[0]))

    # 一次完整爬取结束后 所有爬取状态复位
    mysql.update('update core_crawl_tb set finished = 1')
    # mysql.closeDb()
    return 'over', chrome, mysql
