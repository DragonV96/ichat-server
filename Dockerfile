# 基于openjdk:8-jdk-slim构建
FROM openjdk:8-jdk-slim

# 维护者信息
MAINTAINER glw

# 设置环境变量
ENV JAVA_OPTIONS -Xmx768m

# 复制文件
ADD ./*.jar /data/apps/ichat.jar

# 设置数据挂在目录及工作目录
VOLUME ["/data/apps","/data/log","/data/file"]
WORKDIR /data/apps

#设置对外的端口号
EXPOSE 8080

#容器启动后执行该命令
CMD java $JAVA_OPTIONS -jar /data/apps/ichat.jar --server.port=$serverPort
