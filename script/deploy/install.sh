#!/bin/sh
# 部署安装脚本

echo "*************************部署开始*************************"
# 安装前环境配置
echo "=============正在关闭selinux...========================="
setenforce 0
sed -i -e "s|^[^#]SELINUX=.*|SELINUX=disabled|" /etc/selinux/config

echo "=============防火墙开机自启...========================="
systemctl enable firewalld
echo "=============开放ssh的防火墙端口22...========================="
firewall-cmd --zone=public --add-port=22/tcp --permanent
echo "=============开放mysql的防火墙端口3306...========================="
firewall-cmd --zone=public --add-port=3306/tcp --permanent
echo "=============开放ichat的防火墙端口8080...========================="
firewall-cmd --zone=public --add-port=8080/tcp --permanent
echo "=============开放fastdfs的防火墙端口80...========================="
firewall-cmd --zone=public --add-port=80/tcp --permanent
echo "=============开放fastdfs的防火墙端口22122...========================="
firewall-cmd --zone=public --add-port=22122/tcp --permanent
echo "=============重启防火墙...========================="
service firewalld restart

#启动docker服务
echo "=============启动docker服务...========================="
systemctl start docker
sudo systemctl enable docker

#移动文件到指定位置
echo "=============将fastdfs文件移动到指定位置...========================="
cp -r fastdfs /usr/local/
echo "=============将数据库sql文件移动到指定位置...========================="
mkdir /usr/local/mysql/
cp chat.sql /usr/local/mysql/

#加载本地镜像文件
echo "===============加载mysql镜像文件...==============="
docker load -i mysql.tar
echo "===============加载ichat镜像文件...==============="
docker load -i ichat.tar
echo "===============加载fastdfs镜像文件...==============="
docker load -i fastdfs.tar

#创建运行环境容器并启动
echo "===============创建mysql容器并启动...==============="
docker run --name mysql --net=host --restart=always -v /usr/local/mysql/conf:/etc/mysql/conf.d -v /usr/local/mysql/data:/var/lib/mysql -v /usr/local/mysql/log:/log -e MYSQL_ROOT_PASSWORD=root --privileged=true -d mysql:5.7.26 --lower_case_table_names=1

echo "===============创建fastdfs容器并启动...==============="
docker run --restart=always -m 512m -v /usr/local/fastdfs/storage:/data/fastdfs/storage -v /usr/local/fastdfs/tracker:/data/fastdfs/tracker -v /usr/local/fastdfs/conf/storage.conf:/etc/fdfs/storage.conf -v /usr/local/fastdfs/conf/tracker.conf:/etc/fdfs/tracker.conf -v /usr/local/fastdfs/conf/client.conf:/etc/fdfs/client.conf -v /usr/local/fastdfs/conf/mod_fastdfs.conf:/etc/fdfs/mod_fastdfs.conf --name fastdfs -d --net=host --privileged=true fastdfs-nginx:5.11

sleep 60s
#初始化mysql数据库（sql路径为宿主机）
echo "===============初始化ichat数据库脚本ichat.sql...==============="
docker exec -i mysql sh -c "exec mysql -uroot -proot" < /usr/local/mysql/chat.sql

#创建ichat容器并启动
echo "===============创建ichat容器并启动...==============="
docker run --name ichat --net=host --restart=always -m 1024m -e JAVA_OPTIONS="-Xmx768m" -e appName="ichat.jar" --privileged=true -e serverPort=8080 -v /usr/local/apps/ichat/conf:/data/apps/config -v /usr/local/apps/ichat/log:/data/log -d ichat:1.0.0
echo "*************************部署完成*************************"

