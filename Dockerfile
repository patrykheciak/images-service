FROM openjdk:8-jdk-alpine3.9
ADD target/images-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
CMD java -jar images-0.0.1-SNAPSHOT.jar