FROM openjdk:8-jdk-alpine
COPY ./build/libs/*.jar news_app.jar
ENTRYPOINT ["java","-jar","/news_app.jar"]