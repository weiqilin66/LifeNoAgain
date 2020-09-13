import time
from service.taobao.bySearch import main as tb_main
from selenium import webdriver

from flask import Flask, request
app = Flask(__name__)
task=[]
chromes=[]
@app.route('/do', methods=['GET', 'POST'])
def do():
    task_name = request.args['name']
    task.append(task_name)
    print(task)
    print('----')
    print(chromes[0])
    chrome = chromes[0]
    print(totalPage)
    return totalPage

@app.route('/clear', methods=['GET', 'POST'])
def clear():
    task.clear()
    return 'success'

@app.route('/go',methods=['GET','POST'])
def go():
    res,chrome = tb_main()
    chromes.append(chrome)
    if res=='over':
        return 'success'




# 测试失败
# 整合selenium flask失败
# 方案2 新的webdriver保存cookie
# 每次就可以启动一个新的webdriver
if __name__ == '__main__':
    app.run(host="0.0.0.0", port=8383, debug=False)     # false 解决2个selenium实例
    res,chrome = tb_main()
    while 1==1:
        totalPage = chrome.find_elements_by_xpath("//div[@class='pager']//ul[@class='items']//li/a[@class='link']/span[@class='icon icon-btn-next-2-disable']")  # 只有一页
        print(totalPage)
        time.sleep(20)

