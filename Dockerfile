FROM openjdk:21
LABEL authors="root14"
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} barcodeApp.jar
ENTRYPOINT ["java","-jar","/barcodeApp.jar"]