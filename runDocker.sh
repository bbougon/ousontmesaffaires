#! /bin/bash
echo Launch server on http://locahost:8182
docker run -d --rm --name ou-sont-mes-affaires-server --publish 8080:8182 -w /usr/src/ousontmesaffaires/ --ip 192.168.100.10 --network=bridge-ou-sont-mes-affaires ou-sont-mes-affaires java -jar ousontmesaffaires-docker-jar-with-dependencies.jar