From openjdk:8
MAINTAINER markmburu685@gmail.com
ADD build/libs/url-shortener-0.0.1-SNAPSHOT.jar url-shortener.jar
EXPOSE 8080
CMD ["java","-jar","url-shortener.jar"]