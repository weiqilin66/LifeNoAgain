import os
from multiprocessing import Process


def run_proc(name):
    print('run child process %s (%s)...' % (name, os.getpid()))


if __name__ == '__main__':
    print('the running process is ', os.getpid())
    p = Process(target=run_proc, args=('test',))
    p.start()
    p.join()
    print('child process end')
