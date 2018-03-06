FROM openjdk:8-jre-alpine

RUN mkdir -p /usr/src/ousontmesaffaires
COPY ./target/ousontmesaffaires-docker-jar-with-dependencies.jar /usr/src/ousontmesaffaires
WORKDIR /usr/src/ousontmesaffaires/

RUN apk add --update bash && rm -rf /var/cache/apk/*