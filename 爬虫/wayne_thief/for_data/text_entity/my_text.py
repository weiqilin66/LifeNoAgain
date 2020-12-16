
class MyText:
    def read(self,abs_path):
        with open(abs_path, "r") as f:
            for line in f.readlines():
                # 去掉列表中每一个元素的换行符
                line = line.strip('\n')
                # 替换tab
                line = line.replace('\t',' ')
                # 去掉空行
                if line.strip(' ')== '':
                    continue
