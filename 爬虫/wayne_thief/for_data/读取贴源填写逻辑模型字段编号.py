from for_data.excel_entity.my_excel import *

if __name__ == '__main__':
    path = r'C:\Users\weiqilin66\Desktop\tmp.xlsx'
    data = catch_all(path)
    print('贴源数据总行数:', len(data))
    # 遍历数据
    for d in data:
        # 英文字段
        eng_col = d[1]
        # 编号字段
        code_col = d[1]

    path2 = path
    wb=load_excel_forUpdate(path2)
    # 第二步：选取表单
    sh = wb['Sheet1']
    # 第三步：读取数据
    # 参数 row:行  column：列
    ce = sh.cell(row=1, column=1)  # 读取第一行，第一列的数据
    print(ce.value)