from collections.abc import Iterable

print("hello world")
list1 = ['java', 'python', 1, 2]
print(list1[1:3])
print(list1[-2:])
print(list1[::-1])
print(list1[:4:2])
print(list1[::3].pop())  # pop删除的是原来的list1

str1 = 'abctddgo11'
print(str1[1])
print('right to left', str1[:4:-2])  # step 为负数 则缺省startIndex为终点
print('left to right', str1[:4:2])
print('left to right', str1[:-4:1])  # abctdd
print('right to left', str1[:-4:-1])  # 11o
print(str1[2::1])
print(str1[2::-1])
print(str1[-12:])
print('----------------切片-----------------over')
print('复杂生成式--')
list2 = [x * x for x in range(1, 11)]
print(list2)
list3 = [x * y for x in range(3, 11) if x % 2 == 0 for y in range(3, 11)]
print(list3)

print('generator--')
list2 = (x * x for x in range(1, 11))
print(next(list2))
print(next(list2))


# for i in list2:
#    print(i)
def fun1(max):
    while 5 < max:
        yield max
        max = max - 1
        print('max=', max)


print(fun1(9))
for x in fun1(9):
    print(x)
print(next(fun1(9)))  # 函数这种方法不能取到下一个值
print(next(fun1(9)))
print(next(fun1(9)))
# 应该酱紫
for x in fun1(9):
    print(x)
    if x == 7:
        break


if isinstance(list2, Iterable):
    print('is iterable')
if isinstance({'a':1}, Iterable):
    print('is iterable')
if isinstance((1,2), Iterable):
    print('is iterable')
if isinstance('aaa', Iterable):
    print('is iterable')
