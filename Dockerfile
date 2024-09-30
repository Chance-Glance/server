FROM openjdk:21
WORKDIR /mohazi
COPY mohago_nocar-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]