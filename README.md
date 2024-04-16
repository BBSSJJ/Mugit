# A502 Backend

## MySQL 설치

```docker
docker pull mysql:8.3.0
docker run -it --name mysql_mugit -p 3307:3307 -e MYSQL_DATABASE=mugit_main_db -e MYSQL_ROOT_PASSWORD={DB 비밀번호} -e MYSQL_USER=mugit -e MYSQL_PASSWORD={DB 비밀번호} -d mysql:8.3.0 --port=3307 --default_authentication_plugin=mysql_native_password
```