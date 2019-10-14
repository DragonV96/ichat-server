# 运行镜像脚本
docker load -i fastdfs.tar

docker run --restart=always -m 512m -v /usr/local/comtom/fastdfs/storage:/data/fastdfs/storage -v /usr/local/comtom/fastdfs/tracker:/data/fastdfs/tracker -v /usr/local/comtom/fastdfs/conf/storage.conf:/etc/fdfs/storage.conf -v /usr/local/comtom/fastdfs/conf/tracker.conf:/etc/fdfs/tracker.conf -v /usr/local/comtom/fastdfs/conf/client.conf:/etc/fdfs/client.conf -v /usr/local/comtom/fastdfs/conf/mod_fastdfs.conf:/etc/fdfs/mod_fastdfs.conf --name fastdfs -d --net=host --privileged=true fastdfs-nginx:5.11

