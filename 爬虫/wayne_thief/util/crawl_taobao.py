import json
import random
import time
import tkinter
from selenium.webdriver.support.wait import WebDriverWait
from util.my_selenium import MySelenium
from util.mysql_util import MySql


class TaoBao(object):
    def __init__(self):     # 初始化参数
        self.chrome = MySelenium().getTbInstance()
        self.mysql = MySql()

    # 回调判断是否登录
    def is_login(self):
        if self.chrome.current_url.startswith('https://login'):
            print('未登录...')
            time.sleep(10)
            self.is_login()

    def searchGood(self):
        pass


    # 爬取店铺所有商品待测
    def crawlShop(self):
        totalPage = self.chrome.find_elements_by_xpath("//span[@class='page-info']")
        if len(totalPage) == 0:
            time.sleep(10)
        return
        total = int(totalPage[0].text.split('/')[1])
        for page in range(1, total + 1):
            try:
                dl_list = chrome.find_elements_by_xpath("//dl//img")  # 多少张图就有多少宝贝 dl与宝贝数不一致
                if len(dl_list) == 0:
                    print('网速较慢', 'slow--' * 10)
                    chrome.refresh()
                    time.sleep(random.randint(10, 15))
                    dl_list = chrome.find_elements_by_xpath("//dl//img")
            except common.exceptions.WebDriverException as e:
                print('--webdriver异常: ', e)
                # playsound(mp3_path)
                tkinter.messagebox.showinfo('tip', 'webdriver异常')
                print("关闭弹窗休息200s")
                time.sleep(200)
                dl_list = chrome.find_elements_by_xpath("//dl//img")
        # 手动关闭滑块
        # ------------------------------------------------------------------------
        price_list = chrome.find_elements_by_xpath("//dl//span[@class='c-price']")
        title_list = chrome.find_elements_by_xpath("//dl//img")
        sales_list = chrome.find_elements_by_xpath("//dl//span[@class='sale-num']")
        detail_url = chrome.find_elements_by_xpath("//dl//a[@class='item-name J_TGoldData']")
        for i in range(0, len(dl_list) - 1):
            title = title_list[i].get_attribute('alt')
            ret = re.sub(r'<span class=H>', '', title)
            ret2 = re.sub(r'</span>', '', ret)
            if len(sales_list) == 0:
                good = {
                    'shop': shop[1],
                    'price': price_list[i].text,
                    'title': ret2,
                    'sales': 99999,
                    'detail_url': detail_url[i].get_attribute('href'),
                    'pic_url': title_list[i].get_attribute('src')
                }
            else:
                good = {
                    'shop': shop[1],
                    'price': price_list[i].text,
                    'title': ret2,
                    'sales': sales_list[i].text,
                    'detail_url': detail_url[i].get_attribute('href'),
                    'pic_url': title_list[i].get_attribute('src')
                }
            print(good)
            etl_date = time.strftime("%Y%m%d", time.localtime())
            etl_time = time.strftime("%H:%M:%S", time.localtime())
            self.mysql.commit(
                sql='insert into goods_shop(shop,price,title,sales,detail_url,img_url,etl_date,etl_time) values (%s,%s,%s,%s,%s,%s,%s,%s)'
                , args=(good['shop'], good['price'], good['title'], good['sales'], good['detail_url'], good['pic_url'],
                        etl_date, etl_time))
        print('--------爬取第%s' % page, '页结束---------')
        time.sleep(random.randint(10, 15))
        if page != total:
            try:
                next_btn = chrome.find_elements_by_xpath("//a[@class='J_SearchAsync next']")[0]
                next_btn.click()
            except common.exceptions.WebDriverException as e:
                print('--webdriver异常: ', e)
                # playsound(mp3_path)
                tkinter.messagebox.showinfo('tip', 'webdriver异常')  # 手动滑块
                print("关闭弹窗休息200s")
                time.sleep(200)
                next_btn = chrome.find_elements_by_xpath("//a[@class='J_SearchAsync next']")[0]
                next_btn.click()
            time.sleep(5)

    # 爬取店铺指定商品
    def crawlShopGood(self, goodName, shopName):
        goods =[]
        try:
            WebDriverWait(self.chrome, 10, 1).until(lambda x: x.find_element_by_xpath("//input[@name='q']")).send_keys(goodName)
            WebDriverWait(self.chrome, 10, 1).until(lambda x: x.find_element_by_xpath("//button[2]")).click()
            # chrome.find_elements_by_xpath("//input[@name='q']")[0].send_keys(goodName)
            # chrome.find_elements_by_xpath("//button[2]")[0].click()
            time.sleep(1)
        except Exception as e:
            print('--webdriver异常: ', e)
            tkinter.messagebox.showinfo('tip', 'webdriver异常')
            self.chrome.find_elements_by_xpath("//input[@name='q']")[0].send_keys(goodName)
            self.chrome.find_elements_by_xpath("//button[2]")[0].click()
            time.sleep(1)
        try:
            # 搜出多个宝贝
            good_length = len(self.chrome.find_elements_by_xpath("//div[@class='shop-hesper-bd grid']//a[@class='item-name J_TGoldData']"))
            if good_length>0:
                for i in range(0,good_length):
                    good = {
                        'shop': shopName,
                        'price': self.chrome.find_elements_by_xpath(
                            "//div[@class='shop-hesper-bd grid']//div[@class='cprice-area']/span[2]")[i].text,
                        'title': self.chrome.find_elements_by_xpath(
                            "//div[@class='shop-hesper-bd grid']//a[@class='item-name J_TGoldData']")[i].text,
                        'sales':
                            self.chrome.find_elements_by_xpath("//div[@class='shop-hesper-bd grid']//div[@class='sale-area']/span")[
                                i].text,
                        'detail_url': self.chrome.find_elements_by_xpath(
                            "//div[@class='shop-hesper-bd grid']//a[@class='item-name J_TGoldData']")[i].get_attribute('href'),
                        'pic_url':
                            self.chrome.find_elements_by_xpath("//div[@class='shop-hesper-bd grid']//dt[@class='photo']//img")[
                                i].get_attribute('src'),
                    }
                    print('  %s' % good)
                    goods.append(good)
            else:
                print('未获取到: %s' % goodName)
                good = {
                    'shop': shopName,
                    'price': 0,
                    'title': '无此商品',
                    'sales': 0,
                    'detail_url': 'null',
                    'pic_url': 'null'
                }
                goods.append(good)
        except:
            print('未获取到: %s' % goodName)
            good = {
                'shop': shopName,
                'price': 0,
                'title': '无此商品',
                'sales': 0,
                'detail_url': 'null',
                'pic_url': 'null'
            }
            goods.append(good)
        etl_date = time.strftime("%Y%m%d", time.localtime())
        etl_time = time.strftime("%H:%M:%S", time.localtime())
        for good in goods:
            MySql().commit(
                'insert into goods_shop(shop,price,title,sales,detail_url,img_url,etl_date,etl_time,kw) values (%s,%s,%s,%s,%s,%s,%s,%s,%s)'
                , (
                    good['shop'], good['price'], good['title'], good['sales'], good['detail_url'], good['pic_url'], etl_date,
                    etl_time, goodName)
            )
        time.sleep(random.randint(10,15))

    # 解析json数据
    def json2info(self,json_):
        json_dic = json.loads(json_)
        try:
            good_items = json_dic['mods']['itemlist']['data']['auctions']
        except:
            print('筛选条件过多,搜索不到该商品!')
            return 1
