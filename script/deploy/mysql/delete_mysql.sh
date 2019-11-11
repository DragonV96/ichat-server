#!/bin/sh
# 删除mysql容器及镜像
docker rm -f mysql
docker rmi -f mysql:5.7.26
