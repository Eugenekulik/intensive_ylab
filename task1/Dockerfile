FROM openjdk:17-jre-slim

VOLUME /tmp

# Копируем JAR-файл приложения в контейнер
COPY target/.jar app.jar

# Задаем параметры запуска приложения
ENTRYPOINT ["java", "-jar", "/app.jar"]