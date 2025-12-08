$env:Path = "C:\Program Files\GitHub CLI;C:\Program Files\Git\cmd;" + $env:Path

Write-Host "Resetting history to remove potential secrets..."
git reset --soft HEAD~2

Write-Host "Staging files..."
git add .

Write-Host "Creating clean commit..."
git commit -m "Initial release of Java Classroom (Clean)"

Write-Host "Force pushing to GitHub..."
git push --force origin main
