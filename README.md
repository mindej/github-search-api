# github-search-api

Spring boot simple GitHub search application api

Requisites:

- JDK8
- Maven
- docker
- docker-compose

Before you start you have update GitHub token in: `src/main/resources/application.yaml`

```
$ mvn install
$ docker-compose up
```

Application documentation is available in http://localhost:8080/swagger-ui.html API description json is available in http://localhost:8080/v2/api-docs?group=public-api

API is located in http://localhost:8080/api
