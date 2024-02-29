FROM openjdk:17-jre-slim

VOLUME /tmp

COPY target/.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]