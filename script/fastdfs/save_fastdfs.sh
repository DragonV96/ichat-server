# 构建并保存镜像脚本
docker build -t fastdfs-nginx:5.11 .
docker save -o /usr/local/dockerimages/fastdfs.tar fastdfs-nginx:5.11

