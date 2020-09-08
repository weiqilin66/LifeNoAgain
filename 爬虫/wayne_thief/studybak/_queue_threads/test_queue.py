from queue import Queue
"""队列 控制线程顺序 锁 控制数据紊乱"""
# 创建队列
q = Queue(5)
# 存数据
q.put('yaoMing')
q.put('jone')
q.put('mike')
q.put('wayne')
q.put('tom')

# q.put('tom',False) # false 不等待,直接报错 true,3 等待3秒

print(q)

# 取数据 先进先出
print(q.get())
print(q.get())
print(q.get())
print(q.get())
print(q.get())
print(q.get(True,3))

# q.empty() 判断队列是否为空
# q.full() 是否已满
# q.qsize() 获取队列长度
