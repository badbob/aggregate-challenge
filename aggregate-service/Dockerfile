FROM eclipse-temurin:17-jdk-alpine
EXPOSE 8080
VOLUME /tmp
COPY build/libs/aggregate-service.jar aggregate-service.jar
ENTRYPOINT ["java", "-jar", "aggregate-service.jar", "--review.host=review"]