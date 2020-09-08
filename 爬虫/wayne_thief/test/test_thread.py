import time

from mythread.my_thread import MyThread

if __name__ == '__main__':
    a = MyThread()
    a.start()
    time.sleep(3)
    a.pause()
    time.sleep(3)
    a.restart()
    time.sleep(3)
    a.pause()
    time.sleep(2)
    a.stop()