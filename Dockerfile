FROM openjdk:17-jdk-alpine
COPY target/semtech-program-0.0.1.jar semtech-program-0.0.1.jar
ENTRYPOINT ["java","-jar","/semtech-program-0.0.1.jar"]