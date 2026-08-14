# syntax=docker/dockerfile:1

FROM docker.io/library/eclipse-temurin:25-jdk-alpine AS build
WORKDIR /build
COPY mvnw mvnw.cmd ./
COPY .mvn/ .mvn/
COPY pom.xml ./
RUN chmod +x mvnw && ./mvnw -B dependency:go-offline
COPY src/ src/
RUN ./mvnw -B -DskipTests package

FROM docker.io/library/eclipse-temurin:25-jre-alpine AS run
WORKDIR /app
COPY --from=build /build/target/*.jar app.jar
EXPOSE 8009
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
