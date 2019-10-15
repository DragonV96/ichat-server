#!/bin/sh
docker restart $(docker ps -a -q)
