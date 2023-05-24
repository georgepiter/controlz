FROM maven:3.8.3-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src src
RUN mvn package

FROM openjdk:17-jdk-slim AS runtime
WORKDIR /app
COPY --from=build /app/target/controlz.jar .
CMD ["java", "-jar", "controlz.jar"]