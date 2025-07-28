# XPHR - Employee Time Tracking & Reporting System

XPHR is a Spring Boot application for managing employee time records, generating reports, and securing access via role-based Spring Security.

---

## Features

- Time tracking for employees
- Report generation by admin
- Role-based access (Admin / Employee)
- JSP-based UI (Thymeleaf optional)
- PostgreSQL backend
- Redis support

---

## Installation

### Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL
- Redis
- Optional: IntelliJ IDEA / VS Code

### Clone / Extract Project

If cloning from Git:

```bash
git clone https://github.com/your-org/xphr.git
cd xphr
```

### Setup DB 

```sql
CREATE DATABASE xphr_db;
CREATE USER xphr_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE xphr_db TO xphr_user;
```

You can use the sql script : 
```sql
dump-xphr_test-202507271708.sql
```

#### Update your src/main/resources/application.properties
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/xphr_db
spring.datasource.username=xphr_user
spring.datasource.password=your_password
```

## How to Run
Build the project : 
```bash
mvn clean install
```

Run the application : 
```bash
mvn spring-boot:run
```

RUn The Unit Test : 
```bash
mvn test
```

Access The reporting Page : 
```bash
http://localhost:8080/report?start=2025-05-01T00:00:00&end=2025-08-31T23:59:59
```

### Login Credential : 
#### Admin 
```
username : admin
password : admin123
```

#### Employee
```
username : lee
password : employee123
```

## Architecture Overview

### Database Optimization Strategy 
Please see this docs : https://docs.google.com/document/d/1cickrGacpb6zSOvqHDOSQ_oRv_kZVpEpNC0xOqsxcSY/edit?usp=sharing 

### Security Note
- A custom UserDetailsService implementation loads users from the database and maps their roles to GrantedAuthority
- ```new UsernamePasswordAuthenticationToken(user, password, authorities);```
- Security Configuration HttpSecurity is configured to authorize requests based on roles:
```java
http
    .authorizeHttpRequests(auth -> auth
        .requestMatchers("/admin/**").hasRole("ADMIN")
        .requestMatchers("/employee/**").hasRole("EMPLOYEE")
        .anyRequest().authenticated()
    )
    .formLogin()
    .and()
    .logout();
```

## Any assumptions or design decisions made during development :

### Using Cache Engne HTTP Server (https://github.com/AnggaBS86/cache-engine-httpserver) instead of Redis for some advantages : 
- Easier setup — no need to install Redis, manage ports, or configure persistent storage. Just run the Go-based HTTP cache server.
- Has lower memory footprint than Redis for small apps.
- Since it likely uses an in-memory map (or BigCache), it's very fast for local access.
- Easily modify the caching policy, TTL, eviction, or add observability tailored to our needs.
- Can be language-agnostic (Spring Boot, Node.js, etc.) through simple HTTP calls

COde example : 
```java
@Service
public class CacheClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://localhost:8081"; // your Go cache server

    public void put(String key, String value) {
        restTemplate.postForEntity(baseUrl + "/set?key=" + key, value, Void.class);
    }

    public String get(String key) {
        try {
            return restTemplate.getForObject(baseUrl + "/get?key=" + key, String.class);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        }
    }
}
```


