# github-search-api

Spring boot simple GitHub search application api

##Build

[![Build Status](https://travis-ci.org/mindej/github-search-api.svg?branch=master)](https://travis-ci.org/mindej/github-search-api)

##Requisites:

- JDK11S
- Maven
- docker

Before you start you have update GitHub token in: `src/main/resources/application.yaml`

```
$ mvn install
$ docker-compose up
```

Application documentation is available in http://localhost:8080/swagger-ui.html API description json is available in http://localhost:8080/v2/api-docs?group=public-api

API is located in http://localhost:8080/
