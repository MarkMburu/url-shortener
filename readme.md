# URL Shortener

Spring Boot based REST API that takes a URL and returns a shortened URL and uses MySQL to persist data.

# Getting Started

## Dependencies

This project depends on 
* spring-boot-starter-web (Spring boot framework)
* spring-boot-starter-data-jpa (for data persistence)
* spring-boot-starter-actuator (for API statistics)
* commons-validator (for URL validation)
* spring-boot-starter-test and h2 (for tests)

## Project Build 

To build this project, run

```shell script
cd url-shortener
gradle clean build
```

## Deployment

Project build can be deployed using docker-compose.yml which sets up two containers for
* REST API
* MySql


To deploy the project, run

```shell script
docker-compose up --build
```

**The application will be accessible on http://localhost:8080**

### db.Dockerfile
`db.Dockerfile` builds the docker image for MySql using MySql version 8 as the base image. It uses `url.sql` at startup to set up the database schema.

### api.Dockerfile
`api.Dockerfile` sets up an image to deploy the project's jar file generated above from `build/libs/url-shortener-0.0.1-SNAPSHOT.jar`. It exposes the API on port `8080`

### docker-compose.yml
>> This provides the settings necessary to host API and MySQL in containers. With the container names "urlshortener" and "shorten," it creates the two services "api-server" and "api-db."
The MySql container is specified as the datasource url in the 'api-server' setting.
The docker network named "urlshortener-mysql-network" connects "api-server" and "api-db" together. The network permits communication between the two containers.

## API Endpoints

You can access following API endpoints at http://localhost:8080

### POST `/shorten`
It takes a JSON object in the following format as payload

```json
{
  "originalUrl":"<The URL to be shortened>"
}
```

#### cURL

```shell script
curl -X POST \
  http://localhost:8080/shorten \
  -H 'Content-Type: application/json' \
  -d '{"originalUrl":"https://example.com/example/1"}'
```

Response:

```json
{
  "shortenedUrl": "<shortened url for the originalUrl provided in the request payload>"
}
```

* Please take note that only valid HTTP or HTTPS Urls can be used with the API. If the URL is invalid, it returns a "400 Bad Request" error and a JSON object in the response body with the following structure.

```json
{
  "field":"originalUrl",
  "value":"<Invalid Url provided in the request>",
  "message":"<Exception message>"
}
```

### GET `/<shortened_url>`

This endpoint redirects to the corresponding originalUrl.

### GET `/actuator/health`

Included the spring boot actuator dependency for API metrics. You can try this endpoint for health checks.

#### cURL

```shell script
curl -X GET   http://localhost:8080/actuator/health
```

# Url Shortening Algorithm

I thought of two approaches
1. Generating hashes for the originalUrl and storing them as key value pairs in redis cache or in mysql database
2. Performing a Base62 conversion from Base10 on the id of stored originalUrl

I tested both strategies, but while using hashes, the hashes occasionally exceeded the length of the URL. The readability and memorability were other problems. I chose the second strategy, thus. Even the highest value of Long yields 10 characters using the base conversion method, which are still quite simple to remember.
>> Guava, a dependency from Google, might be utilized to produce hashes in this situation. Despite the fact that the murmur 3 32 hash implemented in Guava could produce strings up to 10 characters long.

