import os
import sys

def read_goods_by_excel():
    # 第一步：打开工作簿
    wb = openpyxl.load_workbook('D:\workSpaceXD\MyTarget\goods.xlsx')
    # 第二步：选取表单
    sh = wb['Sheet1']
    # 第三步：读取数据
    # 参数 row:行  column：列
    ce = sh.cell(row=1, column=1)  # 读取第一行，第一列的数据
    # print(ce.value)
    # 按行读取数据 list(sh.rows)
    # print(list(sh.rows)[1:])
    # 按行读取数据，去掉第一行的表头信息数据
    switch_list = []
    ps4_list = []
    search_goods = []
    for cases in list(sh.rows)[1:]:
        switch_search = cases[0].value
        if switch_search is not None:
            if switch_search == 0:
                continue
            case_switch = 'switch二手游戏 ' + switch_search
            switch_list.append(case_switch)
            search_goods.append(case_switch)
        ps4_search = cases[2].value
        if ps4_search is not None:
            if ps4_search == 0:
                continue
            case_ps4 = 'ps4二手游戏 ' + ps4_search
            ps4_list.append(case_ps4)
            search_goods.append(case_ps4)
    # 链接mysql
    conn = pymysql.connect('localhost', 'root', 'root', 'vhr')
    # 创建游标
    cursor = conn.cursor()

    init_data(cursor, conn,search_goods)

    # 关闭工作薄
    wb.close()
    print("搜索关键字导入成功!")
    return search_goods


def init_data(cursor, conn, search_goods):
    del_sql = """
        delete from tb_search 
    """
    cursor.execute(del_sql)
    conn.commit()
    for goodName in search_goods:
        insert_sql = """
            insert into tb_search(name, type, enabled) values(%s,%s,%s)
        """
        cursor.execute(insert_sql, (goodName, 'info', 1))
    conn.commit()


if __name__ == '__main__':
    # current_directory = os.path.dirname(os.path.abspath(__file__))
    # root_path = os.path.abspath(os.path.dirname(current_directory) + os.path.sep + ".")
    # last_path = os.path.abspath(os.path.dirname(root_path) + os.path.sep + ".")
    # sys.path.append(last_path)
    import openpyxl
    import pymysql
    read_goods_by_excel()
