ARG MAVEN_IMAGE=maven:3.9.12-eclipse-temurin-17
ARG RUNTIME_IMAGE=eclipse-temurin:17-jre
FROM ${MAVEN_IMAGE} AS build
WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY src ./src
RUN mvn -B -DskipTests package
FROM ${RUNTIME_IMAGE}
WORKDIR /app

ARG JAR_FILE=Todo-0.0.1-SNAPSHOT.jar
COPY --from=build /app/target/${JAR_FILE} app/todo.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app/todo.jar"]
