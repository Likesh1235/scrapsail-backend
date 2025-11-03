# 🔧 Railway Deployment Troubleshooting

## Issue: 404 Not Found on `/health` endpoint

This usually means:
1. Service isn't deployed yet
2. Service failed to start
3. Build failed
4. Environment variables missing
5. Wrong domain/proxy configuration

---

## 🔍 Step 1: Check Railway Deployment Status

1. Go to **Railway Dashboard**
2. Click on your **backend service**
3. Check the **"Deployments"** tab

### Look for:
- ✅ **Green checkmark** = Deployed successfully
- ⚠️ **Yellow/Orange** = Deploying
- ❌ **Red X** = Failed

---

## 🔍 Step 2: Check Build Logs

1. Click on the latest **deployment**
2. Check **"Build Logs"**
3. Look for errors

### What to check:
- ✅ `BUILD SUCCESS` in logs
- ✅ `Started BackendApplication in X.XXX seconds`
- ❌ Any error messages

---

## 🔍 Step 3: Check Runtime Logs

1. Go to your service → **"Logs"** tab
2. Check for:
   - Application startup messages
   - Database connection errors
   - Port binding errors

### Common Errors:

#### Database Connection Error:
```
Cannot create PoolableConnectionFactory
```
**Solution:** Check MySQL environment variables

#### Port Already in Use:
```
Port 8080 is already in use
```
**Solution:** Railway handles this automatically, but check PORT env var

#### Application Failed to Start:
```
Failed to start application
```
**Solution:** Check logs for specific error

---

## 🔍 Step 4: Verify Environment Variables

Go to your backend service → **"Variables"** tab

### Required Variables:
- [ ] `MYSQLHOST` = `hopper.proxy.rlwy.net`
- [ ] `MYSQLPORT` = `51116`
- [ ] `MYSQLDATABASE` = `railway`
- [ ] `MYSQLUSER` = `root`
- [ ] `MYSQLPASSWORD` = `MoxMmvgySDSiKQceRvMQREvioTGxmzOZ`
- [ ] `PORT` = (Railway sets this automatically, don't override)

---

## 🔍 Step 5: Check Service Health

1. In Railway dashboard → Your service
2. Check **"Metrics"** tab
3. Look for:
   - CPU usage
   - Memory usage
   - Network traffic

If all are 0, the service might not be running.

---

## 🔍 Step 6: Verify Domain Configuration

1. Go to **Settings** → **Networking**
2. Check if domain is **provisioned**
3. If not, click **"Generate Domain"**

---

## 🔧 Common Fixes

### Fix 1: Rebuild and Redeploy
1. In Railway, click **"Redeploy"**
2. Watch build logs
3. Wait for successful deployment

### Fix 2: Check Procfile
Ensure `Procfile` exists with:
```
web: java -jar target/*.jar
```

### Fix 3: Verify Build Command
Railway should use: `mvn clean package -DskipTests`

### Fix 4: Check Application Port
Your app should listen on `${PORT}` (Railway sets this automatically)

---

## 📋 Quick Diagnostic Checklist

- [ ] Service is deployed (green status)
- [ ] Build logs show `BUILD SUCCESS`
- [ ] Runtime logs show `Started BackendApplication`
- [ ] All 5 MySQL environment variables are set
- [ ] Domain is generated and active
- [ ] No errors in logs

---

## 🚨 If Still Not Working

Share with me:
1. **Build logs** screenshot/error
2. **Runtime logs** screenshot/error
3. **Environment variables** list (names only, not values)
4. **Service status** (deployed/failed/deploying)

Then I can help debug the specific issue!

