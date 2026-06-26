FROM eclipse-temurin:8-jdk
RUN addgroup --system spring && adduser --system spring --group spring
USER spring:spring
ARG JAR_FILE_RELATIVE_LOCATION=target
ARG JAR_FILE=${JAR_FILE_RELATIVE_LOCATION}/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]

