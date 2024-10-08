FROM maven:3.8-openjdk-17-slim AS build


RUN apt-get update && \
    apt-get install -y apt-utils && \
    apt-get install -y gnupg && \
    apt-get install -y wget && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*


RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list
RUN apt-get update && apt-get -y install google-chrome-stable


WORKDIR /app

COPY pom.xml /app/
COPY src /app/src
RUN mvn clean package









FROM openjdk:17-slim

RUN apt-get update && \
    apt-get install -y apt-utils && \
    apt-get install -y gnupg && \
    apt-get install -y wget && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*


RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list
RUN apt-get update && apt-get -y install google-chrome-stable


WORKDIR /app


# Copy the JAR file from the build stage
COPY --from=build /app/target/* /app/


RUN apt-get update && apt-get install -y \
    fontconfig \
    unzip \
    && rm -rf /var/lib/apt/lists/*

COPY ./assets/ ./assets/*
COPY install-fonts.sh /usr/local/bin/install-fonts.sh
RUN chmod +x /usr/local/bin/install-fonts.sh
RUN /usr/local/bin/install-fonts.sh



COPY ./cache/* /app/cache/
COPY auth /app/

ENTRYPOINT ["java", "-cp", "/app/CVForYou-1.0-SNAPSHOT-jar-with-dependencies.jar", "MK.CVForYou.App"]

