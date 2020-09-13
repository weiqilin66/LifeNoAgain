from bean.my_mysql import MySql2

mysql = MySql2('localhost', 'root', 'root', 'vhr')

if __name__ == '__main__':
    mysql.insert("""
    insert into goods (title)values ('test')
    """
                 )

