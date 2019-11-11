# 运行镜像脚本
docker load -i fastdfs.tar

docker run --restart=always -m 512m -v /usr/local/fastdfs/storage:/data/fastdfs/storage -v /usr/local/fastdfs/tracker:/data/fastdfs/tracker -v /usr/local/fastdfs/conf/storage.conf:/etc/fdfs/storage.conf -v /usr/local/fastdfs/conf/tracker.conf:/etc/fdfs/tracker.conf -v /usr/local/fastdfs/conf/client.conf:/etc/fdfs/client.conf -v /usr/local/fastdfs/conf/mod_fastdfs.conf:/etc/fdfs/mod_fastdfs.conf --name fastdfs -d --net=host --privileged=true fastdfs-nginx:5.11

