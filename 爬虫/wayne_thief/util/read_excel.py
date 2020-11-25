import os
import sys

path = "C:/Users/weiqilin66/Desktop/"
path = path+'tmp2.xlsx'
tablename = 'T0062_INTLG_TS_CMPN_I1_H'
def read_goods_by_excel():
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
    s = "INSERT INTO BASE."+tablename+" VALUES("
    e=');'
    res=''
    search_goods = []
    for cases in list(sh.rows)[0:]:
        columns = len(cases)
        # print(columns)
        sql=''
        for i in range(0,columns):
            col = cases[i].value
            if col is None or col=='<NULL>':
                sql = sql + "NULL" +","
                continue
            if i ==4 or i==5 or i==8 or i==9 or i==15 or i==18 or i==20:
            # if i ==8 or i==10 or i==13 or i==15:
                if col is not None:
                    col = "to_date('"+str(col)+"','YYYY-MM-DD')"
                else:
                    col = 'NULL'

            else:
                col = "'"+str(col)+"'"

            sql = sql + col +","
        sql = sql[0:-1]
        res=s+sql+e
        print(res)

    # 关闭工作薄
    wb.close()
    return search_goods


if __name__ == '__main__':
    # current_directory = os.path.dirname(os.path.abspath(__file__))
    # root_path = os.path.abspath(os.path.dirname(current_directory) + os.path.sep + ".")
    # last_path = os.path.abspath(os.path.dirname(root_path) + os.path.sep + ".")
    # sys.path.append(last_path)
    import openpyxl
    import pymysql
    read_goods_by_excel()
