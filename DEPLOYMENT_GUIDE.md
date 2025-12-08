# Free Global Hosting Guide (Java + MySQL)

Hosting Java applications for free is a bit trickier than other languages, but it is possible!

We will use:
1.  **Render.com** (to host the Java Backend).
2.  **TiDB Cloud** or **Aiven** (to host the free MySQL Database).

---

## Prerequisite: GitHub
You must push your code to **GitHub** first.
You need to create your `users` table in this new cloud database.
1.  In the standard TiDB Cloud dashboard, find the **"SQL Editor"** or **"Chat2Query"** tab on the left.
2.  Copy the content from your local file `d:\Java classroom\src\main\resources\schema.sql`.
    *(Constraint: It is essentially this SQL)*:
    ```sql
    CREATE TABLE IF NOT EXISTS users (
        id INT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        email VARCHAR(255) NOT NULL UNIQUE,
        password VARCHAR(255) NOT NULL,
        role ENUM('student', 'staff') DEFAULT 'student'
    );
    ```
3.  Paste it into the TiDB SQL Editor and click **Run**.
4.  Success! Your cloud database now has the table.

---

## Step 2: Deploy Backend to Render
1.  Sign up at [Render.com](https://render.com/).
2.  Click **New +** -> **Web Service**.
3.  Connect your GitHub repository.
4.  **Settings:**
    *   **Runtime:** Docker (It will detect the `Dockerfile` I just created for you).
    *   **Instance Type:** Free.
5.  **Environment Variables** (Click "Advanced" or "Environment"):
    You MUST add these so your cloud app knows to connect to the cloud DB, not localhost.
    *   `DB_URL` = `jdbc:mysql://<your-cloud-db-host>:<port>/<db_name>`
    *   `DB_USER` = `<your-cloud-db-user>`
    *   `DB_PASS` = `<your-cloud-db-password>`
6.  Click **Create Web Service**.

Render will now build your Docker container. It takes about 3-5 minutes. Once green, it will give you a URL like `https://my-java-app.onrender.com`.

---

## Step 3: Update Frontend
Your `index.html` is currently trying to hit `localhost:8080`.
1.  Open `index.html`.
2.  Find `fetch('http://localhost:8080/api/login', ...)`
3.  Change it to your new Render URL: `fetch('https://my-java-app.onrender.com/api/login', ...)`
4.  Push the change to GitHub.
5.  Render will auto-redeploy.

---

## Step 4: Host Frontend on Vercel (Recommended)
You asked about Vercel. **Vercel is great for the Frontend**, but it **cannot** host standard Java Spring Boot Backends (those need a running server).

**The Strategy: Split Hosting**
-   **Backend (Java):** hosted on **Render** (as done in Step 2).
-   **Frontend (HTML/JS):** hosted on **Vercel**.

### Instructions:
1.  **Prepare Frontend:**
    *   Make sure you updated `index.html` with your **Render Backend URL** (from Step 3).
    *   Create a file named `vercel.json` in your project root with this content:
        ```json
        {
          "version": 2,
          "builds": [{ "src": "index.html", "use": "@vercel/static" }]
        }
        ```
2.  **Deploy to Vercel:**
    *   Go to [Vercel.com](https://vercel.com) and Sign Up.
    *   Click **"Add New..."** -> **Project**.
    *   Import your GitHub Repository.
    *   **Root Directory:** If your `index.html` is in the root, just click Deploy. If it's in a subfolder, select that folder.
    *   Click **Deploy**.

Vercel will give you a URL like `https://java-classroom.vercel.app`.
Now you have a professional setup:
*   Frontend: Super fast on Vercel CDN.
*   Backend: Java Spring Boot running on Render.

