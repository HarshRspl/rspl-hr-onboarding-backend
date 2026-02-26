FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .

RUN sed -i 's/\r$//' mvnw && chmod +x mvnw \
  && sh mvnw -B -DskipTests clean package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

CMD ["sh", "-c", "java -Dserver.port=${PORT} -jar app.jar"]
