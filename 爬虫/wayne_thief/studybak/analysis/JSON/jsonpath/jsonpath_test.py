import jsonpath
import json

# jsonpath的使用
# 1. 转换成python对象
dict_1 = json.load(open('store.json','r',encoding='utf8'))
print(type(dict_1))

# 2. 使用
# $代表json的虚拟外节点 多个元素使用[] *代表所有 下标从0开始
ret = jsonpath.jsonpath(dict_1,'$.store.book[*].author')
print(ret)
ret2 =jsonpath.jsonpath(dict_1,'$.store.book[0].author')
print(ret2)
# ..所有
ret3 = jsonpath.jsonpath(dict_1,'$..author')
print(ret3)
# $.store.book[*].author	the authors of all books in the store
# 	$..author	all authors
# 	$.store.*	all things in store, which are some books and a red bicycle.
# 	$.store..price	the price of everything in the store.
# 	$..book[2]	the third book
# 	$..book[(@.length-1)]   $..book[-1:]   the last book in order.
# 	$..book[0,1]  $..book[:2]	the first two books
# 	$..book[?(@.isbn)]	filter all books with isbn number  查找有isbn这个key的所有book字典
# 	$..book[?(@.price<10)]	filter all books cheapier than 10
