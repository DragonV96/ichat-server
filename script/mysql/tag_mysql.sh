#!/bin/sh
# 制作镜像脚本
docker build -t mysql:5.7.26 .

docker load -i mysql.tar

docker tag mysql:5.7.26 mysql:5.7.26


