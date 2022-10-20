# Stage 1: Build application
FROM openjdk:17 as build

# Set current working directory inside the image
WORKDIR /opt/app

COPY . .

# Build all dependencies in preparation to go offline
RUN ./mvnw dependency:go-offline -B

# Package the application
RUN ./mvnw package -DskipTests

ENTRYPOINT ["./mvnw", "spring-boot:run"]