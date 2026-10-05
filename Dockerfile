# Stage 1: Build Maven WAR
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run Tomcat with WAR
FROM tomcat:10.1-jdk17-temurin
LABEL maintainer="Guru-Venkatesh07"

# Remove default Tomcat webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy built WAR as ROOT application
COPY --from=build /app/target/assignment-submission-manager.war /usr/local/tomcat/webapps/ROOT.war

# Expose Tomcat HTTP Port
EXPOSE 8080

CMD ["catalina.sh", "run"]
