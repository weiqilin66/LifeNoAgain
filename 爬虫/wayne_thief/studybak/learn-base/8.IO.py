import os

# 路径
print(os.path.exists('8.IO.py')) # true 当前路径下是否存在xx文件/文件夹
# dirName = '饭拍秀'
# if not os.path.exists(dirName):
#     os.mkdir(dirName)
# 把两个路径合成一个时，不要直接拼字符串，而要通过os.path.join()函数
# open 的文件不存在会自动创建
f = open('d:/test/io.txt', 'r')
try:
    f.read()
except ValueError as e:
    print(e)
else:
    print('no error')
finally:
    if f:
       f.close()
    print('end')

# 不用close
with open('d:/test/io.txt', 'r') as f2:
    f2.read()
# 调用read()会一次性读取文件的全部内容，如果文件有10G，内存就爆了，
# 要保险起见，可以反复调用read(size)方法，每次最多读取size个字节的内容。
# 另外，调用readline()可以每次读取一行内容，调用readlines()一次读取所有内容并按行返回list。因此，要根据需要决定怎么调用。
# 写入时,最好指定打开的编码格式 与写入的格式统一 默认是GBK 写中文乱码
with open('d:/test/io.txt', 'w', encoding='utf-8') as f3:
    try:
        f3.write('写入')
    finally:
        print('with open close自动')

# open的方式 r w a

# 写入
# string1 = json.dumps(self.items)  # dict转换成str 写入为二进制
# string1 = json.dumps(self.items, ensure_ascii=False)  # 写中文




