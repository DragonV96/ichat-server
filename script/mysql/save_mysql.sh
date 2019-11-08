#!/bin/sh
# 制作镜像脚本
docker build -t mysql:5.7.26 .

# 保存镜像脚本
docker save -o /usr/local/dockerimages/mysql.tar mysql:5.7.26


