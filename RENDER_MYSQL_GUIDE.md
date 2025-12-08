# How to Deploy MySQL on Render
Render does not offer a **free** hosted MySQL service. However, you can deploy your own MySQL instance using their Docker runtime and Persistent Disks.
**Cost:** This method requires a paid plan because Persistent Disks cost (~$0.25/GB/month) and Private Services run on paid compute.

## Step 1: Create a GitHub Repository for MySQL
1. Go to [https://github.com/render-examples/mysql](https://github.com/render-examples/mysql)
2. Click **"Use this template"** -> **Create a new repository**.
3. Name it `my-render-mysql` and keep it Public or Private.

## Step 2: Create a Private Service on Render
1. Go to the [Render Dashboard](https://dashboard.render.com).
2. Click **New +** -> **Private Service**.
3. Connect the repository you just created (`my-render-mysql`).

## Step 3: Configure Service
*   **Name:** `mysql-db`
*   **Runtime:** Docker
*   **Instance Type:** Starter (Paid) - *Free instance not supported for Private Services*.
*   **Environment Variables:**
    *   `MYSQL_ROOT_PASSWORD`: (Generate a strong password)
    *   `MYSQL_DATABASE`: `java_classroom`
    *   `MYSQL_USER`: `app_user`
    *   `MYSQL_PASSWORD`: (Generate a strong user password)

## Step 4: Add Persistent Disk (Crucial)
1. On the setup page, creating a disk is required for data to survive restarts.
2. **Mount Path:** `/var/lib/mysql`
3. **Size:** 1 GB (or more if needed).
4. Click **Create Private Service**.

## Step 5: Connect Backend to MySQL
Once the service is running, Render provides an internal hostname.
1. Go to your Java Backend service on Render.
2. Add/Update Environment Variables:
    *   `DB_URL`: `jdbc:mysql://mysql-db:3306/java_classroom`
        *(Note: `mysql-db` is the service name you chose in Step 3)*
    *   `DB_USER`: `app_user`
    *   `DB_PASS`: (The password you set in Step 3)

## Important Note
Since this method costs money, we highly recommend using **Aiven.io** or **TiDB Cloud** which offer truly free MySQL tiers, as described in `DEPLOYMENT_GUIDE.md`.
