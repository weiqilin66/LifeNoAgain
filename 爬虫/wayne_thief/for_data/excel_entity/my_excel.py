import openpyxl
import xlrd
from xlutils.copy import copy

# 读取所有行数据
def catch_all(path):
    # 第一步：打开工作簿
    wb = openpyxl.load_workbook(path)
    # 第二步：选取表单
    sh = wb['Sheet1']
    # 第三步：读取数据
    # 参数 row:行  column：列
    ce = sh.cell(row=1, column=1)  # 读取第一行，第一列的数据
    # print(ce.value)
    # 按行读取数据 list(sh.rows)
    # print(list(sh.rows)[1:])
    # 按行读取数据，去掉第一行的表头信息数据
    data_arr = []
    for row in list(sh.rows):
        # 列数
        columns = len(row)
        # print(columns)
        col_arr=[]
        for i in range(0,columns):
            col = row[i].value     # 遍历取值
            col_arr.append(col)
        data_arr.append(col_arr)
    # 关闭工作薄
    wb.close()
    return data_arr

# 返回可修改wb实例
def load_excel_forUpdate(path):
    rb = xlrd.open_workbook(path)
    wb = copy(rb)
    return wb