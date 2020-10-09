from util.mysql_util import MySql

mysql = MySql('localhost', 'root', 'root', 'vhr')


# 所有宝贝
def all_goods():
    return mysql.select("select concat('二手 ',label,name),gid from core_crawl_tb t1 inner join good_main t2"
                        " on t1.gid = t2.id where enabled =1 and finished=1")


# 热度T3 新卡 热度T2 优秀  热度T1 过时
def t_goods(T):
    return mysql.select("select concat('二手 ',label,name),gid from core_crawl_tb t1 inner join good_main t2 "
                        "on t1.gid = t2.id where enabled =1 and finished=1 and advance = %d" % T)


def ins_good_sales(gid, etl_date, index_total):
    mysql.insert("insert into good_sales(gid, etl_date, sales) values (%d,'%s',%d)" % (gid, etl_date, index_total))


def del_good_sales(gid, etl_date):
    mysql.delete("delete from  good_sales where gid =%d and etl_date = '%s'" % (gid, etl_date))



