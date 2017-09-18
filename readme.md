# Spring Boot Books

[![Build Status](https://travis-ci.org/marre/books.png)](https://travis-ci.org/marre/books)

Simple demo on how to build a REST API with Spring Boot

Features:
  * Spring Boot
    * Spring Data Mongo
    * Spring Validation
  * Fongo (kind of "in memory mongo")
  * Swagger
  
Things to investigate further:
  * Cleanup swagger:
    * References unused HTTP status codes 
    * Example for authors
  * Search for mutiple fields (author + title)
  * CORS
  * Logging
  * Async controller
  * Security (spring-boot-starter-security)
  * Metrics (actuator, or dropwizard metrics)
  * Deploy to heroku (or something else)
  * Coverage (coveralls, codecov?)
  * Fongo for dev, Mongo for prod
  
To run:
` mvn spring-boot:run`

Swagger UI : [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html#/book45controller)
