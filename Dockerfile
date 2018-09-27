#FROM openjdk:8-jdk-alpine
FROM maven:3.5.4-jdk-8-alpine
VOLUME /tmp
EXPOSE 8080
COPY . /app

RUN cd /app && mvn clean package -DskipTests=true
#ARG JAR_FILE=/app/target/member-service-0.1.0.jar
#ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/target/member-service-0.1.0.jar"]