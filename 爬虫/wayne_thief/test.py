from service.xianyu.fish import fish_constants


def pressNum( num_str):
    for num in str(int(num_str)):  # int()向下取整
        enterNum = fish_constants['codeMap'][num]

        print('输入数字:', enterNum)


if __name__ == '__main__':
   ss='二手 小秀秀'
   print(ss[3:])



