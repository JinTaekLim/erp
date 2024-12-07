FROM openjdk:17-jdk-slim
COPY build/libs/erp-0.0.1-SNAPSHOT.jar erp.jar
ENV SPRING_PROFILES_ACTIVE=dev
ENV TZ=Asia/Seoul
ENTRYPOINT ["java", "-jar", "erp.jar"]