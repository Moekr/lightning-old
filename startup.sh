#!/usr/bin/env bash

root=/opt/blog
version=1.0.0-SNAPSHOT
profile=prd
database_username=root
database_password=password
admin_username=admin
admin_password=admin

java -server -jar ${root}/moekr-blog-${version}.jar \
    --spring.profiles.active=${profile} \
    --spring.datasource.username=${database_username} \
    --spring.datasource.password=${database_password} \
    --security.user.name=${admin_username} \
    --security.user.password=${admin_password} \
    >${root}/log/$(date +%y%m%d%H%M%S).log 2>&1 &
