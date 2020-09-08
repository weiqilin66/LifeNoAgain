def consumer():
    r = ''
    while True:
        print('r', r)
        n = yield r
        if not n:
            return
        print('[CONSUMER] C %s...' % n)
        r = '200 OK'


def produce(c):
    c.send(None)
    print('send none')
    n = 0
    while n < 5:
        n = n + 1
        print('[PRODUCER] P %s...' % n)
        r = c.send(n)
        print('[PRODUCER] C return: %s' % r)
    c.close()


c = consumer()
produce(c)
