# Stage 1: Build application
FROM openjdk:17 as build

# Set current working directory inside the image
WORKDIR /opt/app

COPY . .

# Package the application
RUN ./mvnw install -DskipTests

CMD ["./mvnw", "spring-boot:run"]