#!/bin/sh
#读取并运行mysql
docker load -i mysql.tar

docker run --name mysql --net=host --restart=always -v /usr/local/mysql/conf:/etc/mysql/conf.d -v /usr/local/mysql/data:/var/lib/mysql -v /usr/local/mysql/log:/log -e MYSQL_ROOT_PASSWORD=root --privileged=true -d mysql:5.7.26 --lower_case_table_names=1


