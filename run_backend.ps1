$jdkPath = "C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot\bin"
$mavenPath = "$PSScriptRoot\apache-maven\bin"

# Add to PATH temporarily
$env:Path = "$jdkPath;$mavenPath;" + $env:Path

Write-Host "Checking versions..."
java -version
mvn -version

Write-Host "Starting Spring Boot Server..."
mvn spring-boot:run
