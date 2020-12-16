import time
from selenium import webdriver

import tkinter.messagebox  # 弹窗库
import re
import json
import random
from selenium import common


# chrome_path = r'd:/driverAndPlugs/chromedriver.exe'
chrome_path = r'D:\webdriver/newest/chromedriver.exe'

class MySelenium2(object):
    def __init__(self):
        self.options = webdriver.ChromeOptions()
        self.options.add_experimental_option('excludeSwitches', ['enable-automation'])
        self.chrome = webdriver.Chrome(executable_path=chrome_path, options=self.options)

def do():
    chrome = MySelenium2().chrome
    chrome.get('http://128.64.214.11/Citrix/XDWeb/auth/login.aspx?CTX_MessageType=WARNING&CTX_MessageKey=NoUsableClientDetected')
    user = chrome.find_element_by_xpath("//input[@name='user']")
    password = chrome.find_element_by_xpath("//input[@name='password']")
    login = chrome.find_element_by_id('btnLogin')
    user.send_keys('09757672')
    time.sleep(0.5)
    password.send_keys('yfllsg19950611')
    time.sleep(0.5)
    login.click()
    time.sleep(1)
    i = 0
    while 1==1:
        try:
            cloud = chrome.find_element_by_xpath("//div[@class='delayedImageNone']")
        except:
            time.sleep(5)
            cloud = chrome.find_element_by_xpath("//div[@class='delayedImageNone']")

        cloud.click()
        i = i+1
        time.sleep(1)
        # error = chrome.find_elements_by_xpath('//p[@class="feedbackStyleError"]')
        # if len(error)>0:
        #     print('尝试登录',i,'次, 登录失败:',error[0].text)
        # else:
        #     tkinter.messagebox.showinfo(message='登录成功')
        #     # chrome.quit()
if __name__ == '__main__':
    # 定时任务
    # while True:
    #     hour = time.strftime("%H",time.localtime())
    #     print('当前时间:',hour)
    #     if hour=="08":
    #         break
    #     else:
    #         time.sleep(60*30)
    while 1==1:
        try:
            do()
        except:
            continue



