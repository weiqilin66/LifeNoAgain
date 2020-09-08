import threading
import time

"""思想:主线程下带多个子线程 实现唱歌又跳舞 使用thread 如果只使用一个线程(主)只能唱完歌再跳舞"""
"""面向过程"""
def sing():
    print('当前运行线程是%s' % threading.current_thread().name)
    for i in range(1,6):
        print('我唱')
        time.sleep(1)

def dance(a):
    print('当前运行线程是%s' % threading.current_thread().name)
    for i in range(1,6):
        print(a+'在跳舞')
        time.sleep(1)

def main():
    # 单线程顺序执行完sing()再执行dance()
    # sing()
    # dance()
    a = '孙悟空'
    # 创建 子线程同时运行    注意target不能写成sing() 否则t_sing会由主线程调用
    t_sing = threading.Thread(target=sing,name='唱歌')
    t_dance = threading.Thread(target=dance,name='跳舞',args=(a,))
    t_dance.start()
    t_sing.start()
    t_sing.join()
    t_dance.join()  # 函数join()=> main主线程等t_dance线程结束才结束
    print('%s线程结束' % threading.current_thread().name)




if __name__ == '__main__':
    main()