# ====================================================================
# Stage 1: Build Maven WAR
# ====================================================================
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B || true
COPY src ./src
COPY database ./database
RUN mvn clean package -DskipTests

# ====================================================================
# Stage 2: Tomcat + MariaDB Container (Self-Contained for Render/Railway)
# ====================================================================
FROM tomcat:10.1-jdk17-temurin
LABEL maintainer="Guru-Venkatesh07"

# Install MariaDB (MySQL wire-compatible)
RUN apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends \
        mariadb-server \
        mariadb-client \
        dos2unix && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY database/ ./database/
COPY docker-entrypoint.sh /docker-entrypoint.sh
RUN dos2unix /docker-entrypoint.sh && chmod +x /docker-entrypoint.sh

# Remove default Tomcat webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy built WAR as ROOT application
COPY --from=build /app/target/assignment-submission-manager.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

ENTRYPOINT ["/docker-entrypoint.sh"]
