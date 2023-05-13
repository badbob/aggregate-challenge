FROM eclipse-temurin:17-jdk-alpine
EXPOSE 8081
VOLUME /tmp
COPY build/libs/review-service.jar review-service.jar
ENTRYPOINT ["java", "-jar", "review-service.jar"]