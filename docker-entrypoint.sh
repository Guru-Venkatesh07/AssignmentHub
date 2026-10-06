#!/bin/bash
set -e

echo "=== Starting Assignment Management System Container ==="

# 1. If DB_HOST is not set or set to localhost, start embedded MariaDB/MySQL service
if [ -z "$DB_HOST" ] || [ "$DB_HOST" = "localhost" ] || [ "$DB_HOST" = "127.0.0.1" ]; then
    echo "[+] Starting local MySQL/MariaDB daemon..."
    service mariadb start || service mysql start

    echo "[+] Configuring database credentials..."
    mysql -u root -e "CREATE DATABASE IF NOT EXISTS assignment_manager CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" || true
    mysql -u root -e "ALTER USER 'root'@'localhost' IDENTIFIED BY 'root'; FLUSH PRIVILEGES;" || true

    echo "[+] Verifying database tables..."
    TABLE_COUNT=$(mysql -u root -proot -N -s -e "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema='assignment_manager' AND table_name='users';" 2>/dev/null || echo "0")
    
    if [ "$TABLE_COUNT" -eq "0" ]; then
        echo "[+] Applying database schema and seed data..."
        if [ -f /app/database/schema.sql ]; then
            mysql -u root -proot assignment_manager < /app/database/schema.sql
        fi
        if [ -f /app/database/seed.sql ]; then
            mysql -u root -proot assignment_manager < /app/database/seed.sql
        fi
        echo "[+] Local database initialized and seeded successfully!"
    else
        echo "[+] Database already contains schema and data ($TABLE_COUNT tables)."
    fi
else
    echo "[+] Using external database host: $DB_HOST"
fi

# 2. Dynamic PORT configuration for Render / Railway / Heroku
if [ -n "$PORT" ] && [ "$PORT" != "8080" ]; then
    echo "[+] Setting Tomcat HTTP port to $PORT..."
    sed -i "s/port=\"8080\"/port=\"$PORT\"/g" /usr/local/tomcat/conf/server.xml
fi

echo "[+] Starting Apache Tomcat on port ${PORT:-8080}..."
exec catalina.sh run
