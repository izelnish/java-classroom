$env:Path = "C:\Program Files\GitHub CLI;C:\Program Files\Git\cmd;" + $env:Path
Write-Host "--- REMOTES ---"
git remote -v
Write-Host "--- BRANCHES ---"
git branch
Write-Host "--- STATUS ---"
git status
Write-Host "--- PUSH ATTEMPT ---"
git push origin main 2>&1 | Write-Host
