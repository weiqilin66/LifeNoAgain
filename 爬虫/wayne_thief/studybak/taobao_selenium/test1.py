import os
# 当前文件绝对路径
# 1、找到当前文件run_case.py 的绝对路径 :
import sys
current_directory = os.path.dirname(os.path.abspath(__file__))
# 2、由于测试项目文件夹布局为：run_case.py 的上一级目录才是项目的文件夹，因此需要找到run_case.py 的父文件夹，即项目的根目录
print(current_directory)
root_path = os.path.abspath(os.path.dirname(current_directory) + os.path.sep + ".")
# 3、把项目的根目录通过sys.path.append添加为执行时的环境变量
print(root_path)
sys.path.append(root_path)
last_path = os.path.abspath(os.path.dirname(root_path) + os.path.sep + ".")
print(last_path)

