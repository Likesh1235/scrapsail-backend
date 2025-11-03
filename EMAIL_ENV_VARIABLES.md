# 📧 Email Environment Variables for Render

## Required Environment Variables

Add these to your Render Dashboard → Settings → Environment Variables:

| Key | Value |
|-----|-------|
| `EMAIL_USERNAME` | `likeshkanna74@gmail.com` |
| `EMAIL_PASSWORD` | `rvou eevk bdwt iizl` |
| `EMAIL_HOST` | `smtp.gmail.com` (optional, this is the default) |
| `EMAIL_PORT` | `587` (optional, this is the default) |

---

## How to Add in Render

1. Go to: **Render Dashboard** → **Your Service** → **Settings** → **Environment Variables**
2. Click **"Add Environment Variable"**
3. Add each variable:

**Variable 1:**
- **Key:** `EMAIL_USERNAME`
- **Value:** `likeshkanna74@gmail.com`

**Variable 2:**
- **Key:** `EMAIL_PASSWORD`
- **Value:** `rvou eevk bdwt iizl`

**Variable 3 (Optional - defaults are already set):**
- **Key:** `EMAIL_HOST`
- **Value:** `smtp.gmail.com`

**Variable 4 (Optional - defaults are already set):**
- **Key:** `EMAIL_PORT`
- **Value:** `587`

4. Click **"Save Changes"**

---

## Verification

After adding these variables and redeploying, your email service should work. The application will use these environment variables instead of hardcoded values.

**Note:** These credentials are stored securely in Render's environment variables and are NOT in your code repository.

