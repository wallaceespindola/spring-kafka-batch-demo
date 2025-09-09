# Multi-stage build for Spring Boot (Java 21)

# 1) Build stage (includes Maven)
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# Leverage Docker layer caching for dependencies
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -q -e -DskipTests dependency:go-offline

# Copy sources and build
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests package

# 2) Runtime stage (JRE only)
FROM eclipse-temurin:21-jre

# You can enable a non-root user if desired
# RUN useradd -m appuser && chown -R appuser:appuser /app
WORKDIR /app

# Copy the fat jar and rename to app.jar
COPY --from=build /workspace/target/*.jar /app/app.jar

EXPOSE 8080

# Set JVM defaults (tweak as needed)
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
