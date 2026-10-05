# Deployment & Setup Guide

## 1. Prerequisites & Environment

- **Java Development Kit (JDK)**: OpenJDK / Oracle JDK 17+ (Java 17, 21, or higher)
- **Servlet Container / Application Server**: Apache Tomcat 10.1+ (Supports Jakarta EE 10 / Servlet 6.0)
- **Database**: MySQL Server 8.0+ & MySQL Workbench
- **Build Tool**: Apache Maven 3.8+
- **IDE (Optional)**: Eclipse IDE for Enterprise Java and Web Developers / IntelliJ IDEA

---

## 2. Step 1: Database Setup in MySQL Workbench

1. Launch **MySQL Workbench** and connect to your local MySQL instance (`localhost:3306`).
2. Open `database/schema.sql` (`File` $\rightarrow$ `Open SQL Script...`).
3. Click the **Execute (Lightning bolt)** icon to create the `assignment_manager` database and all relational tables with indexes.
4. Open `database/seed.sql` and execute it to load demo users, subjects, assignments, submissions, and evaluations.
5. In the SQL editor, run:
   ```sql
   USE assignment_manager;
   SELECT * FROM users;
   SELECT * FROM assignments;
   ```

---

## 3. Step 2: Configure Database Credentials

Edit `src/main/resources/db.properties` with your local MySQL username and password:

```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/assignment_manager?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
db.username=root
db.password=YOUR_MYSQL_PASSWORD
```

*(Alternatively, you can set environment variables: `DB_HOST=localhost`, `DB_NAME=assignment_manager`, `DB_USER=root`, `DB_PASSWORD=yourpass`)*

---

## 4. Step 3: Build Maven WAR Package

Open a terminal/command prompt in the root project directory:

```bash
mvn clean package
```

The compiled and assembled WAR file will be generated at:
```
target/assignment-submission-manager.war
```

---

## 5. Step 4: Deploy to Apache Tomcat 10.1+

### Option A: Standalone Tomcat Deployment
1. Copy `target/assignment-submission-manager.war` into your Tomcat installation's `webapps/` folder:
   ```
   C:\apache-tomcat-10.1.x\webapps\assignment-submission-manager.war
   ```
2. Start Tomcat by executing `bin/startup.bat` (Windows) or `bin/startup.sh` (Linux/macOS).
3. Access the application in your browser:
   ```
   http://localhost:8080/assignment-submission-manager/
   ```

### Option B: Running in Eclipse IDE
1. Open **Eclipse IDE for Enterprise Java and Web Developers**.
2. Click `File` $\rightarrow$ `Import...` $\rightarrow$ `Maven` $\rightarrow$ `Existing Maven Projects`.
3. Browse and select the project root directory containing `pom.xml`.
4. Right-click on the project in *Project Explorer* $\rightarrow$ `Run As` $\rightarrow$ `Run on Server`.
5. Select **Apache Tomcat v10.1 Server** and click **Finish**.

---

## 6. Demo Accounts for Lab Presentation

| Role | Email | Password | Pre-seeded Details |
|---|---|---|---|
| **Student** | `student@example.com` | `student123` | Name: Guru V, Reg No: `23CS088`, 3rd Year CSE |
| **Faculty** | `faculty@example.com` | `faculty123` | Name: Dr. Rajesh Kumar, CSE Department |
