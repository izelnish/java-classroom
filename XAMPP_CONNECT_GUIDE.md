# Connecting to XAMPP MySQL with Java

You have already created the `java_classroom` database. Here are the remaining steps to get your Java code connecting to it.

## 1. Download the JDBC Driver
Java needs a specific "driver" library to talk to MySQL.
1.  Go to the [MySQL Connector/J Download Page](https://dev.mysql.com/downloads/connector/j/).
2.  Select "Platform Independent".
3.  Download the ZIP archive.
4.  Extract the ZIP. You are looking for a file named something like `mysql-connector-j-8.x.x.jar`.

## 2. Add Driver to Your Project
### If using VS Code:
1.  Find the "Java Projects" section in the sidebar (or look for "Referenced Libraries" in the explorer if you have the Java extension pack).
2.  Click the `+` icon to add a library.
3.  Select the `mysql-connector-j-8.x.x.jar` file you downloaded.

### If running from Command Line:
I have already downloaded the driver for you (`mysql-connector-j-8.3.0.jar`). Run the following commands:

```bash
# Compile
javac DatabaseConnection.java

# Run
java -cp ".;mysql-connector-j-8.3.0.jar" DatabaseConnection
```

## Troubleshooting: 'javac' is not recognized
If you see an error saying `javac` is not recognized, it means you don't have the Java Development Kit (JDK) installed or configured correctly.

### Solution 1: Install JDK (Recommended)
1.  Download **Eclipse Temurin JDK 17** (LTS) from [adoptium.net](https://adoptium.net/).
2.  Run the installer.
3.  **IMPORTANT:** During installation, make sure to select the option **"Add to PATH"** (it might be a dropdown feature you have to enable).
4.  Restart your terminal (Powershell/VS Code) and try again.

### Solution 2: Fix PATH (If already installed)
If you have Java installed but it still fails:
1.  Search for "System Environment Variables" in Windows Search.
2.  Click "Environment Variables".
3.  Under "System variables", find `Path` and click "Edit".
4.  Add the path to your JDK `bin` folder (e.g., `C:\Program Files\Java\jdk-17\bin`).
5.  Click OK and restart your terminal.

## 3. Verify Connection
1.  Ensure XAMPP Apache and MySQL modules are RUNNING.
2.  Run the `DatabaseConnection.java` file.
3.  You should see: `Successfully connected to the database!`
