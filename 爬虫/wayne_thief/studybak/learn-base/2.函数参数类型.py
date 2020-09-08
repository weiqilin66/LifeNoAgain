print('hello world !')


def my_abs(x):
    if x > 0:
        return x
    else:
        return -x


print(my_abs(-2))


def move(x, y):
    return x, y


print(move(22, 33))
ss = move(33, 44)
if isinstance(ss, tuple):
    print('多参数返回其实是个tuple')

# 默认值
def func1(a, b='dd'):
    print(a, b)


func1(22, 33)
func1(22)

# 可变参数
def func2(a, *args):
    sum = 0
    for arg in args:
        sum = arg + sum
    return sum


nums = [1, 2, 3]
print(func2(1, *nums))
print(func2(1, 2))


def func3(s,**kw):
    print(kw)


xx=4
func3(1,f='1',xx = '3')
ma = {'a': 'ff', 'b':xx}
func3(1, **ma)


def func4(g,*,city,job=2):
    print(g, city, job)

func4(1,city='fj',job=1)
func4(1,city=2)