FROM openjdk:17-alpine
COPY ./build /usr/src/myapp
WORKDIR /usr/src/myapp
CMD ["java", "-jar", "./libs/re-jord-be-0.0.1-SNAPSHOT.jar"]