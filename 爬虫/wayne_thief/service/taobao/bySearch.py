import sched
import time
import tkinter.messagebox  # 弹窗库
from tkinter import *
from playsound import playsound
import re
import json
import pymysql
import random
from selenium import common
from bean.my_mysql import MySql2
from bean.my_selenium import MySelenium

""" 半自动实现 """
mp3_path = r'd:/菊花台.mp3'

class TaoBao(object):
    def __init__(self):     # 初始化参数
        self.chrome = MySelenium().chrome
        self.mysql = MySql2('localhost', 'root', 'root', 'vhr')

    # 初始化随机搜索宝贝
    def start(self,random_good):
        chrome = self.chrome
        url = 'https://s.taobao.com/'
        chrome.get(url)
        chrome.maximize_window()  # 窗口最大化方便扫码
        ser_input = chrome.find_elements_by_xpath("//input[@name='q']")[0]
        ser_input.send_keys(random_good)
        time.sleep(1)
        ser_btn = chrome.find_elements_by_xpath("//button")[0]
        ser_btn.click()
        return chrome

    # 回调登录
    def login(self):
        chrome = self.chrome
        if chrome.current_url.startswith('https://login'):
            # tkinter.messagebox.showerror('提示', '请登录')
            print('未登录...')
            time.sleep(10)
            self.login()

    # 插入数据库
    def info2mysql(self,good_list, etl_date, etl_time, kw):
        # 一个good_list是一个网页数据
        for good in good_list:
            shop = good['shop']  # 店铺名
            title = good['title']  # 宝贝标题
            price = float(good['price'])  # 价格
            if '万' in good['sales']:
                sales = float(good['sales'].strip('+万人付款收货')) * 10000  # 销量
            else:
                sales = int(good['sales'].strip('+人付款收货'))  # 销量
            if good['freight'] == '':  # 运费
                freight = 0
            else:
                freight = float(good['freight'])
            detail_url = good['detail_url']  # 详情页
            pic_url = good['pic_url']
            # sql语句
            del_sql = 'delete from goods where title = %s and etl_date = %s and shop =%s'%(title, etl_date, shop)
            self.mysql.delete(del_sql)
            insert_sql = """
            insert into goods(shop,title,price,sales,freight,etl_date,etl_time,kw,detail_url,img_url) VALUES(%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)
            """%(shop, title, price, sales, freight, etl_date, etl_time, kw, detail_url, pic_url)
            self.mysql.insert(insert_sql)

    # 搜索爬取数据
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
                # 价格100+
                # chrome.find_elements_by_xpath("//input[@class='J_SortbarPriceInput input']")[0].send_keys(100)
                # time.sleep(1)
                # chrome.find_elements_by_xpath("//button")[1].click()
                # time.sleep(random.randint(10, 15))
        except common.exceptions.WebDriverException as e:
            print('--webdriver异常: ', e)
            # playsound(mp3_path)
            tkinter.messagebox.showinfo('tip', 'webdriver异常')
            # print("关闭弹窗休息200s")
            # time.sleep(200)
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
        if good_list==1:
            return
        print('首页size: ', len(good_list))
        self.info2mysql(good_list, etl_date, etl_time, search_good)
        # 下一页
        if pages == 1:
            return
        for page in range(0, pages - 1):
            # 下一页按钮不可用 返回
            totalPage = chrome.find_elements_by_xpath("//div[@class='pager']//ul[@class='items']//li/a[@class='link']/span[@class='icon icon-btn-next-2-disable']")  # 只有一页
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
                print('翻页未搜索到: ', search_good)
                return
            json_ = json_ + '}}'
            # 分析json
            good_list = json2info(json_)
            print('第',page+2,'页size: ', len(good_list))
            # 写入数据库
            self.info2mysql(good_list, etl_date, etl_time, search_good)

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

def main():
    taobao = TaoBao()
    mysql = taobao.mysql
    search_goods = mysql.select('select name from tb_search where enabled =1')
    # print('爬取列表: ', search_goods)
    random_good = random.randint(0, len(search_goods) - 1)
    chrome = taobao.start(search_goods[random_good])
    # 15s手动扫码
    taobao.login()
    print('登录成功:', chrome.current_url)
    # 数据日期
    etl_date = time.strftime("%Y%m%d", time.localtime())
    etl_time = time.strftime("%H:%M:%S", time.localtime())
    # -----  销量排行  -----
    count = 0
    for search_good in search_goods:
        pages = 3
        if count > 40:  # 后续宝贝热度低
            pages = 2
        taobao.data_by_search(etl_date, etl_time, chrome, search_good, 1, pages)
        count = count + 1
        mysql.update('update tb_search set enabled = 0 where name = %s'%search_good)
        break
    mysql.update('update tb_search set enabled = 1')
    mysql.closeDb()
    return 'over',chrome


if __name__ == '__main__':
    main()
    print('-' * 30)
    print('爬取结束')
