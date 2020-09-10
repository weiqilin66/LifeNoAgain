import os
import random
import re
import time
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.support.wait import WebDriverWait
from for_taobao_xianyu.entity.my_constants import my_constants
from for_taobao_xianyu.service.my_mysql import MySql2


class CrawlFish(object):
    def __init__(self, app,phone):
        self.driver = app.driver
        self.app = app
        self.phone = phone

    def search(self, obj):
        driver = self.driver
        app = self.app
        kw = obj[1]
        price = obj[2]
        print('开始检索: ', kw, ' 价格:', price)
        high_price = price - 20
        low_price = price - 50
        # 输入标题
        input_area = WebDriverWait(driver, 10, 1).until(
            lambda x: x.find_element_by_xpath('//*[@resource-id="com.taobao.idlefish:id/search_term"]'))
        input_area.send_keys(kw)
        WebDriverWait(driver, 10, 1).until(
            lambda x: x.find_element_by_xpath('//*[@resource-id="com.taobao.idlefish:id/search_button"]')).click()
        time.sleep(2)
        # 筛选
        WebDriverWait(driver, 10, 1).until(lambda x: x.find_elements_by_xpath('//*[@text="筛选"]'))[0].click()
        time.sleep(1)
        app.tapByPercentage(my_constants[self.phone]['最低价'])
        time.sleep(0.5)
        app.pressNum(low_price)
        app.tapByPercentage(my_constants[self.phone]['最高价'])
        time.sleep(0.5)
        app.pressNum(high_price)
        # 分辨率之外的点击 os.popen('adb shell input tap x y ')
        os.popen(my_constants[self.phone]['筛选确定'])
        time.sleep(1)

    # 获取当前页所有元素个数
    def getEls(self):
        driver = self.driver
        els = WebDriverWait(driver, 10, 1).until(
            lambda x: x.find_elements_by_xpath('//*[@class="android.widget.ScrollView"]/*[@class="android.view.View"]'))
        res_els = []
        print('-------------------------------  view总览  ---------------------------------------')
        for i in els:
            if i.text == '综合排序' or i.text == '信用优先' or i.text == '区域' or i.text == '筛选':
                continue
            print(i.text)
            # print(i.location,',',i.size)
            res_els.append(i)
        return res_els, {'start_el': res_els[0].location, 'end_el': res_els[4].location}

    # 分析els
    def analyse(self, info, obj):
        driver = self.driver
        kw = obj[1]
        # 详情页
        els = WebDriverWait(driver, 20, 1).until(
            lambda x: x.find_elements_by_xpath(
                '//*[@class="android.widget.ScrollView"]/*[@class="android.view.View"]'))
        seller_info = els[0].text
        detail = els[2].text

        # 我想要页面 获取最终价格
        WebDriverWait(driver, 10, 1).until(lambda x: x.find_element_by_xpath('//*[@text="我想要"]')).click()
        time.sleep(3)
        # 复杂嵌套view获取    失败备用方案点击立即购买分析购买页价格
        seller_info2 = WebDriverWait(driver, 10, 1).until(lambda x: x.find_elements_by_xpath(
            '//*[@resource-id="android:id/content"]//*[@class="android.view.View"]//*[@class="android.view.View"]//*[@class="android.view.View"]//*[@class="android.view.View"]//*[@class="android.view.View"]//*[@class="android.view.View"]'))
        # for i in seller_info2:
        #     print('---')
        #     print(i.text)
        try:
            end_price = float(seller_info2[3].text.strip('¥'))
        except:
            try:
                end_price = float(seller_info2[1].text.strip('¥'))
            except:
                try:
                    end_price = float(seller_info2[2].text.strip('¥'))
                except:
                    try:
                        end_price = float(seller_info2[4].text.strip('¥'))
                    except:
                        try:
                            end_price = float(seller_info2[5].text.strip('¥'))
                        except:
                            try:
                                end_price = float(seller_info2[6].text.strip('¥'))
                            except:
                                try:
                                    end_price = float(seller_info2[7].text.strip('¥'))
                                except:
                                    end_price = float(seller_info2[8].text.strip('¥'))

        # # 立即购买 付款页面
        # os.popen(constants[self.phone]['立即购买'])
        # # 最终价格获取
        # es = driver.find_elements_by_xpath(
        #     '//*[@class="android.view.View"]/*[@class="android.view.View"]/*[@class="android.view.View"]/*[@class="android.view.View"]')
        # end_price = float(es[9].text.strip('¥'))
        # os.popen(constants[self.phone]['返回'])
        title = info['标题']
        price = info['价格']
        high_price = info['最高价']
        view_text0 = info['view_text']
        mailing = '包邮' if end_price == price else '不包邮'

        if end_price <= high_price:
            # 留言内容 手动付款购买
            if 'switch' in kw:
                driver.set_clipboard_text(
                    my_constants['switch留言'][random.randint(0, len(my_constants['switch留言']) - 1)])
            elif 'ps' in kw:
                driver.set_clipboard_text(my_constants['ps留言'][random.randint(0, len(my_constants['ps留言']) - 1)])
            elif '中文' in kw:
                pass
            elif '港版' in kw:
                pass
            # 黏贴 想跟TA说点什么呢
            self.app.act.press(x=550, y=2269).wait(1500).release().perform()
            os.popen(my_constants[self.phone]['黏贴'])
            # os.popen(constants[self.phone]['发送'])

            time.sleep(2)

            etl_date = time.strftime("%Y%m%d", time.localtime())
            etl_time = time.strftime("%H:%M:%S", time.localtime())
            MySql2().commit(
                'insert into fish_stock(kw, title, price, mailing, user, detail, count, etl_date, etl_time,view_text) values(%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)'
                , (kw, title, end_price, mailing, seller_info, detail, 0, etl_date, etl_time, view_text0))

        os.popen(my_constants[self.phone]['返回'])
        time.sleep(0.5)
        os.popen(my_constants[self.phone]['返回'])
        time.sleep(1)

    # 筛选 view
    def explain_els(self, els, obj, high_price=0):

        print('---------------------  筛选view  -------------------------')
        price = obj[2]
        if high_price == 0:  # 没传入最高价默认猎人价-20
            high_price = price - 20

        # 分析点击位置
        view_size = len(els)
        if view_size > 6:
            print('eeeeeeee\nview数量大于6个  解析出错\neeeeeeeeeeee')
        taps = (my_constants[self.phone]['pos1'], my_constants[self.phone]['pos2'], my_constants[self.phone]['pos3'],
                my_constants[self.phone]['pos4'], my_constants[self.phone]['pos5'], my_constants[self.phone]['pos6'])
        taps = taps[0:view_size]

        # 页面跳转会导致取不到循环中的Element  分析中不可有页面跳转
        j = 0
        res_taps = []
        res_info = []
        for el in els:
            # 广告view过滤  (细选/淘宝广告)
            if re.search(r'￥', el.text) is None or (re.search(r'人付款', el.text) is not None):
                print('本页面第', j + 1, '个为广告view或细选,跳过')
                j = j + 1
                continue

            # 标题过滤
            title = re.compile(r'(.*?)\n￥', re.S).findall(el.text)[0]
            if (re.search(r'收', title) is not None) or (re.search(r'专拍', title) is not None) or (re.search(r'没游戏', title) is not None):
                print('本页面第', j + 1, '个标题包含 收/steam/没游戏/专拍 跳过 ')
                j = j + 1
                continue

            # 价格过滤
            price = float(re.compile(r'￥\n(.*?)\n', re.S).findall(el.text)[0])
            if price >= high_price:
                print('本页面第', j + 1, '个价格:', price, '高于我的最高回收价:', high_price, ',跳过')
                j = j + 1
                continue

            # 数据库过滤 (防止多次问候一个商品)
            address = re.compile(r'￥\n.*\n(.*?)\n', re.S).findall(el.text)[0]
            view_text0 = title + '#' + str(price) + '#' + address
            if MySql2().query("select id from fish_stock where view_text ='%s'" % view_text0,None):
                print('本页面第', j + 1, '个已留言过,数据库中存在记录,跳过')
                j = j + 1
                continue

            print('本页第', j + 1, '个  标题: ', title, '价格: ', price, '地址: ', address)
            info = {'标题': title, '价格': price, '地址': address, 'view_text': view_text0, '最高价': high_price}
            res_info.append(info)
            res_taps.append(taps[j])
            j = j + 1

        print('点击事件为', res_taps)
        return res_taps, res_info

    def initFish(self):
        driver = self.driver
        app = self.app
        # 确定协议
        sure = WebDriverWait(driver, 10, 1).until(lambda x: x.find_elements_by_id("com.taobao.idlefish:id/right_btn"))
        if len(sure) != 0:
            sure[0].click()
            time.sleep(8)
        cmds = my_constants[self.phone]['启动三连']
        if cmds:
            print('启动3连')
            for cmd in cmds:
                os.popen(cmd)
                time.sleep(1)
        try:
            WebDriverWait(driver, 10, 1).until(
                lambda x: x.find_elements_by_xpath('//*[@resource-id="com.taobao.idlefish:id/login_guide_bar"]'))
        except TimeoutException as e:
            print('已登录,准备退出登录')
            """
                等待有空改造使用元素获取
            """
            driver.find_element_by_xpath('//*[@text="我的"]').click()
            time.sleep(1)
            app.tapByPercentage(my_constants[self.phone]['设置'])
            driver.swipe(510, 552, 524, 1550, 200)
            time.sleep(1)
            app.tapByPercentage(my_constants[self.phone]['退出登录'])
            time.sleep(1)
            app.tapByPercentage(my_constants[self.phone]['确定退出'])
            time.sleep(1)
        # 返回首页重新进入
        driver.press_keycode(3)
        time.sleep(1.5)

    def login(self):
        app = self.app
        app.jump('com.taobao.idlefish', 'com.taobao.fleamarket.home.activity.MainActivity')
        WebDriverWait(app.driver, 30, 1).until(
            lambda x: x.find_element_by_xpath('//*[@resource-id="com.taobao.idlefish:id/login_guide_bar"]')).click()
        time.sleep(1)
        app.tapByPercentage(my_constants[self.phone]['支付宝登录'])
        time.sleep(4)
        app.tapByPercentage(my_constants[self.phone]['立即登录'])
        WebDriverWait(app.driver, 20, 1).until(
            lambda x: x.find_element_by_id('com.taobao.idlefish:id/root_c_view')).click()


