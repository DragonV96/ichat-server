<h1><center>跨平台仿微信即时通讯聊天app</center></h1>

如果本项目对你有用，还请在项目右上角点个star~，谢谢大佬支持。

# 作品简介

​		本项目采用前后端分离开发，面向restful风格的接口编程。

​		前端可跨平台在web，android，ios等设备上运行，图片服务器用的fastdfs统一管理app内图片，登录以及消息转发服务器采用netty+springboot，快速高效开发的同时，服务能承受大量在线同时进行聊天的后台，采用nginx进行反向转发代理，提高服务器稳定性。

​		采用docker容器方式部署，隔离开发与测试环境不一致问题。

# 涉及技术框架

后端：

- Netty
- SpringBoot
- Nginx+FastDFS分布式文件系统
- Mybatis
- lombok + slf4j
- swagger

后端项目：[传送门](https://github.com/DragonV96/ichat-server)

前端：

- MUI
- H5Plus（H5+）

前端项目：[传送门](https://github.com/DragonV96/ichat-weixin)

# 实现功能

- 用户的注册、登录与注销
- 用户离线推送消息
- 头像的上传与保存
- 修改昵称
- 好友申请与添加
- 心跳检测，断开长时间未活动的客户端，回收资源
- 聊天消息AES加密，防抓包,保障聊天消息安全性

# 环境参数

- 基础工具 maven3+、jdk8、tomcat8
- 开发工具 IDEA(或STS，或Eclipse，建议IDEA)
- 核心框架 SpringBoot2.0、Netty 4.1
- 持久层 MyBatis3.4.5
- 数据库 MariaDB/mysql HikariCP 2.7.9
- 文件服务器 nginx 1.12.0 fastdfs 5.05
- 前端框架 MUI H5plus

# 搭建流程

​		稍后有空再更，script文件夹下有部署脚本。

# 界面展示

![](assets/1.jpg)

![2](assets/2.jpg)

![3](assets/3.jpg)

![4](assets/4.jpg)