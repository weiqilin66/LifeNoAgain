def demo():
    for x in range(1, 11):
        yield x
        print('到yield %d 那一行就停止 下次调用从yield下一行开始执行到下次yield' % x)


a = demo()
print(a)
print(next(a))

print('-----遍历所有--------')

for x in a:
    print(x)