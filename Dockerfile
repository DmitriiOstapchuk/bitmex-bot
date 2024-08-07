FROM eclipse-temurin:17

RUN mkdir /opt/app

COPY ./target/bitmexBot-0.0.1-SNAPSHOT.jar /opt/app

COPY ./src/main/resources/templates ./templates

ENTRYPOINT ["java", "-jar", "/opt/app/bitmexBot-0.0.1-SNAPSHOT.jar", "--spring.datasource.url=jdbc:postgresql://postgres-db:5432/bitmex"]