from bean.my_mysql import MySql

mysql = MySql('localhost', 'root', 'root', 'vhr')


# 所有宝贝
def all_goods():
    return mysql.select("select concat(label,name) from core_crawl_tb t1 inner join good_main t2 on t1.gid = t2.id "
                        "where enabled =1 and finished=1")


# 热度T3 新卡 热度T2 优秀  热度T1 过时
def t_goods(T):
    return mysql.select("select concat(label,name) from core_crawl_tb t1 inner join good_main t2 on t1.gid = t2.id "
                        "where enabled =1 and finished=1 and advance = %d" % T)
