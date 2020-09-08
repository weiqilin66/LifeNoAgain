print('hello world !')
# name = input()
# print('my name is', name)  # 逗号表示一个空格
print(1 + int('2'))
print(1.3e10)
print('''line1
line2
line3''')
code = '%s %d %.3f'
print(code % ('x', 1, 1.23232))
print('列表', '-----------------------')
list = ['kk', 1, 1.3]
print(len(list))
print('last', list[-1])
list.append('last')
print(list[3])  # 下标0开始
list.insert(3, 'beforeLast')
print(list[-2])
print(list.pop(-3)) # 输出了删除的元素
print(list.pop())
tupleIns = ('ss', 1)
print(tupleIns,'--------------------------------------------')

list2 = range(11)
print(list2)
if len(list2)==5 :
    pass
else:
    print(len(list2))
for x in tupleIns :
    print(x)
while len(tupleIns)<len(list):
    print(1)
    if 1<2 :
     break
dict ={'a':1,'b':2}
print(len(dict))
print(dict.get('a'))
print(dict['a'])
dict.pop('a')
print(dict)
dict['c'] = 3
print(dict)





