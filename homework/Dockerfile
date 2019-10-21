FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY *.jar app.jar
ADD hm_db.sqlite hm_db.sqlite
ENTRYPOINT ["java","-jar","/app.jar"]