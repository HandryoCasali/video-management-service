# Build stage
FROM eclipse-temurin:21-jdk-alpine as builder

WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline -B

COPY src ./src
RUN ./mvnw package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

ENV JAVA_OPTS=""

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Passa variáveis do ECS para o Spring
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
