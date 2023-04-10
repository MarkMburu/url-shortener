## Goals
- [x] Create new Short URL
- [x] Fetch short URL
- [x] Redirect endpoint
- [x] Create User
- [x] Create private url

## Basic Usage

The endpoints below are basic usage, there's also more features, but they are on beta, that being said, it will not be included for now.

### Creating new shortened URL
```
curl -X POST 'http://localhost:8080/api/v1/shortener' \
              -H 'Content-Type: application/json' \
              -d '{"url": "shorturl"}'
              
Response:
{
   "id": "",
   "createdAt": "",
   "originalUrl": "long url",
   "shortenedUrl": "shorturl"
}
```

### Fetching Shortened URL details
```
curl -X GET 'http://localhost:8080/api/v1/shortener/mjprJAuqhm'

Response:
{
   "id": "",
   "createdAt": "",
   "originalUrl": "longurl",
   "shortenedUrl": "short url"
}
```

### Redirect to URL
This endpoint is exclusively for being redirect to the original url. Paste the URL
below to your browser and you will be redirected.
```
http://localhost:8080/api/v1/redirect/ocf
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
- Pull the image 

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
3. Creates (if it doesn't exist) a .env file on the project root (You can use the template below)
4. Build the project with ```mvn clean install```
5The app should be running at ```http://localhost:8080```

### Environment configuration(.env)
```
MONGODB_URL=mongodb://localhost:27017/url-shortener?directConnection=true&ssl=false&authSource=admin
JWT_ISSUER=http://localhost:8080/
JWT_AUDIENCE=url-shortener
JWT_SECRET=My_SECRET
JWT_EXPIRATION_SECONDS=3600
JWT_REFRESH_TOKEN_EXPIRATION_SECONDS=3600
```