FROM mysql:8
MAINTAINER ahmad.zeeshaan@gmail.com

ENV MYSQL_DATABASE=shortener \
    MYSQL_ROOT_PASSWORD=root

ADD schema.sql /docker-entrypoint-initdb.d

EXPOSE 3306