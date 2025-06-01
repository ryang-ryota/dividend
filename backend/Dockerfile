## Mavenビルド用イメージ
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Devtoolsのリモート起動を有効化
EXPOSE 8080 35729
ENTRYPOINT ["java", "-Dspring.devtools.restart.enabled=true", "-Dspring.devtools.livereload.enabled=true", "-jar", "app.jar"]
