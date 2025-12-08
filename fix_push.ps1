$env:Path = "C:\Program Files\GitHub CLI;C:\Program Files\Git\cmd;" + $env:Path

Write-Host "Resetting last commit to remove secrets from history..."
git reset --soft HEAD~1

Write-Host "Re-staging files..."
git add .

Write-Host "Re-committing..."
git commit -m "Initial release of Java Classroom"

Write-Host "Pushing to GitHub..."
git push origin main
