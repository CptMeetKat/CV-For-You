FROM maven:3.6.3-openjdk-17-slim AS build

WORKDIR /app

COPY pom.xml /app/
COPY src /app/src
COPY sample_components/ /app/sample_components
RUN mvn clean package

# Runtime stage: Use a minimal OpenJDK image
FROM openjdk:17-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/* /app/
COPY auth /app/


COPY ./cache/* /app/cache/

# Set the entry point
ENTRYPOINT ["java", "-cp", "/app/CVForYou-1.0-SNAPSHOT-jar-with-dependencies.jar", "MK.CVForYou.App"]
