# re-jord-be

## Swagger

* https://leadpet-dev6.com/swagger-ui/index.html

## Release

1. AWS EC2 접속
2. 
```shell
# 현재 도커 컨테이너 확인
[ec2-user@ip-172-31-38-180 ~]$ docker container ps
CONTAINER ID   IMAGE                         COMMAND                  CREATED         STATUS         PORTS                                   NAMES
390f5b1338c9   flowertaekk/teamdev6:v0.0.1   "java -jar ./libs/re…"   8 minutes ago   Up 8 minutes   0.0.0.0:80->8080/tcp, :::80->8080/tcp   leadpet

# 도커 컨테이너 정지
[ec2-user@ip-172-31-38-180 ~]$ docker container stop 390f5b1338c9
390f5b1338c9

# 도커 컨테이너 삭제
[ec2-user@ip-172-31-38-180 ~]$ docker container prune -f
Deleted Containers:
390f5b1338c9ccf1a370944d8399b808bc27b0ff435c5000c29a6b0387baaa74

Total reclaimed space: 0B

# 도커 이미지 확인
[ec2-user@ip-172-31-38-180 ~]$ docker image ls
REPOSITORY             TAG       IMAGE ID       CREATED          SIZE
flowertaekk/teamdev6   v0.0.1    26bf4921cdae   16 minutes ago   378MB

# 도커 이미지 삭제
[ec2-user@ip-172-31-38-180 ~]$ docker image rm 26bf4921cdae

# 도커 이미지 다운로드
[ec2-user@ip-172-31-38-180 ~]$ docker pull flowertaekk/teamdev6:v0.0.1

# 도커 이미지로 컨테이너 생성
[ec2-user@ip-172-31-38-180 ~]$ docker run -d -p 8080:8080 -v /etc/localtime:/etc/localtime:ro -e TZ=Asia/Seoul --name=leadpet flowertaekk/teamdev6:v0.0.1 
```
