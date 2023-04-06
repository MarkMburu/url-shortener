# Url Shortener

An open source ready for usage url shortener.

Feel free to use the API (as long there's no abuse) for shortening/testing the API.

## Roadmap
- [x] Creating new Shortened URL
- [x] Fetching shorted URL
- [x] Redirect endpoint
- [x] Create User
- [x] Create private url

## Basic Usage

The endpoints below are basic usage, there's also more features, but they are on beta, that being said, it will not be included for now.

### Creating new shortened URL
```
curl -X POST 'http://localhost:8080/api/v1/shortener' \
              -H 'Content-Type: application/json' \
              -d '{"url": "https://www.reuters.com/article/urnidgns002570f3005978d8002576f60035a6bb-idUS98192761820100330"}'
              
Response:
{
   "id": "2b99abe2-1f24-4964-91e5-f7517f4dc16b",
   "createdAt": "2023-02-20T00:04:27.977013737",
   "originalUrl": "SOME_LONG_URL",
   "shortenedUrl": "https://www.reuters.com/article/urnidgns002570f3005978d8002576f60035a6bb-idUS98192761820100330"
}
```

### Fetching Shortened URL details
```
curl -X GET 'http://localhost:8080/api/v1/shortener/mjprJAuqhm'

Response:
{
   "id": "2b99abe2-1f24-4964-91e5-f7517f4dc16b",
   "createdAt": "2023-02-20T00:04:27.977013737",
   "originalUrl": "SOME_LONG_URL",
   "shortenedUrl": "https://www.reuters.com/article/urnidgns002570f3005978d8002576f60035a6bb-idUS98192761820100330"
}
```

### Redirect to URL
This endpoint is exclusively for being redirect to the original url. Paste the URL
below to your browser and you will be redirected.
```
http://localhost:8080/api/v1/redirect/abcfg
```


## Running with Docker-Compose
There's already a configured `docker-compose.yaml` file in the project root, so you can 
easily start the project locally following the instructions:

1. Open `docker-compose.yml`
2. Change the required environment variables
3. If you build the image locally, change the image property
4. Start the apps with `docker-compose up -d`

You should be able to reach the app accessing `http://localhost:8080/api/v1/ping` on your browser

## Manually running it with Docker

- Make sure that you have docker running 
- Pull the image with: `docker pull ghcr.io/markmburu/url-shortener:latest`

Start the container with
```
    docker run --name some-url-shortener \
        --restart always \
        -e 'MONGODB_URL=mongodb://localhost:27017/url-shortener?directConnection=true&ssl=false&authSource=admin' \
        -e 'JWT_ISSUER=http://localhost:8080' \ 
        -e 'JWT_AUDIENCE=http://localhost:8080' \
        -e 'JWT_EXPIRATION_SECONDS=7200' \
        -e 'JWT_REFRESH_TOKEN_EXPIRATION_SECONDS=7200' \
        -p 8080:8080 \
        -d ghcr.io/m4urici0gm/url-shortener:latest
```
Check if the app is running with `curl 'http://localhost:8080/api/ping' | json_pp`.
If everything went fine, you should be able to see the response like this:
```json
{
   "message" : "Hello World!"
}
```

## Prerequisites
Before you begin, ensure you have met the following requirements:
- Java JDK 17 or later
- A running mongodb instance.

## Running it locally (dev environment)
1. Fork the repository
2. Make sure you have a running mongodb instance
3. Creates (if it doesnt exist) a .env file on the project root (You can use the template below)
4. Build the project with ```./gradlew clean build```
5. Run the project with ```./gradlew bootRun```
6. The app should be running at ```http://localhost:8080```

### Template file for .env
```
MONGODB_URL=mongodb://root:blueScreen#666@localhost:27017/url-shortener
JWT_ISSUER=http://localhost:8080/
JWT_AUDIENCE=url-shortener
JWT_SECRET=SOME_RANDOM_STRING
JWT_EXPIRATION_SECONDS=7200
JWT_REFRESH_TOKEN_EXPIRATION_SECONDS=7200
```