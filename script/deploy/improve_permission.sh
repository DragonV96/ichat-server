#!/bin/sh
# 提升部署脚本权限

echo "提升部署脚本权限..."
chmod +x delete.sh
chmod +x install.sh
chmod +x restart.sh

echo "提升mysql操作脚本权限..."
chmod +x /usr/local/mysql/sql/save_mysql.sh
chmod +x /usr/local/mysql/sql/tag_mysql.sh

echo "提升fastdfs操作脚本权限..."
chmod +x /usr/local/mysql/sql/save_fastdfs.sh
chmod +x /usr/local/mysql/sql/tag_fastdfs.sh


