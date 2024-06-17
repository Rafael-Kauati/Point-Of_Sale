FROM eclipse-temurin:17-jdk-alpine
LABEL authors="IESS company"


COPY ./target/POS-service.jar /app.jar
RUN chmod +x app.jar

CMD ["java", "-jar", "app.jar"]