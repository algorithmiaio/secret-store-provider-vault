#!/bin/bash

docker run --rm --name db.secret_provider.it -d -p 7702:3306 -e MYSQL_ROOT_PASSWORD=my-secret-pw mariadb

sleep 15

echo "create database secret_it" | mysql -h localhost -P 7702 -u root -pmy-secret-pw




