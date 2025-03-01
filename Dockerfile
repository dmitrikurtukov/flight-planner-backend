FROM openjdk:23-jdk-slim
WORKDIR /app
COPY build/libs/flight-planner-backend-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]