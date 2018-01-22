#! /bin/bash
echo Launch server in debug mode on port 8787 for http://locahost:8182
java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8787,suspend=n -jar target/fr.bbougon.ousontmesaffaires-1.0-SNAPSHOT-jar-with-dependencies.jar
