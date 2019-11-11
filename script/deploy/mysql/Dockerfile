# 基于mysql:5.7.26构建
FROM mysql:5.7.26

# 维护者信息
MAINTAINER glw

# 复制文件
ADD my.cnf /etc/mysql/conf.d/

# 设置数据挂在目录及工作目录
VOLUME ["/log","/var/lib/mysql","/etc/mysql/conf.d"]
WORKDIR /var/lib

#设置对外的端口号
EXPOSE 3306
