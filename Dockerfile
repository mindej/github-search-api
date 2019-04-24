FROM openjdk:8-jdk-alpine

WORKDIR /usr/share/github-search-api

ADD target/github-search-api-*.jar github-search-api.jar

EXPOSE 8080

ENTRYPOINT java -jar github-search-api.jar