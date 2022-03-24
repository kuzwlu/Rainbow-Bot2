# Rainbow-Bot2
一个QQ机器人--springboot
基于simple-reboot开发
### 动态编译java文件实现QQ机器人的插件化（QQ聊天监听，发送消息）
    
#### 使用Javassist来控制动态编译java文件导入的包（黑白名单）

### 通过反射调用方法实现QQ聊天监听，发送消息


加载插件，卸载插件，动态菜单

支持动态编译出错提示（具体什么错误，多少行）

### 工具类
    Aria2----aria2下载
    Email----邮件发送
    Http-----实现多线程，ip池，回滚操作
    Path-----通过getPath获取jar包所在的目录，自动创建文件
    QQLogin--QQ账号密码登录，获取qq的pkey和其他重要参数
    QRLogin--QQ扫码登录，获取qq的pkey和其他重要参数
    ThreadPool--线程池，根据电脑性能自动创建
    
    由于编译功能模块化，以后将把编译的功能提取出来，单独做成一个依赖包，暂时只支持java，由于设计好了接口，适配编译其他语言有很好的拓展性
