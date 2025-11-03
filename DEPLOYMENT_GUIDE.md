# ScrapSail Deployment Guide

## 🚀 Quick Deployment Steps

### 1. Set Environment Variables

#### Windows (PowerShell):
```powershell
$env:DB_HOST="localhost"
$env:DB_PORT="3306"
$env:DB_NAME="scrapsail"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_password"
$env:MAIL_USERNAME="your_email@gmail.com"
$env:MAIL_PASSWORD="your_app_password"
$env:LOG_LEVEL="INFO"
```

#### Linux/Mac:
```bash
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=scrapsail
export DB_USERNAME=root
export DB_PASSWORD=your_password
export MAIL_USERNAME=your_email@gmail.com
export MAIL_PASSWORD=your_app_password
export LOG_LEVEL=INFO
```

### 2. Build and Run

#### Build the project:
```bash
mvn clean package -DskipTests
```

#### Run the JAR:
```bash
java -jar target/scrapsail-backend-0.0.1-SNAPSHOT.jar
```

### 3. Production Deployment Options

#### Option A: Docker (Recommended)
Create a `Dockerfile`:
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/scrapsail-backend-0.0.1-SNAPSHOT.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
```

Build and run:
```bash
docker build -t scrapsail-backend .
docker run -p 8080:8080 \
  -e DB_HOST=your_db_host \
  -e DB_PASSWORD=your_password \
  -e MAIL_USERNAME=your_email \
  -e MAIL_PASSWORD=your_password \
  scrapsail-backend
```

#### Option B: Systemd Service (Linux)
Create `/etc/systemd/system/scrapsail.service`:
```ini
[Unit]
Description=ScrapSail Backend Service
After=network.target mysql.service

[Service]
Type=simple
User=your_user
Environment="DB_HOST=localhost"
Environment="DB_PASSWORD=your_password"
Environment="MAIL_USERNAME=your_email"
Environment="MAIL_PASSWORD=your_password"
ExecStart=/usr/bin/java -jar /path/to/scrapsail-backend-0.0.1-SNAPSHOT.jar
Restart=always

[Install]
WantedBy=multi-user.target
```

Then:
```bash
sudo systemctl enable scrapsail
sudo systemctl start scrapsail
```

#### Option C: Cloud Platforms

**Heroku:**
```bash
heroku config:set DB_HOST=your_host DB_PASSWORD=your_password
git push heroku main
```

**AWS/GCP/Azure:**
- Use their respective environment variable configuration
- Set all required environment variables in the platform's settings

## 📋 Environment Variables Reference

### Required Variables:
- `DB_HOST` - Database host (default: localhost)
- `DB_PASSWORD` - Database password (REQUIRED, no default)
- `MAIL_USERNAME` - Email username (default: empty)
- `MAIL_PASSWORD` - Email password (default: empty)

### Optional Variables:
- `DB_PORT` - Database port (default: 3306)
- `DB_NAME` - Database name (default: scrapsail)
- `DB_USERNAME` - Database username (default: root)
- `MAIL_HOST` - Mail server (default: smtp.gmail.com)
- `MAIL_PORT` - Mail port (default: 587)
- `LOG_LEVEL` - Logging level (default: INFO)
- `SERVER_PORT` - Server port (default: 8080)

## ✅ Security Checklist

- [x] Hardcoded passwords removed
- [x] Environment variables configured
- [x] Production logging levels set
- [ ] SSL/HTTPS configured
- [ ] Database credentials secured
- [ ] Email credentials secured
- [ ] CORS configured for production domain

## 🔒 Security Best Practices

1. **Never commit `.env` files** to version control
2. **Use strong passwords** for database and email
3. **Rotate credentials** regularly
4. **Use SSL/TLS** for database connections in production
5. **Enable HTTPS** for the application
6. **Restrict database access** to application server only
7. **Use secrets management** services (AWS Secrets Manager, HashiCorp Vault, etc.)

## 🐛 Troubleshooting

### Database Connection Failed:
- Check `DB_HOST`, `DB_PORT`, `DB_USERNAME`, `DB_PASSWORD` are set correctly
- Verify MySQL is running and accessible
- Check firewall rules

### Email Not Sending:
- Verify `MAIL_USERNAME` and `MAIL_PASSWORD` are correct
- For Gmail, use App Password (not regular password)
- Check firewall allows SMTP connections

### Application Won't Start:
- Check all required environment variables are set
- Verify database exists and is accessible
- Check logs for specific error messages

## 📞 Support

For deployment issues, check:
1. Application logs
2. Database connectivity
3. Environment variables are set correctly
4. Port 8080 is available


