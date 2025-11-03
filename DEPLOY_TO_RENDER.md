# Deploy ScrapSail Backend to Render

## Quick Deployment Steps

### Option 1: Using Render Dashboard (Recommended)

1. **Connect GitHub Repository:**
   - Go to https://dashboard.render.com
   - Click "New +" → "Web Service"
   - Connect your GitHub account
   - Select repository: `Likesh1235/scrapsail-backend`
   - Select branch: `main`

2. **Configure Service:**
   - **Name:** `scrapsail-backend`
   - **Region:** `Singapore`
   - **Branch:** `main`
   - **Root Directory:** (leave empty)
   - **Runtime:** `Java 17`
   - **Build Command:** `./mvnw clean package -DskipTests`
   - **Start Command:** `java -jar target/*.jar`
   - **Plan:** `Free`

3. **Set Environment Variables:**
   Click "Advanced" → "Environment Variables" and add:
   ```
   SPRING_PROFILES_ACTIVE=prod
   MYSQL_URL=jdbc:mysql://scrapsaildb-scrapsaildb.e.aivencloud.com:22902/defaultdb?sslMode=REQUIRED
   DB_USERNAME=avnadmin
   DB_PASSWORD=AVNS_q3bA1ATbxyymPpRXPIY
   SERVER_PORT=8080
   ```

4. **Enable Auto-Deploy:**
   - Check "Auto-Deploy" is enabled
   - Click "Create Web Service"

5. **Wait for Deployment:**
   - First deployment takes 5-10 minutes
   - Monitor logs in Render dashboard
   - Service URL will be displayed when ready

### Option 2: Using render.yaml (Blueprint)

1. **Commit render.yaml to your repository** (already done)

2. **Go to Render Dashboard:**
   - Navigate to https://dashboard.render.com
   - Click "New +" → "Blueprint"
   - Connect your GitHub repository
   - Render will automatically detect `render.yaml`

3. **Review and Deploy:**
   - Render will read the configuration from `render.yaml`
   - Click "Apply" to create the service

### Verify Deployment

1. **Check Health Endpoint:**
   ```
   https://your-service-name.onrender.com/health
   ```
   Should return: `{"status":"UP"}`

2. **Test API:**
   ```
   https://your-service-name.onrender.com/api/auth/login
   ```

## Troubleshooting

### Build Fails
- Check Render logs for Maven errors
- Ensure Java 17 runtime is selected
- Verify `mvnw` files are committed to repo

### Database Connection Fails
- Verify environment variables are set correctly
- Check Aiven MySQL is accessible from Render's IP
- Test connection string format

### Service Won't Start
- Check logs for Java errors
- Verify JAR file is created in `target/` directory
- Ensure PORT environment variable is set (Render sets this automatically)

### Port Issues
- Render sets PORT automatically - don't hardcode 8080
- Update application.properties to use `${PORT}` variable

## Important Notes

⚠️ **Security:** DB_PASSWORD is hardcoded in render.yaml for convenience. In production:
- Use Render's secret environment variables
- Mark sensitive vars as `sync: false` in render.yaml
- Or set them manually in Render dashboard

## Auto-Deploy

Once configured, Render will automatically deploy on every push to the `main` branch.

