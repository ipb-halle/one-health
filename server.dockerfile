# Use a base image with Maven installed
FROM maven:3.8.3-openjdk-17-slim AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml file to the working directory
COPY ./ontology-manager-server/pom.xml .

# Download the dependencies specified in pom.xml
RUN mvn dependency:go-offline -B

# Copy the source code to the working directory
COPY ./ontology-manager-server/src ./src

# Build the application
RUN mvn package

# Create a new image for running the application
FROM openjdk:17

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage to the new image
COPY --from=build /app/target/server.jar ./app.jar

# Command to run the application
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]
