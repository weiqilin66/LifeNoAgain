import re
import os

"""
功能: 读取文本表结构数据 自动生成插入语句
tip:
插入数据前加@@表名
"""
# path = 'C:/Users/weiqilin66/Desktop/'
path = 'C:/Users/Administrator/Desktop/'
file_name = '计划账套测试数据.pl'
abs_path = path + file_name

"""
读取文档
"""


def read():
    text_array = []
    with open(abs_path, "r") as f:
        for line in f.readlines():
            line = line.strip('\n')  # 去掉列表中每一个元素的换行符
            text_array.append(line)
    return text_array


if __name__ == '__main__':
    res = ''
    texts = read()
    create_start_info = []
    create_end_index = []
    data_index = []
    line_index = 1
    tables = []

    with open(path + "wayne_auto_ins.txt", "w") as f:
        for text in texts:
            if 'create' in text or 'CREATE' in text:    # 获取表结构开始行
                table_name = text.split('.')[1].strip('(')
                scheme = text.split('.')[0].split(' ')[2]
                create_start_info.append((line_index, scheme, table_name))   # [(行,scheme,表名)]
            if ');' in text:    # 表结构结束行
                create_end_index.append(line_index)
            if '@@' in text:    # 数据开始行包含表名
                table = text.strip('@@;')
                data_index.append((line_index, table))
                tables.append(table)
            line_index = line_index + 1

        # print(create_index)  # 表名行信息
        # print(end_index)  # 表名结束行
        # print(data_index)  # 数据表对应及开始行
        c_index = 0
        for c in create_start_info:
            table_start_line = c[0]     # 表结构开始行
            end_col_index = create_end_index[c_index]  # 表结构结束行
            sql = 'INSERT INTO ' + c[1] + '.' + c[2] + '(%s)VALUES('
            cols = ''
            remark = '--' + c[1] + "." + c[2]       #注释
            print(remark)
            f.write(remark)
            for data in data_index:
                if c[2] == data[1]:
                    # 数据开始行
                    data_start_line = data[0]
                    for j in range(data_start_line, len(texts)):
                        if '@@' in texts[j]:
                            end_data_index = j
                            break
                        else:
                            end_data_index = len(texts)
                    values_array = texts[data_start_line:end_data_index]  # 值数组
                    break
            types = []
            # 拼接表字段
            for i in range(table_start_line, end_col_index - 1):
                col = texts[i].strip(' ')
                col_text = col.split('\t')[0].strip('"')    # 字段值
                col_type = col.split('\t')[1]   # 字段类型
                types.append(col_type)
                cols = cols + col_text + ','
            sql = sql % cols[:-1]
            # 拼接value值
            for value in values_array:
                val_length = len(value)
                if val_length == 0:     # 空行
                    continue
                value_index = 0
                values = ''
                value = value.split(';')
                for v in value:
                    type = types[value_index]
                    v2 = "'" + v + "'"  # 默认字符类型
                    if 'DATE' in type or 'date' in type:
                        v2 = "TO_DATE('" + v + "',YYYY-MM-DD)"
                    elif 'NUM' in type or 'num' in type:
                        v2 = v.strip("'")
                    if '<NULL>' in v2:
                        v2 = 'NULL'
                    value_index = value_index + 1
                    values = values + v2 + ','
                res = sql + values[:-1] + ');'
                print(res)
                f.write(res + '\n')  # 自带文件关闭功能，不需要再写f.close()
            c_index = c_index + 1
