# 删除并重装镜像及运行容器脚本
docker rm -f ichat

docker rmi -f ichat:1.0.0

docker build -t ichat:1.0.0 /usr/local/apps/ichat

docker run --name ichat --net=host --restart=always -m 1024m -e JAVA_OPTIONS="-Xmx768m" -e appName="ichat.jar" --privileged=true -e serverPort=8080 -v /usr/local/apps/ichat/conf:/data/apps/config -v /usr/local/apps/ichat/log:/data/log -d ichat:1.0.0

docker logs -f ichat