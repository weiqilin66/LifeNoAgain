class Student(object):
    pass


def fun(self):
    self.age = 27


Student.set_age = fun  # 绑定类方法 所有实例都可以使用
stu = Student
stu.name = 'wayne'  # 动态语言可以随时绑定属性方法

print(stu.name)
stu.set_age
print(dir(stu))


class Person(object):

    @property
    def birth(self):
        return self._birth

    @birth.setter
    def birth(self, value):
        self._birth = value

    @property
    def age(self):
        return 2020 - self._birth


p = Person
p.birth = 1993
print(dir(p))
print(p.age, p.birth)
