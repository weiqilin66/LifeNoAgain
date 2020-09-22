from selenium import webdriver
"""
    selenium封装
"""

chrome_path = r'd:/driverAndPlugs/chromedriver.exe'

class MySelenium(object):
    def __init__(self):
        self.options = webdriver.ChromeOptions()
        # 不加载图片
        self.options.add_experimental_option("prefs", {"profile.managed_default_content_settings.images": 2})
        # 设置为开发者模式，防止被各大网站识别出来使用了Selenium
        # window.navigator.webdriver判断是否undefined
        self.options.add_experimental_option('excludeSwitches', ['enable-automation'])
        self.chrome = webdriver.Chrome(executable_path=chrome_path, options=self.options)

    def getTbInstance(self):
        chrome = self.chrome
        url = 'https://login.taobao.com/'
        chrome.get(url)
        chrome.maximize_window()  # 窗口最大化方便扫码
        return chrome