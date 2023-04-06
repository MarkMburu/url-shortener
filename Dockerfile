FROM gradle:7.6-jdk17 as TEMP_BUILD_IMAGE

ENV APP_HOME = /app

RUN gradle --version && java -version

WORKDIR /app

COPY build.gradle settings.gradle $APP_HOME/


RUN gradle clean build > /dev/null 2>&1 || true


COPY ./ /app/

RUN gradle build --no-daemon -x test

FROM openjdk:17-jdk-slim
ENV ARTIFACT_NAME=url-shortener-0.1.0.jar
ENV APP_HOME=/app

WORKDIR $APP_HOME
COPY --from=TEMP_BUILD_IMAGE $APP_HOME/build/libs/$ARTIFACT_NAME .
COPY --from=TEMP_BUILD_IMAGE $APP_HOME/config/application.yaml ./config/
EXPOSE 8080
ENTRYPOINT exec java -DconfigFileName=config/application.yaml -DchecksumEnabled=false -jar ${ARTIFACT_NAME}