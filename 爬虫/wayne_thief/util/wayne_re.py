import re


# key 是否存在于word字符中 忽略大小写
def checkIgnoreCase(key, word):
    m = re.search(key, word, re.IGNORECASE)
    return bool(m)
