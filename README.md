# Où sont mes affaires
[![Build Status](https://travis-ci.org/bbougon/ousontmesaffaires.svg)](https://travis-ci.org/bbougon/ousontmesaffaires)


## CI
https://travis-ci.org/bbougon/ousontmesaffaires

## Docker

### Building container
`docker build -t ou-sont-mes-affaires .`

### Creating bridge network
`docker network create --driver=bridge --subnet=192.168.100.0/24 bridge-ou-sont-mes-affaires`

### Running mongo db container
***Run mongo container***

`docker run -d --ip 192.168.100.2 --network=bridge-ou-sont-mes-affaires --name ou-sont-mes-affaires-mongo mongo`

### Running Ou Sont Mes Affaires container
***Run container***
```bash
docker run -d --rm --name ou-sont-mes-affaires-server --publish 8080:8182 -w /usr/src/ousontmesaffaires/ --ip 192.168.100.10 --network=bridge-ou-sont-mes-affaires ou-sont-mes-affaires java -jar ousontmesaffaires-docker-jar-with-dependencies.jar
```

### Running Ou Sont Mes Affaires angular container

```
docker run -d --rm --name ou-sont-mes-affaires-angular --publish 4200:80 --ip 192.168.100.15 --network=bridge-ou-sont-mes-affaires ou-sont-mes-affaires-web
```

### Some docker command
`docker container ls -a`

`docker network ls`

`docker ***COMMAND*** inspect`



`./runDocker.sh`