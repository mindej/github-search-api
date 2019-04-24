FROM openjdk:8-jdk-alpine

ARG JAVA_USER=java
ARG JAVA_GROUP=java

RUN addgroup -S  $JAVA_GROUP
RUN adduser -S  $JAVA_USER -g $JAVA_GROUP

WORKDIR /usr/share/github-search-api

RUN chown -R $JAVA_USER:$JAVA_USER /usr/share/github-search-api
USER $JAVA_USER

ADD target/github-search-api-*.jar github-search-api.jar

EXPOSE 8080

ENTRYPOINT java -jar github-search-api.jar