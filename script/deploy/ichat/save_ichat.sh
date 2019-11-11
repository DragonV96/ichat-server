# 构建并保存镜像脚本
docker build -t ichat:1.0.0 .
docker save -o /usr/local/dockerimages/ichat.tar ichat:1.0.0

