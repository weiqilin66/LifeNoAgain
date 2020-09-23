import cx_Oracle


class MyOracle(object):
    def __init__(self):
        self.conn = cx_Oracle.connect('system/linweiqi45vip@127.0.0.1:1521/orcl')
        self.cursor = self.conn.cursor()

    def insert(self, sql):
        """ 插入数据库操作 """
        self.cursor = self.conn.cursor()
        try:
            # 执行sql
            tt = self.cursor.execute(sql)  # 返回 插入数据 条数 可以根据 返回值 判定处理结果
            # print(sql)
            # if tt == 1:
            #     print('插入成功')
            # else:
            #     print('插入失败')

            self.conn.commit()
        except:
            # 发生错误时回滚
            self.conn.rollback()
        finally:
            self.cursor.close()

    def delete(self, sql):
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

    def update(self, sql):
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

    # 返回的是包含tuple的tuple 一个tuple一行记录
    def select(self, sql):
        """ 数据库查询 """
        self.cursor = self.conn.cursor()
        try:
            self.cursor.execute(sql)  # 返回 查询数据 条数 可以根据 返回值 判定处理结果
            data = self.cursor.fetchall()  # 返回所有记录列表
            return data
        except:
            print('Error: unable to fecth data')
        finally:
            self.cursor.close()

    def closeDb(self):
        """ 数据库连接关闭 """
        self.conn.close()
