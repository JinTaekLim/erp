FROM openjdk:17-jdk-slim
COPY build/libs/erp-0.0.1-SNAPSHOT.jar erp.jar
ENTRYPOINT ["java", "-jar", "erp.jar"]