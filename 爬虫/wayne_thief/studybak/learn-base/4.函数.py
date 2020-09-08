from functools import reduce


def f(x):
    return x * x


# map(函数名, iterable)
r = map(f, [1, 2, 3])  # 结果r是一个iterator 是惰性的 所以要转成list才能输出元素
print(r)
print(list(r))
print(list(map(str, [1])))


# reduce(functionName, 序列)
def add(x, y):
    return x + y


# reduce(f, [x1, x2, x3, x4]) = f(f(f(x1, x2), x3), x4)
a = reduce(add, [1, 3, 5, 7, 9])
print(a)


# filter()
def fil(n):
    return n % 2 == 1


a = filter(fil, [2, 3, 4, 5])  # true 的留下 false的del掉
print(list(a))

print(3 % 2 == 1)  # true

# sorted(参数,函数) 排序
a = sorted([36, 5, -12, 9, -21], key=abs)
print(a)
sorted(['bob', 'about', 'Zoo', 'Credit'], key=str.lower, reverse=True)


def lazy_sum(*args):
    def sum():
        count = 0
        for d in args:
            count = count + d
        return count

    return sum


f = lazy_sum(1, 2)
print(f)
print(f())
