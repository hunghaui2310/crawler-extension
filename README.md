# Crawler
## Getting started

## How to run
Prerequisites
- Docker Compose: [Docker link](https://www.docker.com/get-started/)

# Backend
cd to `crawler` and run to command line:

```
    docker-compose up -d
```

Rebuild Backend:
Check docker container running:
```
    docker ps
```
After that, kill container with image name: `crawler-spring-app`
```
    docker stop [CONTAINER ID]
```
Clear cache backend of Backend app:
```
    docker system prune -a
```
Enter `y` to confirm.

## Restart Backend App
To restart Crawler App, enter to command line:
```
    docker-compose up -d
```

# Extension:

Add extension to `Google Chrome`. See: [Load an unpacked extension](https://developer.chrome.com/docs/extensions/get-started/tutorial/hello-world)