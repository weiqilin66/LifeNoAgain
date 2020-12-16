import re
import os

"""
"""
path = ''
file_name = 'test/测试文档1.txt'
abs_path = path + file_name


# 读取文档
def read():
    text_array = []
    with open(abs_path, "r") as f:
        for line in f.readlines():
            # 去掉列表中每一个元素的换行符
            line = line.strip('\n')
            # 替换tab
            line = line.replace('\t',' ')
            # 去掉空行
            if line.strip(' ')== '':
                continue
            text_array.append(line)
    print(text_array)
    return text_array


# 读取列 字段
def catch_col(text_array):
    # 第一个select 和from之间是列
    first_sel =None
    first_from = None
    for i in range(0, len(text_array)):
        if 'select' in text_array[i] or 'SELECT' in text_array[i]:
            first_sel = i
        if 'from' in text_array[i] or 'FROM' in text_array[i]:
            first_from = i
        if first_sel is not None and first_from is not None:
            break
    print('--first_sel:', first_sel, ',first_from:', first_from)
    col_array = text_array[first_sel + 1:first_from]
    # print('col_array:', col_array)
    # col_array: ['aaa --AA', ',bb as bbx', ',cc--XX', ',case when xxx', 'else', '    end', ',ddd AS DD --OO', ',case when bb', 'else', '    end', ',ee']

    # 处理字段
    res_col = []
    for i in range(len(col_array)):
        # 首字段
        if i == 0:
            res_col.append(','+col_array[i])
            continue
        # 最后一行
        if i ==len(col_array)-1:
            res_col.append(col_array[i])
            break
        # 判断依据 每个字段都是以,开始 下一行也是,开头为单行字段
        if col_array[i].strip(' ')[0] == ',' and col_array[i + 1].strip(' ')[0] == ',':
            res_col.append(col_array[i])
        else:  # 判断下一行数据以,开始为止
            index = i+1
            tmp = col_array[i] +' '
            while 1 == 1:
                if index> len(col_array)-1:
                    break
                if col_array[index].strip(' ')[0] != ',':
                    tmp=tmp +col_array[index] +' '
                    col_array.pop(index)
                else:
                    break
            res_col.append(tmp)
            # res_col: [',aaa --AA', ',bb as bbx', ',cc--XX', ',case when xxx else     end ', ',ddd AS DD --OO', ',case when bb else     end ', ',ee']
    return res_col
# 格式化字段
def format_col(res_col):
    format_res = []
    format_res2 = []
    comment_arr=[]      # 中文注释
    create_arr=[]       # 建表
    format_res3 = []    # 逻辑
    # 去逗号空格
    for f_col in res_col:
        arr = f_col.split(',',maxsplit=1)
        format_res.append(arr[1])
    # print(format_res)
    # 去备注
    for res in format_res:
        v_arr = res.split('--')
        format_res2.append(v_arr[0])
        if len(v_arr)>1: # 有备注
            comment_arr.append(v_arr[-1].replace('\t',' ').strip(' '))
        else:
            comment_arr.append('')
    # print(format_res2)
    print('中文备注:',comment_arr)
    # 去别名取临时表字段名
    for l in format_res2:
        l_arr = l.upper().split(' AS ')
        format_res3.append(l_arr[0].replace('\t',' ').strip(' '))
        if len(l_arr)>1:
            create_arr.append(l_arr[-1].replace('\t',' ').strip(' '))
        else:
            if len(l_arr[0].split('.'))>1:
                create_arr.append(l_arr[0].split('.')[1].replace('\t',' ').strip(' '))
            else:
                create_arr.append(l_arr[0].replace('\t',' ').strip(' '))
    print('逻辑字段:',format_res3)
    print('目标表:',create_arr)

if __name__ == '__main__':
    res_col = catch_col(read())
    format_col(res_col)
    print('--AUTO BY WAYNE OVER--')
