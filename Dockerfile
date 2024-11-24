FROM gradle:8.11.1-jdk21 AS BUILD
WORKDIR /usr/app/
COPY . .
RUN gradle build

FROM openjdk:21

ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY --from=BUILD $APP_HOME .
RUN chmod +x gradlew
EXPOSE 8080
ENTRYPOINT ["./gradlew", "run"]