#!/usr/bin/env bash

admin_username=admin
admin_password=admin

authorization=$(echo -e "${admin_username}:${admin_password}\c" | base64)

curl -l -H "Content-type: application/json" -H "Authorization: Basic ${authorization}" -X POST http://localhost:8080/api/shutdown

echo