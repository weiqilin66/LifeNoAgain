import time

from service.firstVersion.half_auto_taobao import data_by_search
from service.taobao.bySearch import main as tb_main
from mythread.tb_thread import tbThread
from flask import Flask, request

app = Flask(__name__)
task=[]

@app.route('/do', methods=['GET', 'POST'])
def do():
    task_name = request.args['name']
    task.append(task_name)
    print(task_name)
    return 'success'



if __name__ == '__main__':
    app.run(host="0.0.0.0", port=8383, debug=True)
    res,chrome = tb_main()
    if res == 'over':
        print('初始化爬取结束--进入实时爬取--')
    thread_1 = tbThread(chrome)
    thread_1.start()
    thread_1.pause()
    while 1==1:
        if len(task)!=0:
            for task_name in task:
                data_by_search()
        # 3秒轮询一次
        time.sleep(3)


    # t_dance.join()  # 函数join()=> main主线程等t_dance线程结束才结束