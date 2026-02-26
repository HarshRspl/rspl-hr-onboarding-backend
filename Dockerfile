FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy everything into the container
COPY . .

# Fix permissions + (optional) strip Windows CRLF from mvnw, then build
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw \
    && ./mvnw -B -DskipTests clean package

FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built jar only
COPY --from=build /app/target/*.jar app.jar

# Railway provides PORT at runtime; Spring Boot should bind to it
CMD ["sh", "-c", "java -Dserver.port=${PORT} -jar app.jar"]
