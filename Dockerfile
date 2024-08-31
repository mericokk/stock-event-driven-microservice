FROM openjdk:21
COPY target/*.jar stock-service.jar
ENTRYPOINT ["java", "-jar", "stock-service.jar"]