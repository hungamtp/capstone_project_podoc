FROM maven:3.8.2-jdk-11
WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests
EXPOSE 8080
CMD mvn spring-boot:run
