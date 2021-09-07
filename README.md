# POC Backend for Frontend 

Prova de conceito utilizando o padrão Backend for Frontend, usando como exemplo uma Single Page Application (SPA) de um delivery. 


## Libraries and Frameworks 

- Quarkus 
- JHipster
- Postman
- Mongodb 
- Mustache 
- Docker 
- Maven
- Sonar

## Development

Para iniciar a aplicação localmente, inicie o banco de dados com o mongodb e rode: 

    ./mvnw

Para mais informações sobre a execução de aplicações com o JHipster, acesse [Using JHipster in development][].

## Testing

Para executar os testes da aplicação, execute: 

    ./mvnw verify

Para mais informações sobre os testes com o JHipster, acesse [Running tests page][].

## Docker

Para iniciar o mongodb via docker-compose, execute: 

    docker-compose -f src/main/docker/mongodb.yml up -d

Para encerrar o mongodb via docker-compose, execute: 

    docker-compose -f src/main/docker/mongodb.yml down

[jhipster homepage and latest documentation]: https://www.jhipster.tech
[jhipster 6.10.5 archive]: https://www.jhipster.tech/documentation-archive/v6.10.5
[using jhipster in development]: https://www.jhipster.tech/documentation-archive/v6.10.5/development/
[using docker and docker-compose]: https://www.jhipster.tech/documentation-archive/v6.10.5/docker-compose
[using jhipster in production]: https://www.jhipster.tech/documentation-archive/v6.10.5/production/
[running tests page]: https://www.jhipster.tech/documentation-archive/v6.10.5/running-tests/
[code quality page]: https://www.jhipster.tech/documentation-archive/v6.10.5/code-quality/
[setting up continuous integration]: https://www.jhipster.tech/documentation-archive/v6.10.5/setting-up-ci/
