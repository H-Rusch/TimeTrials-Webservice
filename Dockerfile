FROM amazoncorretto:21

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} webservice.jar
ENTRYPOINT ["java", "-jar", "/webservice.jar"]