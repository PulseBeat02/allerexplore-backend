FROM gradle:8.11.1-jdk21 AS BUILD
WORKDIR /usr/app/
COPY . .
RUN gradle shadowJar

FROM openjdk:21

ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY --from=BUILD $APP_HOME/build/libs/*-all.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]