FROM adoptopenjdk/openjdk11:alpine-slim
MAINTAINER James Ma <jamesma.1987@gmail.com>

RUN mkdir /opt/app
ARG JAR_FILE
ADD target/${JAR_FILE} /opt/app/top-tweets.jar
EXPOSE 8080
CMD ["java", "-jar", "/opt/app/top-tweets.jar"]
