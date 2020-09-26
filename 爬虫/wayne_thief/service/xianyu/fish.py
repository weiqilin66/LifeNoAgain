import os
import random
import tkinter
from util.mysql_util import MySql
import pymysql
from appium import webdriver as appWebDriver
import time
from appium.webdriver.common.touch_action import TouchAction
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.support.wait import WebDriverWait
import re
from util.wayne_re import *

# adb API
"""
模拟点击
    adb shell input tap x y
"""
# 元素操作
"""
当无法定位元素时,使用高级手势去点击输入

获取当前应用包名界面名 
    adb shell dumpsys window windows | findstr mFocusedApp

元素定位 基于当前屏幕可见范围
    xpath: 
        文本: //*[contains(text(),"￥")]
        id: resource-id(不唯一)
        class: ...
     调用java底层高级写法
    # driver.find_element_by_android_uiautomator('new UiSelector().className(\"android.widget.EditText\").textContains(\"最低价\")').send_keys(200)

元素等待()
    隐式:统一设置等待时间
        driver.implicitly_wait(10)
        后续所有找元素操作,都会10s时间等待元素出现,例:5s内出现了元素会立即执行该查找语句,第11s出现则报错(NoSuchElement...)
    显式:单独设置等待时间
        导包并创建对象wait = WebDriverWait(driver,5,1) 5s 每1秒都会去执行是否能找到元素,找不到报错(timeOut)
        使用 wait.until(lambda x:x.find_element_by_id("com.taobao.idlefish:id/right_btn"))    

元素位置和大小(返回dict) 左上角为基点
    element.location key: x, y
    element.size     key: width, height

获取到元素后,获取元素属性
    element.get_attribute(属性名)  
    特殊获取
        class:className 
        resource-id:resourceId 
        content-desc: name
        text文本值: text 
"""
# 手势
"""
    滑动:
        swipe:      duration =10000 无惯性 实际拉动不需要10秒
        (2个坐标的位置的移动,每次执行有小误差)
            start_x,start_y,    起点
            end_x,end_y,    终点
            duration    滑动时间ms,时间小滑动惯性越大展示效果越靠后
            driver.swipe(start_x ,start_y ,end_x, end_y ,duration)   

        scroll:
        (2个元素之间的移动,有惯性误差,且无法指定惯性大小)
            driver.scroll(origin_element,target_element)

        drag_and_drop:
        (2个元素之间的移动,没惯性误差)
            driver.drag_and_drop(origin_element,target_element)

    TouchAction      
    (创建对象 act = TouchAction(driver)  perform()提交结尾 )  
        轻敲:
        (区别click 可以点击任何位置,和任意次数,不是双击)
            act.tap(element).perform()
            act.tap(x=100, y=100,count=3).perform() 坐标位置点击3次 默认点击1次
        双击:
            按下抬起+等待+按下抬起
        长按:
        (可以用按下抬起等待拼接,可以用专用方法long_press)
            act.long_press(坐标/元素,duration=时间ms).perform()
        移动:
        (按下再移动,例如手势解锁)
            act.press(...).move_to(坐标/元素).move_to(...).perform()

        按下抬起:(长按黏贴使用此操作)
            act.press(坐标/元素).perform()  按下
            act.press(坐标/元素).release().perform()    抬起
        等待:
            act.wait(时间ms).perform()
            act.press(坐标/元素).wait(1000).release().perform() 按下1秒抬起 
"""
# 手机API
"""
截图
    driver.get_screenshot_as_file(存储路径)
呼出键盘
   # print('键盘列表: ',driver.available_ime_engines)
    # driver.activate_ime_engine(constants[phone]['输入法'])
    # driver.activate_ime_engine('io.appium.settings/.UnicodeIME')

按键
(driver.press_keycode())
    返回键: 4
    HOME: 3
    回车键: 23
复制黏贴

"""

phone = 'shark3'
chrome_path = r'd:/driverAndPlugs/chromedriver.exe'

# 坐标 （屏占比）
# 以手机具体十字位置为准,坐标边缘可能大于分辨率 使用os.popen()来点击
fish_constants = {
    # 黑鲨3
    'shark3': {
        '我的': (0.897, 0.957),
        '设置': (0.901, 0.04),
        '退出登录': (0.491, 0.951),
        '确定退出': (0.705, 0.558),
        '支付宝登录': (0.54, 0.66),
        '立即登录': (0.45, 0.396),
        '输入法': 'com.baidu.input_heisha/.ImeService',
        '最低价': (0.443, 0.438),
        '最低价2': 'adb shell input tap 468 534',
        '最高价': (0.796, 0.439),
        '最高价2': 'adb shell input tap 813 542',
        '筛选确定': 'adb shell input tap 790 2333',
        '双击间隔': 110,  # ms
        '上滑加载': (531, 2269, 540, 0),
        '启动三连': ('adb shell input tap 541 1537', 'adb shell input tap 544 1342', 'adb shell input tap 555 1260'),
        '长按聊天框': 'adb shell input swipe 550 2313 551 2314 1500',  # adb命令长按,差1的位移
        '单击聊天框': 'adb shell input tap 949 2314',
        '发送': 'adb shell input tap 949 2300',
        '立即购买': 'adb shell input tap 1037 341',
        '黏贴': 'adb shell input tap 149 2196',
        '返回': 'adb shell input tap 59 138',
        'pos1': {'x': 300, 'y': 730},
        'pos2': {'x': 800, 'y': 730},
        'pos3': {'x': 300, 'y': 1400},
        'pos4': {'x': 800, 'y': 1400},
        'pos5': {'x': 300, 'y': 2100},
        'pos6': {'x': 800, 'y': 2100},
    },
    '界面': {
        '包名': 'com.taobao.idlefish',
        '首页': 'com.taobao.fleamarket.home.activity.MainActivity',
        '搜索页': 'com.idlefish.flutterbridge.flutterboost.IdleFishFlutterActivity',
        '详情页': 'com.idlefish.flutterbridge.flutterboost.IdleFishFlutterActivity',
        '聊天页': 'com.idlefish.flutterbridge.flutterboost.IdleFishFlutterActivity',

    },
    'codeMap': {
        '0': 7,
        '1': 8,
        '2': 9,
        '3': 10,
        '4': 11,
        '5': 12,
        '6': 13,
        '7': 14,
        '8': 15,
        '9': 16,
        'home': 3,
        'back': 4,
    },
    'ps留言': ('盘无划痕吗 盒子有损坏吗 老铁', '老铁 盒子有损吗 盘有划痕吗'),
    'switch留言': ('老铁 卡带盒子无损吗', '卡带盒子有损坏吗 老铁'),
    '中文': '',
    '港版': '',
}


class MyAppium(object):
    def __init__(self, platformName, platformVersion, deviceName, appPackage, appActivity):
        # 前置代码(启动参数)
        self.desired_caps = dict()
        self.desired_caps['platformName'] = platformName  # 平台名 /IOS
        self.desired_caps['platformVersion'] = platformVersion  # 平台版本 可只写大版本 5
        self.desired_caps['deviceName'] = deviceName  # 设备名 可乱写
        self.desired_caps['appPackage'] = appPackage  # 包名
        self.desired_caps['appActivity'] = appActivity  # 界面名
        # 浮窗无法定位解决
        # self.desired_caps['automationName'] = 'uiautomator2'
        # 输入中文
        self.desired_caps['unicodeKeyboard'] = True
        self.desired_caps['resetKeyboard'] = True
        # 链接appium客户端
        self.driver = appWebDriver.Remote('http://localhost:4723/wd/hub', self.desired_caps)
        self.act = TouchAction(self.driver)
        # 分辨率 默认1080x1920
        self.default_x = self.driver.get_window_size()['width']
        self.default_y = self.driver.get_window_size()['height']
        print('当前屏幕分辨率: ', self.default_x, ' * ', self.default_y)

    # 重要方法 根据屏幕百分比返回坐标 (tuple1: x,y为百分比参数 weditor可快速获得)
    def tapByPercentage(self, tuple1, count=1):
        res_x = tuple1[0] * self.default_x
        res_y = tuple1[1] * self.default_y
        self.act.tap(x=res_x, y=res_y, count=count).perform()

    def double_tap(self, tuple1):
        res_x = tuple1[0] * self.default_x
        res_y = tuple1[1] * self.default_y
        self.act.press(x=res_x, y=res_y).release().perform().wait(
            fish_constants[phone]['双击间隔']).press(x=res_x, y=res_y).release().perform()

    # 获取当前包名 界面名
    def getCurrentPackageUI(self):
        print('包名/界面名:', self.driver.current_package, '/', self.driver.current_activity)
        return self.driver.current_package, self.driver.current_activity

    # app中启动其他app 例如支付宝 注意界面名不用加.
    def jump(self, package, ui):
        print('跳转:', package, '/', ui)
        self.driver.start_activity(package, ui)

    # 数字转换driver输入
    def pressNum(self, num_str):
        for num in str(int(num_str)):  # int()向下取整
            enterNum = fish_constants['codeMap'][num]
            self.driver.press_keycode(enterNum)


class CrawlFish(object):
    def __init__(self, app):
        self.driver = app.driver
        self.app = app

    # 后退
    def errorDetailBack(self):
        os.popen(fish_constants[phone]['返回'])
        print('详情不符合关键词')
        time.sleep(1)

    # 搜索+价格条件筛选
    def search(self, good):
        driver = self.driver
        kw = good[1]  # 标题
        price = good[2]  # 价格
        print('开始检索: ', kw, ' 价格:', price)

        high_price = price - 5
        low_price = price - 30
        # 输入标题
        input_area = WebDriverWait(driver, 10, 1).until(
            lambda x: x.find_element_by_xpath('//*[@resource-id="com.taobao.idlefish:id/search_term"]'))
        input_area.send_keys(kw)
        # 点击搜搜
        WebDriverWait(driver, 10, 1).until(
            lambda x: x.find_element_by_xpath('//*[@resource-id="com.taobao.idlefish:id/search_button"]')).click()
        time.sleep(2)
        # 筛选
        WebDriverWait(driver, 10, 1).until(lambda x: x.find_elements_by_xpath('//*[@text="筛选"]'))[0].click()
        time.sleep(1)
        # app.tapByPercentage(fish_constants[phone]['最低价'])
        os.popen(fish_constants[phone]['最低价2'])
        time.sleep(0.5)
        app.pressNum(low_price)
        # app.tapByPercentage(fish_constants[phone]['最高价'])
        os.popen(fish_constants[phone]['最高价2'])
        time.sleep(0.5)
        app.pressNum(high_price)
        # 分辨率之外的点击 os.popen('adb shell input tap x y ')
        os.popen(fish_constants[phone]['筛选确定'])
        time.sleep(1)

    # 获取当前页所有元素个数
    def getEls(self):
        driver = self.driver
        els = WebDriverWait(driver, 10, 1).until(
            lambda x: x.find_elements_by_xpath('//*[@class="android.widget.ScrollView"]/*[@class="android.view.View"]'))
        res_els = []
        print('-------------------------------  view总览  ---------------------------------------')
        for i in els:
            if i.text == '综合排序' or i.text == '综合' or i.text == '信用优先' or i.text == '信用' \
                    or i.text == '区域' or i.text == '筛选':
                continue
            print(i.text)
            # print(i.location,',',i.size)
            res_els.append(i)
        return res_els, {'start_el': res_els[0].location, 'end_el': res_els[4].location}

    # 点击事件进入 分析els
    def analyse(self, info, good):
        driver = self.driver
        kw = good[1]
        # 详情页
        els = WebDriverWait(driver, 10, 1).until(
            lambda x: x.find_elements_by_xpath(
                '//*[@class="android.widget.ScrollView"]/*[@class="android.view.View"]'))
        seller_info = els[0].text
        detail = els[2].text
        # 数据库详情过滤
        if good[4] is not None:  # enclude1
            if checkIgnoreCase(good[4], detail):
                self.errorDetailBack()
                return
        if good[5] is not None:  # enclude2
            if checkIgnoreCase(good[5], detail):
                self.errorDetailBack()
                return
        if good[6] is not None:  # enclude3
            if checkIgnoreCase(good[6], detail):
                self.errorDetailBack()
                return
        if good[7] is not None:  # include1
            if checkIgnoreCase(good[7], detail) is False:
                self.errorDetailBack()
                return
        if good[8] is not None:  # include2
            if checkIgnoreCase(good[8], detail) is False:
                self.errorDetailBack()
                return
        if good[9] is not None:  # include3
            if checkIgnoreCase(good[9], detail) is False:
                self.errorDetailBack()
                return

        # 我想要页面 获取最终价格
        WebDriverWait(driver, 10, 1).until(lambda x: x.find_element_by_xpath('//*[@text="我想要"]')).click()
        time.sleep(3)
        # 复杂嵌套view获取价格
        seller_info2 = WebDriverWait(driver, 10, 1).until(lambda x: x.find_elements_by_xpath(
            '//*[@resource-id="android:id/content"]//*[@class="android.view.View"]//*[@class="android.view.View"]//*[@class="android.view.View"]//*[@class="android.view.View"]//*[@class="android.view.View"]//*[@class="android.view.View"]'))
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
        # 以上嵌套view获取失败的替代方案: 点击立即购买获取价格
        # # 立即购买 付款页面
        # os.popen(constants[phone]['立即购买'])
        # # 最终价格获取
        # es = driver.find_elements_by_xpath(
        #     '//*[@class="android.view.View"]/*[@class="android.view.View"]/*[@class="android.view.View"]/*[@class="android.view.View"]')
        # end_price = float(es[9].text.strip('¥'))
        # os.popen(constants[phone]['返回'])

        title = info['标题']
        price = info['价格']
        high_price = info['最高价']
        view_text0 = info['view_text']
        mailing = '包邮' if end_price == price else '不包邮'

        if end_price <= high_price:
            # 留言内容 手动付款购买
            if 'switch' or 'NS' in kw:
                driver.set_clipboard_text(
                    fish_constants['switch留言'][random.randint(0, len(fish_constants['switch留言']) - 1)])
            elif 'ps' in kw:
                driver.set_clipboard_text(fish_constants['ps留言'][random.randint(0, len(fish_constants['ps留言']) - 1)])
            elif '中文' in kw:
                pass
            elif '港版' in kw:
                pass
            # 黏贴 留言
            self.app.act.press(x=550, y=2269).wait(1500).release().perform()  # 长按
            os.popen(fish_constants[phone]['黏贴'])  # os.popen(cmd)
            # os.popen(constants[phone]['发送'])
            time.sleep(2)
            etl_date = time.strftime("%Y%m%d", time.localtime())
            etl_time = time.strftime("%H:%M:%S", time.localtime())
            # 存储有留言的记录 用于后续的搜索过滤和查错
            MySql2().commit(
                sql='insert into fish_stock(kw, title, price, mailing, user, detail, count, etl_date, etl_time,'
                    'view_text) values(%s,%s,%s,%s,%s,%s,%s,%s,%s,%s) '
                , args=(kw, title, end_price, mailing, seller_info, detail, 0, etl_date, etl_time, view_text0))

        os.popen(fish_constants[phone]['返回'])
        time.sleep(0.5)
        os.popen(fish_constants[phone]['返回'])
        time.sleep(1)

    # 筛选 view
    def explain_els(self, els, obj, high_price=0):

        print('---------------------  筛选view  -------------------------')
        price = obj[2]
        high_price = price - 5
        # 分析点击位置
        view_size = len(els)
        if view_size > 6:
            tkinter.messagebox.showinfo('tip', 'code363--------滑动出错\nview数量大于6个')

        # 屏幕点击的6个坐标
        taps = (fish_constants[phone]['pos1'], fish_constants[phone]['pos2'], fish_constants[phone]['pos3'],
                fish_constants[phone]['pos4'], fish_constants[phone]['pos5'], fish_constants[phone]['pos6'])
        # 筛选坐标
        taps = taps[0:view_size]

        # 页面跳转会导致取不到循环中的Element  分析中不可有页面跳转
        j = 0  # 先分析出当前页所有的符合view
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
            if (re.search(r'收', title) is not None) or (re.search(r'steam', title) is not None) or (
                    re.search(r'专拍', title) is not None) or (re.search(r'没游戏', title) is not None):
                print('本页面第', j + 1, '个标题包含 收/steam/没游戏/专拍 跳过 ')
                j = j + 1
                continue

            # 价格
            price = float(re.compile(r'￥\n(.*?)\n', re.S).findall(el.text)[0])
            # if price >= high_price:
            #     print('本页面第', j + 1, '个价格:', price, '高于我的最高回收价:', high_price, ',跳过')
            #     j = j + 1
            #     continue

            # 数据库过滤 (防止多次问候一个商品) 可省略 不影响功能只提高性能
            address = re.compile(r'￥\n.*\n(.*?)\n', re.S).findall(el.text)[0]
            view_text0 = title + '#' + str(price) + '#' + address
            if MySql2().query("select id from fish_stock where view_text ='%s'" % view_text0):
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
        cmds = fish_constants[phone]['启动三连']
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
            app.tapByPercentage(fish_constants[phone]['设置'])
            driver.swipe(510, 552, 524, 1550, 200)
            time.sleep(1)
            app.tapByPercentage(fish_constants[phone]['退出登录'])
            time.sleep(1)
            app.tapByPercentage(fish_constants[phone]['确定退出'])
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
        app.tapByPercentage(fish_constants[phone]['支付宝登录'])
        time.sleep(4)
        app.tapByPercentage(fish_constants[phone]['立即登录'])
        WebDriverWait(app.driver, 20, 1).until(
            lambda x: x.find_element_by_id('com.taobao.idlefish:id/root_c_view')).click()


# 独有mysql封装
class MySql2(object):
    def __init__(self):
        self.conn = pymysql.connect('localhost', 'root', 'root', 'vhr')
        self.cursor = self.conn.cursor()

    def commit(self, sql, args):  # tuple参数
        self.cursor.execute(sql, args)
        self.conn.commit()

    def query(self, query_sql):
        self.cursor.execute(query_sql)
        return self.cursor.fetchall()


if __name__ == '__main__':
    mysql = MySql('localhost', 'root', 'root', 'vhr')
    # 获取要爬取的宝贝名称及平均价格
    sql = """select g.gid, concat(label,name),price,g.base,g.enclude1,g.enclude2,g.enclude3,g.include1,g.include2,
    g.include3 from core_crawl_tb c ,good_key_word g,good_main m ,good_stock k 
    where c.gid = g.gid and c.gid=m.id and c.gid = k.gid"""
    flist = mysql.select(sql)
    print('crawl列表:', flist)
    # 雷电模拟器
    # app = MyAppium('Android', '5', 'emulator-5554', 'com.taobao.idlefish',
    #                'com.taobao.fleamarket.home.activity.MainActivity'
    #                )
    # 黑鲨3
    app = MyAppium('Android', '10', 'c45a530a', 'com.taobao.idlefish',
                   'com.taobao.fleamarket.home.activity.MainActivity')
    print('初始化完毕')
    #
    cf = CrawlFish(app)
    cf.initFish()  # 启动三连
    cf.login()  # 支付宝登录

    bak_first_text = ''
    for obj in flist:
        cf.search(obj)
        while True:
            els, pos = cf.getEls()
            # 根据第一个元素的text是否相同 判断是否全部分析完毕，即上滑不再刷新
            first_el_text = els[0].text
            if bak_first_text:
                if first_el_text == bak_first_text:
                    break  # break退出的是当前所在循环不会跳出外循环
            bak_first_text = first_el_text

            # view 过滤得到所需view
            res_taps, res_info = cf.explain_els(els, obj)
            # 点击分析符合规则的view
            k = 0
            if len(res_taps) != 0:
                for tap in res_taps:
                    app.act.tap(x=tap['x'], y=tap['y']).perform()
                    time.sleep(1)
                    cf.analyse(res_info[k], obj)
                    k = k + 1
                    print('>>>>>>>>>>>>>>>>>>>>>>>>>>坐标', tap, '解析完成')
            else:
                print('本页面全部解析过,即将下滑刷新')
                time.sleep(1)

            # 一次只下拉刷新4个元素 多次测试让页面中只有6元素
            app.driver.swipe(330, pos['end_el']['y'] + 30, 330, pos['start_el']['y'], duration=10000)
            print('下滑加载完成')
            time.sleep(1)

    app.driver.close_app()
    app.driver.quit()
