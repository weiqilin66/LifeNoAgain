import cx_Oracle
from util.oracle_util import Oracle

o = Oracle()
result=o.select('Select sysdate from dual')

print (result)
