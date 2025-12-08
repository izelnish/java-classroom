$env:Path = "C:\Program Files\GitHub CLI;C:\Program Files\Git\cmd;" + $env:Path

Write-Host "Removing existing Git setup..."
Remove-Item -Path .git -Recurse -Force -ErrorAction SilentlyContinue

Write-Host "Initializing new Git repository..."
git init
git branch -M main

Write-Host "Adding files..."
git add .

Write-Host "Committing..."
git commit -m "Initial release of Java Classroom"

Write-Host "Adding Remote..."
git remote add origin https://github.com/izelnish/java-classroom.git

Write-Host "Force Pushing..."
git push --force origin main -u
