import re
import os

"""
插入数据前加@@表名
不要有空行
"""
path = 'C:/Users/Administrator/Desktop/'
file_name = 'wayne.txt'
abs_path = path + file_name


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
    create_index = []
    end_index = []
    data_index = []
    line_index = 1
    tables = []
    # filename = path + 'wayne_auto_ins.txt'
    # print(filename)
    # if not os.path.exists(filename):
    #     os.mknod(filename)

    with open(path+"wayne_auto_ins.txt","w") as f:
        for text in texts:
            if 'create' in text or 'CREATE' in text:
                table_name = text.split('.')[1].strip('(')
                scheme = text.split('.')[0].split(' ')[2]
                create_index.append((line_index, scheme, table_name))
            if ');' in text:
                end_index.append(line_index)
            if '@@' in text:
                table = text.strip('@@')
                data_index.append((line_index, table))
                tables.append(table)
            line_index = line_index + 1

        # print(create_index)
        # print(end_index)
        # print(data_index)
        c_index = 0
        for c in create_index:
            clo_index = c[0]
            end_col_index = end_index[c_index]
            sql = 'INSERT INTO ' + c[1] + '.' + c[2] + '(%s)VALUES('
            cols = ''
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
            types = []
            for i in range(clo_index, end_col_index - 1):
                col = texts[i]
                col_text = col.split(' ')[0]
                col_type = col.split(' ')[1]
                types.append(col_type)
                cols = cols + col_text + ','
            sql = sql % cols[:-1]
            for value in values_array:
                value_index = 0
                values = ''
                value = value.split(';')
                for v in value:
                    type = types[value_index]
                    if type == 'DATE' or type == 'date':
                        v = "TO_DATE('" + v + "',YYYY-MM-DD)"
                    if 'NUM' in type:
                        v = v.strip("'")
                    value_index = value_index + 1
                    values = values + v + ','
                res = sql + values[:-1] + ');'
                print(res)
                f.write(res+'\n')  # 自带文件关闭功能，不需要再写f.close()
            c_index = c_index + 1
