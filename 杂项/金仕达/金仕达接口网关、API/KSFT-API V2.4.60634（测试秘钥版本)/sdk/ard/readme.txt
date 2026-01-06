Android版本API看穿式部分修改说明
1.使用采集加密API安卓版本中 KingStar_GetSystemInfo 方法，获取客户端采集信息；
2.按照KSFT-API V2.4.20331中doc目录下《看穿式监管金仕达API使用说明V1.0.pdf》“3.2.2.2多对多中继终端使用流程”进行：
登录前发起认证ReqAuthenticate、认证成功后调用RegisterUserSystemInfo上传采集信息（ClientSystemInfo字段传1中拿到的采集信息，ClientSystemInfoLen传采集信息长度）并发起登录。

