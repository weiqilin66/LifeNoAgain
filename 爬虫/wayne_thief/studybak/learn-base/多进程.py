from multiprocessing import Pool
import os, time, random


def long_time_task(name):
    print('run task %s (%s) ' % (name, os.getpid()))
    start = time.time()
    time.sleep(random.random() * 30)
    end = time.time()
    print('task %s runs %0.2f second' % (name, (end - start)))


if __name__ == '__main__':
    print('parent process %s' % os.getpid())
    p = Pool(4)
    for i in range(5):
        p.apply_async(long_time_task, args=(i,))
    print('waiting for all subProcesses done')
    p.close()
    p.join()
    print('all done')

# 对Pool对象调用join()方法会等待所有子进程执行完毕，调用join()之前必须先调用close()，
# 调用close()之后就不能继续添加新的Process了。
#
# 请注意输出的结果，task 0，1，2，3是立刻执行的，而task 4要等待前面某个task完成后才执行，
# 这是因为Pool的默认大小在我的电脑上是4，因此，最多同时执行4个进程
