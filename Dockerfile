FROM maven:3.8.3-openjdk-17 as maven_build
COPY ../pom.xml .
RUN mvn -h clean package
WORKDIR /build
COPY ../url-shortener/pom.xml .
COPY ../url-shortener/src ./src
RUN --mount=type=cache,target=/root/.m2 mvn clean package -Dmaven.test.skip

FROM eclipse-temurin:17-jre-alpine
COPY --from=maven_build /build/target/*.jar app.jar
EXPOSE 5100
ENTRYPOINT ["java","-jar","app.jar"]