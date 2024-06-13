FROM amazoncorretto:22-alpine

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

ENV TZ="America/Sao_Paulo"

ENTRYPOINT ["java", "-jar", "app.jar"]