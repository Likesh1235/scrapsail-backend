# ScrapSail Backend

Spring Boot backend application for ScrapSail - a scrap management and recycling platform.

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+
- MySQL 8.0+

### Starting the Backend

1. **Ensure MySQL is running:**
   ```powershell
   Start-Service MySQL80
   ```

2. **Start the backend:**
   ```powershell
   mvn spring-boot:run
   ```

   Or use the integrated startup script:
   ```powershell
   .\START_PROJECT.ps1
   ```
   This will start both backend and frontend automatically.

3. **Verify backend is running:**
   - Health check: http://localhost:8080/health
   - Should return: `{"status":"UP"}`

## 🔧 Configuration

### Database Configuration
Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/scrapsail
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### Email Configuration (Optional)
To enable email sending, configure Gmail SMTP in `application.properties`:

```properties
spring.mail.username=your-email@gmail.com
spring.mail.password=YOUR_APP_PASSWORD
```

## 📋 Default Credentials

After backend starts, these accounts are automatically created:

- **Admin:**
  - Email: `admin@scrapsail.com`
  - Password: `admin123`

- **Collector:**
  - Email: `collector@scrapsail.com`
  - Password: `collector123`

## 🔌 API Endpoints

- **Health:** `GET /health`
- **Login:** `POST /api/auth/login`
- **Admin Login:** `POST /api/auth/admin-login`
- **Collector Login:** `POST /api/auth/collector-login`
- **Register:** `POST /api/auth/register`
- **OTP Send:** `POST /api/otp/send`
- **OTP Verify:** `POST /api/otp/verify`

## 🧪 Testing

Run tests:
```powershell
mvn test
```

## 📦 Building

Build JAR file:
```powershell
mvn clean package
```

Run JAR:
```powershell
java -jar target/scrapsail-backend-0.0.1-SNAPSHOT.jar
```

## 🌐 Port Configuration

Default port: **8080**

Change in `application.properties`:
```properties
server.port=8080
```

## 🔒 CORS Configuration

CORS is configured for these origins:
- http://localhost:3000
- http://localhost:3001
- http://localhost:5173

To add more, edit `src/main/java/com/scrapsail/backend/config/CorsConfig.java`

## 📝 Project Structure

```
src/
├── main/
│   ├── java/com/scrapsail/backend/
│   │   ├── config/          # Configuration (CORS, Security)
│   │   ├── controller/      # REST Controllers
│   │   ├── service/         # Business Logic
│   │   ├── repository/      # Data Access
│   │   ├── model/           # Entity Models
│   │   └── util/            # Utilities (DataInitializer)
│   └── resources/
│       └── application.properties
└── test/
    └── java/                # Test classes
```

## 🛠️ Technology Stack

- **Framework:** Spring Boot 3.3.4
- **Database:** MySQL 8.0
- **ORM:** JPA/Hibernate
- **Security:** Spring Security with JWT
- **Build Tool:** Maven

## 📞 Support

For issues or questions, check the backend console logs or test the health endpoint.
