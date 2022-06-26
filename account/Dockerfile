#FROM openjdk:8-jdk-alpine
#start with a base image containing Java Runtime
FROM openjdk:8-slim as build
#who will maintain the image

#Add the application's jar to the container
COPY target/account.jar account.jar

#command to execute the app
ENTRYPOINT ["java","-jar","/account.jar"]