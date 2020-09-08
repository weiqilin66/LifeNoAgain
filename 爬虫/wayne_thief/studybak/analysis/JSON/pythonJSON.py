import json

data1 = [
    {'name':'王宝强','age':30},
    {'name':'宋吉吉','age':30}
]
data2 = {
    'name':'李小璐',
    'job': '洗头'
}
# 1
print(json.dumps(data1,ensure_ascii=False))
string=json.dumps(data2)

# 2
obj = json.loads(string)
print(obj)
print(type(obj))

# 3 对象转换成json字符串写入
# json.dump(obj,open('json.txt','w',encoding='utf8'))
# # 4 读取json字符串转换成对象
# obj2 = json.load(open('json.txt','r',encoding='utf8'))
# print(obj2)