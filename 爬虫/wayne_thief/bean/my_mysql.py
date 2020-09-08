import pymysql


class MySql2(object):
    def __init__(self,host,username,password,service):
        self.conn = pymysql.connect(host, username, password, service)
        # self.conn = pymysql.connect('localhost', 'root', 'root', 'vhr')
        self.cursor = self.conn.cursor()

    def commit(self, sql, args):  # tuple参数
        if args:
            self.cursor.execute(sql, args)
        else:
            self.cursor.execute(sql)
        self.conn.commit()

    def query(self, query_sql,args):
        if args:
            self.cursor.execute(query_sql,args)
        else:
            self.cursor.execute(query_sql)

        return self.cursor.fetchall()
    # DbHandle = DataBaseHandle('127.0.0.1','adil','helloyyj','AdilTest',3306)
    # DbHandle.insertDB('insert into test(name) values ("%s")'%('FuHongXue'))
    # DbHandle.selectDb('select * from test')
    # DbHandle.updateDb('update test set name = "%s" where sid = "%d"' %('YeKai',22))
    # DbHandle.deleteDB('delete from test where sid > "%d"' %(25))
    # DbHandle.closeDb()
    # # 此处执行 数据库插入，将 图片名称、url 插入到数据库   注意 这里的 values('占位符 一定要用 引号引起来)
    # sql = "insert into JdwSpider(image_name,image_url) values ('%s','%s')" % (file_name,image_url)
    # DbHandle.insertDB(sql)
    def insert(self,sql):
        """ 插入数据库操作 """
        self.cursor = self.conn.cursor()
        try:
            # 执行sql
            self.cursor.execute(sql)
            # tt = self.cursor.execute(sql)  # 返回 插入数据 条数 可以根据 返回值 判定处理结果
            # print(tt)
            self.conn.commit()
        except:
            # 发生错误时回滚
            self.conn.rollback()
        finally:
            self.cursor.close()



    def delete(self,sql):
        """ 操作数据库数据删除 """
        self.cursor = self.conn.cursor()
        try:
            # 执行sql
            self.cursor.execute(sql)
            # tt = self.cursor.execute(sql) # 返回 删除数据 条数 可以根据 返回值 判定处理结果
            # print(tt)
            self.conn.commit()
        except:
            # 发生错误时回滚
            self.conn.rollback()
        finally:
            self.cursor.close()

    def update(self,sql):
        """ 更新数据库操作 """

        self.cursor = self.conn.cursor()

        try:
            # 执行sql
            self.cursor.execute(sql)
            # tt = self.cursor.execute(sql) # 返回 更新数据 条数 可以根据 返回值 判定处理结果
            # print(tt)
            self.conn.commit()
        except:
            # 发生错误时回滚
            self.conn.rollback()
        finally:
            self.cursor.close()



    def select(self,sql):
        """ 数据库查询 """
        self.cursor = self.conn.cursor()
        try:
            self.cursor.execute(sql) # 返回 查询数据 条数 可以根据 返回值 判定处理结果
            data = self.cursor.fetchall() # 返回所有记录列表
            return data
        except:
            print('Error: unable to fecth data')
        finally:
            self.cursor.close()


    def closeDb(self):
        """ 数据库连接关闭 """
        self.conn.close()