from bean.my_mysql import MySql

mysql = MySql('localhost', 'root', 'root', 'vhr')

if __name__ == '__main__':
    kw='NS宝可梦剑盾'
    index_total=66
    init_goods = mysql.select(
        "select concat(label,name) from core_crawl_tb t1 inner join good_main t2 on t1.gid = t2.id where enabled =1 limit 30")
    print(init_goods)

