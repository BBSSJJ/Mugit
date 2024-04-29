# A502 Backend

## MySQL 설치

### DB 설치
```docker
docker pull mysql:8.3.0
docker run -it --name mysql_mugit -p 3307:3307 -e MYSQL_DATABASE=mugit_main_db -e MYSQL_ROOT_PASSWORD={DB 비밀번호} -e MYSQL_USER=mugit -e MYSQL_PASSWORD={DB 비밀번호} -d mysql:8.3.0 --port=3307 --default_authentication_plugin=mysql_native_password
```

## Session Redis 설치
```docker
docker pull redis:alpine3.19
docker run -it --name redis_session -p 6379:6379 -d redis:alpine3.19
```

### TEST DB 설치
```docker
docker run -it --name mysql_mugit_test -p 3308:3308 -e MYSQL_DATABASE=mugit_test_db -e MYSQL_ROOT_PASSWORD={DB 비밀번호} -e MYSQL_USER=mugit_test -e MYSQL_PASSWORD={DB 비밀번호} -d mysql:8.3.0 --port=3308 --default_authentication_plugin=mysql_native_password
docker run -it --name redis_test_session -p 6380:6379 -d redis:alpine3.19
```

## User Server 생성
```docker
docker pull 1eaf/mugit_user_server
docker run -it -d -p 8080:8080 --name mugit_user_server 1eaf/mugit_user_server
```