$env:Path = "C:\Program Files\GitHub CLI;C:\Program Files\Git\cmd;" + $env:Path
Write-Host "Starting GitHub Login..."
gh auth login
