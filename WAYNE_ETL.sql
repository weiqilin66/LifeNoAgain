--=============================================    ORACLE    ========================================================

--表操作
CREATE TABLE WAYNE_DEMO_TB(
 test1 varchar(20),
 test2 number(3,1)
)
--注释
--加索引
--自增加序列
--修改表
alter table tablename add(字段名 字段类型)
alter table tablename modify(字段名 字段类型)
alter table tablename drop(字段名)
alter table tablename rename 字段名1 to 字段名2

--存储过程
CREATE OR REPLACE PROCEDURE PROCE_DEMO(x varchar2,y in number,z out varchar) AS --in入参（可省略）out返回只能在begin end中赋值
       --定义变量(变量名 类型 初始值)
       today date :=to_date('20200808','yyyyMMdd')
       BEGIN
         --into赋值
         select sysdate into today from dual;
         --输出
         dbms_output.put_line('今天日期是'||today);
         --事务提交（增删改之后）
         COMMIT;
       END;
--常用函数
       --DECODE(如果column等于80则算1，100算2，都不等缺省为0) 常搭配sum使用等价于带case when条件的count行统计
       SUM(DECODE(COLUMN1,'80',1,'100',2,0 ))
       --向上取整 1.3个人 算2人
       ceil(1.3)
       --转时间
       to_date('20200808','yyyyMMdd')
       --转字符
       to_char(sysdate,'yyyyMMdd')
       --当前系统时间
       select sysdate from dual;
       --这个月最后一天
       lastday(sysdate)

--================================================    MYSQL    ========================================================

--存储过程
CREATE PROCEDURE WAYNE_PROCE_DEMO(IN p_in int)
--表操作
CREATE TABLE WAYNE_DEMO_TB(
id int PRIMARY KEY AUTO_INCREMENT,--主键自增
 test1 varchar(20),
 test2 int
)
--注释
--自增
