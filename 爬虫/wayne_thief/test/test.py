import time

from util.my_mysql import MySql

mysql = MySql('localhost', 'root', 'root', 'vhr')

if __name__ == '__main__':
    etl_date = time.strftime("%Y-%m-%d", time.localtime())
    etl_time = time.strftime("%H:%M:%S", time.localtime())
    s= etl_date+' '+etl_time
    print(s)
