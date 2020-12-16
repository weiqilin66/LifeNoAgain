import os

date = '2020-07-31'
scheme = 'base_pro_20200731'


# 读取文档
def read(path):
    text_array = []
    with open(path, "r", encoding='utf-8') as f:
        for line in f.readlines():
            # 替换${TXNDATE},${BASE},${FIDI_PUB_OB}
            line = line.replace('${TXNDATE}', date)
            line = line.replace('${BASE}', scheme)
            line = line.replace('${FIDI_PUB_OB}', 'FIDI_PUB_OB')
            # line = line +'\n'
            text_array.append(line)
    return text_array


# 覆写
def write(text, path):
    with open(path, "w", encoding='utf-8') as f:
        f.writelines(text)


# 遍历文件夹
def walkFile(path):
    for root, dirs, files in os.walk(path):

        # root 表示当前正在访问的文件夹路径
        # dirs 表示该文件夹下的子目录名list
        # files 表示该文件夹下的文件list

        # 遍历文件
        for f in files:
            file = os.path.join(root, f)
            write(read(file), file)
            # 遍历所有的文件夹
        # for d in dirs:
        #     print(os.path.join(root, d))


if __name__ == '__main__':
    path = ''
    walkFile(path)
