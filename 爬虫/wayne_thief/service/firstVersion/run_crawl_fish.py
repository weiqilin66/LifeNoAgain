import time

from for_taobao_xianyu.crawl_fish import CrawlFish
from for_taobao_xianyu.service.my_appium import MyAppium
from for_taobao_xianyu.service.my_mysql import MySql2
from for_taobao_xianyu.service.my_selenium import MySelenium

phone = 'shark3'
if __name__ == '__main__':
    # 爬取猎人价
    switch_url = 'https://shop255640440.taobao.com/search.htm?orderType=&viewType=grid&keyword=switch%D3%CE%CF%B7&lowPrice=100&highPrice=600'
    ps4_url = 'https://shop255640440.taobao.com/search.htm?orderType=&viewType=grid&keyword=ps4%D3%CE%CF%B7&lowPrice=30&highPrice=600'
    login_url = 'https://login.taobao.com/'
    shop_name = '宁波老猎人电玩店'
    # chrome = MySelenium().chrome
    # tb = CrawlTB(chrome)
    # chrome.get(login_url)
    # tb.is_login()
    # chrome.get(switch_url)
    # # 初始精品策略
    # my_shop_goods = MySql2().query('select name from my_focus where enabled = 1',None)
    # for good in my_shop_goods:
    #     good_name = good[0]
    #     print('tb_crawl: ',good_name)
    #     tb.crawlShopGood(good_name,shop_name)
    # print('*'*20,'  tb_crawl结束  ','*'*20)
    # chrome.quit()

    ####################################################################################################################
    # 加工数据
    etl = MySql2()
    etl.commit('delete from fish_kw_price', None)
    my_focus_list = etl.query('select name,kw,label from my_focus where enabled = 1', None)
    for my_focus in my_focus_list:
        good_name = my_focus[0]
        kw = my_focus[1]
        label = my_focus[2]
        if kw is None:
            like_condition = '%' + good_name + '%'
            arg1 = label + good_name
        else:
            like_condition = '%' + my_focus[0] + '%' + my_focus[1] + '%'
            arg1 = label + '' + good_name + kw

        res = etl.query('select * from goods_shop where title like %s', like_condition)
        if res:
            price = res[0][6]
            etl.commit('insert into fish_kw_price(kw,price,enabled) values (%s,%s,1)', (arg1, price))

    ####################################################################################################################
    flist = etl.query("select * from fish_kw_price", None)
    print('fish_crawl列表:', flist)

    # 黑鲨3
    app = MyAppium('Android', '10', 'c45a530a', 'com.taobao.idlefish',
                   'com.taobao.fleamarket.home.activity.MainActivity')
    print('初始化完毕')

    cf = CrawlFish(app, phone)
    cf.initFish()
    cf.login()
    bak_first_text = ''
    for obj in flist:
        cf.search(obj)

        while True:
            els, pos = cf.getEls()

            # 根据第一个元素的text是否相同 判断是否全部分析完毕
            first_el_text = els[0].text
            if bak_first_text:
                if first_el_text == bak_first_text:
                    break  # break退出的是当前循环不会跳出所有循环
            bak_first_text = first_el_text

            # view 过滤
            res_taps, res_info = cf.explain_els(els, obj)
            # 点击符合规则的view
            k = 0
            if len(res_taps) != 0:
                for tap in res_taps:
                    app.act.tap(x=tap['x'], y=tap['y']).perform()
                    time.sleep(1)
                    cf.analyse(res_info[k], obj)
                    k = k + 1
                    print('>>>>>>>>>>>>>>>>>>>>>>>>>>view坐标', tap, '解析完成')
            else:
                print('本页面全部解析过,即将下滑刷新')
                time.sleep(1)

            # 一次只下拉刷新4个元素 多次测试让页面中只有6元素
            app.driver.swipe(330, pos['end_el']['y'] + 30, 330, pos['start_el']['y'], duration=10000)
            print('下滑加载完成')
            time.sleep(1)

    app.driver.close_app()
    app.driver.quit()
