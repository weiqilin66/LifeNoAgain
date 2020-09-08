from appium import webdriver
from appium.webdriver.common.touch_action import TouchAction

from for_taobao_xianyu.entity.my_constants import my_constants


class MyAppium(object):
    def __init__(self, platformName, platformVersion, deviceName, appPackage, appActivity):
        # 前置代码(启动参数)
        self.desired_caps = dict()
        self.desired_caps['platformName'] = platformName  # 平台名 /IOS
        self.desired_caps['platformVersion'] = platformVersion  # 平台版本 可只写大版本 5
        self.desired_caps['deviceName'] = deviceName  # 设备名 可乱写
        self.desired_caps['appPackage'] = appPackage  # 包名
        self.desired_caps['appActivity'] = appActivity  # 界面名
        # 输入中文
        self.desired_caps['unicodeKeyboard'] = True
        self.desired_caps['resetKeyboard'] = True
        # 链接appium客户端
        self.driver = webdriver.Remote('http://localhost:4723/wd/hub', self.desired_caps)
        self.act = TouchAction(self.driver)
        # 分辨率 默认1080x1920
        self.default_x = self.driver.get_window_size()['width']
        self.default_y = self.driver.get_window_size()['height']
        print('当前屏幕分辨率: ', self.default_x, ' * ', self.default_y)

    # 根据屏幕百分比返回坐标 (tuple1: x,y为百分比参数 weditor可快速获得)
    def tapByPercentage(self, tuple1, count=1):
        res_x = tuple1[0] * self.default_x
        res_y = tuple1[1] * self.default_y
        self.act.tap(x=res_x, y=res_y, count=count).perform()

    # 获取当前包名 界面名
    def getCurrentPackageUI(self):
        print('包名/界面名:', self.driver.current_package, '/', self.driver.current_activity)
        return self.driver.current_package, self.driver.current_activity

    # app中启动其他app 例如支付宝 注意界面名不用加.
    def jump(self, package, ui):
        print('跳转:', package, '/', ui)
        self.driver.start_activity(package, ui)

    def pressNum(self, num_str):
        for num in str(num_str):
            self.driver.press_keycode(my_constants['codeMap'][num])
