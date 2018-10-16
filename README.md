# Où sont mes affaires
[![Build Status](https://travis-ci.org/bbougon/ousontmesaffaires.svg)](https://travis-ci.org/bbougon/ousontmesaffaires)
[![codecov.io](https://codecov.io/gh/bbougon/ousontmesaffaires/coverage.svg?branch=master)](https://codecov.io/gh/bbougon/ousontmesaffaires/codecov.io?branch=master)

## CI
https://travis-ci.org/bbougon/ousontmesaffaires

## Docker

### Building container
`docker build -t ou-sont-mes-affaires-api .`

### Creating bridge network
`docker network create --driver=bridge --subnet=192.168.100.0/24 bridge-ou-sont-mes-affaires`

### Running mongo db container
***Run mongo container***

`docker run -d --ip 192.168.100.2 --network=bridge-ou-sont-mes-affaires --name ou-sont-mes-affaires-mongo mongo`

### Running Ou Sont Mes Affaires container
***Run container***
```bash
docker run -d --rm --name ou-sont-mes-affaires-server --env-file ./.env.list --mount source=ou-sont-mes-affaires-image-storage,target=/image-storage --publish 8080:8182 -w /usr/src/ousontmesaffaires/ --ip 192.168.100.10 --network=bridge-ou-sont-mes-affaires ou-sont-mes-affaires-api java -jar ousontmesaffaires-docker-jar-with-dependencies.jar
```

***Run in debug mode***
```bash
docker run -d --rm --name ou-sont-mes-affaires-server --env-file ./.env.list --mount source=ou-sont-mes-affaires-image-storage,target=/image-storage --publish 8080:8182 --publish 8787:8787 -w /usr/src/ousontmesaffaires/ --ip 192.168.100.10 --network=bridge-ou-sont-mes-affaires ou-sont-mes-affaires-api java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8787,suspend=n -jar ousontmesaffaires-docker-jar-with-dependencies.jar
```

### Running Ou Sont Mes Affaires angular container

```
docker run -d --rm --name ou-sont-mes-affaires-angular --publish 4200:80 --ip 192.168.100.15 --network=bridge-ou-sont-mes-affaires ou-sont-mes-affaires-web
```

### Some docker command
`docker container ls -a`

`docker network ls`

`docker ***COMMAND*** inspect`

`docker exec -it ou-sont-mes-affaires-server /bin/bash`

`docker logs ou-sont-mes-affaires-server`

`docker container rm CONTAINER_NAME`

`./runDocker.sh`