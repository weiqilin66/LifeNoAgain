import time
import tkinter as tk
from tkinter import messagebox

"""倒计时"""
def main(i):
    window1 = tk.Tk()
    window1.title('计时器【v0.0】')
    window1.geometry('300x200')

    l1 = tk.Label(window1, text='距离下次爬取时间：', font=('宋体', 15))
    l1.place(x=5, y=10)
    sec_now = time.localtime(time.time())
    hour = i // 60  # 向下取整
    sec = i % 60
    # 目标时间
    target_time_list = list(sec_now)
    target_time_list[3] = target_time_list[3] + hour
    target_time_list[4] = target_time_list[4] + sec
    if target_time_list[4]>60:
        target_time_list[3] +=1
        target_time_list[4]-=60

    def time_now():

        seconds_now = time.time()
        lt = time.localtime(seconds_now)
        # 当前时间
        now_list = list(lt)
        # 剩余时间
        l_hour = target_time_list[3]-now_list[3]
        l_sec = target_time_list[4]-now_list[4]
        l_s = target_time_list[5]-now_list[5]
        if l_s<0:
            l_sec-=1
            l_s+=60
        if l_sec<0:
            l_hour-=1
            l_sec+=60
        leave_time = [l_hour, l_sec, l_s]
        time1 = []
        time2 = '%02d时%02d分%02d秒' % (leave_time[0], leave_time[1], leave_time[2])
        if leave_time==[0,0,0]:
            return
        if target_time_list != now_list:
            time1 = time2
            l1_2 = tk.Label(window1, text=time1, font=('宋体', 20))
            l1_2.configure(text=time2)
            l1_2.place(x=30, y=50)
            l1_2.after(200, time_now)
    time_now()

    # button1 = tk.Button(window1, text='输入查询日期', font=('宋体', 15), command=input_time)
    # button1.place(x=85, y=125)

    window1.mainloop()


if __name__ == '__main__':
    i = 1
    main(i)
