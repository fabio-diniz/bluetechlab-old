FROM openjdk:8-jdk-alpine

LABEL SERVICE_NAME=wallets
ENV APP_NAME orders.jar
ENV APP_HOME .
ENV LANG en_US.UTF-8

ADD target/*.jar ${APP_HOME}/${APP_NAME}

ENTRYPOINT java -jar ${JAVA_OPTS} ${APP_NAME}