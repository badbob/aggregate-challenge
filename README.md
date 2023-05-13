# Modules
## review-service

Contains the CRUD JPA microservice which operates review.
User can POST 1 to 5 scope for the product.
Service implemented in Kotlin which I'm have no experience with.
Just as demonstraction that I can dive in quickly into new JVM language.

### How to build

    $ ./gradlew :review-service:bootJar

### How to run locally

    $ java -jar review-service/build/libs/review-service.jar

### Request examples

#### Post the 5 score to product with ID = 1:

Writing end-points requires basic authentication.

Authorized request:

    $ curl -X POST -v -u user:password localhost:8081/rating/1/5

*201 Created* status responded

    $ curl -X POST -v localhost:8081/rating/1/5

*401 Unauthorized* status responded

#### Getting the product (id=1) rating:

Request:

    $ curl localhost:8081/rating/1

Response:

```json
{
  "productId": 1,
  "rating": 3.6666667,
  "total": 3
}
```

## review-model

Contains the shared (by *review-client* and *review-service*) model classes.

## review-client

Contains the reactive client to call _review-service_

## aggregate-service

Contains microservice aggregating results from external API and _review-service_.
As an external API I used [Dummy Rest API](https://dummy.restapiexample.com/) instead
of Adidas, because Adidas introduced some kind of WAF which is blocking some of the
requests. So I decided that bypassing the WAS is out of the scope of this task.

### How to build

    $ ./gradlew :aggregate-service:bootJar

### How to run locally

    $ java -jar aggregate-service/build/libs/aggregate-service.jar --review.host=localhost


### Request examples
#### Getting the aggregate:

Request

    $ curl localhost:8080/aggregate/2

Response:

```json
{
  "product": {
    "id": 2,
    "title": "iPhone X",
    "description": "SIM-Free, Model A19211 6.5-inch Super Retina HD display with OLED technology A12 Bionic chip with ..."
  },
  "rating": {
    "productId": 2,
    "rating": 5,
    "total": 1
  }
}
```

#### Getting the aggregate with/out rating

Request:

    $ curl -s localhost:8080/aggregate/3

Response has no rating, 404 error handled correctly:

```json
{
    "product": {
        "id": 3,
        "title": "Samsung Universe 9",
        "description": "Samsung's new variant which goes beyond Galaxy to the Universe"
    },
    "rating": null
}
```

*200 OK* status returned

#### Trying to get unexistent product

Request:

    $ curl -s -v localhost:8080/aggregate/1000

Response:

```json
$ curl -s localhost:8080/aggregate/1000 | jq .
{
  "timestamp": "2023-05-10T09:45:19.477+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "No such product with given ID: 1000",
  "path": "/aggregate/1000
```

*404* status returned.


# How to run in Docker
## How to build [Review] service Docker image

    $ ./gradlew :review-service:bootRun
    $ docker build -t aggragate-challenge/review-service review-service/

## How to build [Aggregate] service Docker image

    $ ./gradlew :aggregate-service:bootJar
    $ docker build -t aggragate-challenge/aggregate-service aggregate-service/

## Run Docker compose

    $ docker-compose up
