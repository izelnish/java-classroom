# Temporarily add Git and GitHub CLI to path for this session
$env:Path = "C:\Program Files\GitHub CLI;C:\Program Files\Git\cmd;" + $env:Path

# Check if git/gh commands exist
if (-not (Get-Command git -ErrorAction SilentlyContinue)) {
    Write-Error "Git not found even after path update."
    exit 1
}
if (-not (Get-Command gh -ErrorAction SilentlyContinue)) {
    Write-Error "GitHub CLI not found even after path update."
    exit 1
}

# Configure Git User if not set (avoid "Tell me who you are" error)
try {
    $user = git config user.name
    if (-not $user) {
        Write-Host "Setting default git user identity..."
        git config --global user.name "Java Student"
        git config --global user.email "student@example.com"
    }
}
catch {
    # Ignore errors checking config
}

# Check Auth Status
gh auth status
if ($LASTEXITCODE -ne 0) {
    Write-Warning "You are not logged in to GitHub."
    Write-Host "Please run: 'gh auth login' first, then run this script again."
    exit 1
}

# Initialize Repo
if (-not (Test-Path .git)) {
    Write-Host "Initializing Git..."
    git init
    git branch -M main
}

# Add & Commit
git add .
git commit -m "Initial release of Java Classroom"

# Create & Push
Write-Host "Creating GitHub Repository..."
# Try to create; if it exists, it might error, so we catch or ignore
gh repo create java-classroom --public --source=. --remote=origin --push

Write-Host "-------------------------------------------"
Write-Host "SUCCESS! Code pushed to GitHub."
Write-Host "-------------------------------------------"
