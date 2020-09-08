
def delete_data(cursor, etl_date, shop):
    sel_sql = """
        delete from goods_shop where etl_date = %s and shop = %s
    """
    cursor.execute(sel_sql, (etl_date, shop[1]))


def updateShop(cursor, conn, shop):
    sql = """
        update shop set enable = 0 where name = %s
    """
    cursor.execute(sql, shop[1])
    conn.commit()


def UpdateAllShop(cursor, conn):
    sql = """
        update shop set enable = 1 
    """
    cursor.execute(sql)
    conn.commit()

# def read_goods_by_excel():
#     # 读取excel中的数据
#     # 第一步：打开工作簿
#     wb = openpyxl.load_workbook('D:\workSpaceXD\MyTarget\goods.xlsx')
#     # 第二步：选取表单
#     sh = wb['Sheet1']
#     # 第三步：读取数据
#     # 参数 row:行  column：列
#     ce = sh.cell(row=1, column=1)  # 读取第一行，第一列的数据
#     # print(ce.value)
#     # 按行读取数据 list(sh.rows)
#     # print(list(sh.rows)[1:])
#     # 按行读取数据，去掉第一行的表头信息数据
#     switch_list = []
#     ps4_list = []
#     search_goods = []
#     for cases in list(sh.rows)[1:]:
#         if cases[0].value is not None:
#             case_switch = 'switch ' + cases[0].value
#             switch_list.append(case_switch)
#             search_goods.append(case_switch)
#
#         if cases[2].value is not None:
#             case_ps4 = 'ps4 ' + cases[2].value
#             ps4_list.append(case_ps4)
#             search_goods.append(case_ps4)
#
#     # 关闭工作薄
#     wb.close()
#     return search_goods
# 爬取店铺所有商品
def crawl_all_f_shop(chrome, shop, cursor, conn, etl_date, etl_time):
    totalPage = chrome.find_elements_by_xpath("//span[@class='page-info']")
    if len(totalPage) == 0:
        time.sleep(10)
        return
    total = totalPage[0].text
    total = int(total.split('/')[1])
    error_str = []
    for page in range(1, total + 1):
        try:
            dl_list = chrome.find_elements_by_xpath("//dl//img")  # 多少张图就有多少宝贝 dl与宝贝数不一致
            if len(dl_list) == 0:
                print('网速较慢', 'slow--' * 10)
                chrome.refresh()
                time.sleep(random.randint(10, 15))
                dl_list = chrome.find_elements_by_xpath("//dl//img")
        except common.exceptions.WebDriverException as e:
            print('--webdriver异常: ', e)
            # playsound(mp3_path)
            tkinter.messagebox.showinfo('tip', 'webdriver异常')
            print("关闭弹窗休息200s")
            time.sleep(200)
            dl_list = chrome.find_elements_by_xpath("//dl//img")
        # 手动关闭滑块
        # ------------------------------------------------------------------------
        price_list = chrome.find_elements_by_xpath("//dl//span[@class='c-price']")
        title_list = chrome.find_elements_by_xpath("//dl//img")
        sales_list = chrome.find_elements_by_xpath("//dl//span[@class='sale-num']")
        detail_url = chrome.find_elements_by_xpath("//dl//a[@class='item-name J_TGoldData']")

        for i in range(0, len(dl_list) - 1):
            title = title_list[i].get_attribute('alt')
            ret = re.sub(r'<span class=H>', '', title)
            ret2 = re.sub(r'</span>', '', ret)
            if len(sales_list) == 0:
                good = {
                    'shop': shop[1],
                    'price': price_list[i].text,
                    'title': ret2,
                    'sales': 99999,
                    'detail_url': detail_url[i].get_attribute('href'),
                    'pic_url': title_list[i].get_attribute('src')
                }
            else:
                good = {
                    'shop': shop[1],
                    'price': price_list[i].text,
                    'title': ret2,
                    'sales': sales_list[i].text,
                    'detail_url': detail_url[i].get_attribute('href'),
                    'pic_url': title_list[i].get_attribute('src')
                }
            print(good)
            all2mysql(cursor, conn, good, etl_date, etl_time)
        print('--------爬取第%s' % page, '页结束---------')
        time.sleep(random.randint(10, 15))
        if page != total:
            try:
                next_btn = chrome.find_elements_by_xpath("//a[@class='J_SearchAsync next']")[0]
                next_btn.click()
            except common.exceptions.WebDriverException as e:
                print('--webdriver异常: ', e)
                # playsound(mp3_path)
                tkinter.messagebox.showinfo('tip', 'webdriver异常')  # 手动滑块
                print("关闭弹窗休息200s")
                time.sleep(200)
                next_btn = chrome.find_elements_by_xpath("//a[@class='J_SearchAsync next']")[0]
                next_btn.click()
            time.sleep(5)
    return error_str
# 爬取店铺所有
def all2mysql(cursor, conn, item, etl_date, etl_time):
    insert_sql = """
        insert into goods_shop(shop,price,title,sales,detail_url,img_url,etl_date,etl_time) values (%s,%s,%s,%s,%s,%s,%s,%s)
    """
    cursor.execute(insert_sql, (
        item['shop'], item['price'], item['title'], item['sales'], item['detail_url'], item['pic_url'], etl_date,
        etl_time))
    conn.commit()

