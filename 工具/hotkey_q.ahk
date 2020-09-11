;Notes: #==win !==Alt   ^==Ctr  +==shift
;---------------启动应用----------------------
^+i::run D:\soft\JetBrains\IntelliJ IDEA 2019.3.1\bin\idea64.exe
^+s::run D:\soft\JetBrains\WebStorm 2019.3.1\bin\webstorm64.exe
^+p::run D:\soft\JetBrains\PyCharm 2019.3.1\bin\pycharm64.exe
^+g::run C:\Users\weiqilin66\AppData\Local\Google\Chrome\Application\chrome.exe
^+n::run D:\soft\PremiumSoft\Navicat Premium 12\navicat.exe
;----------------------------------------------
;# use injk to exchange up down left right

!n::
   Send, {Down}
Return

!k::
   Send, {Right}
Return

!j::
   Send, {Left}
Return

!i::
   Send, {Up}
Return

;---------------------------------------------
!m::
   Send, +{Enter}
Return
!o::
   Send, {End}
Return
!h::
   Send, {Home}
Return