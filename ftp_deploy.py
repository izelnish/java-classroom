import ftplib
import os

# Configuration
FTP_HOST = "ftpupload.net"
FTP_USER = "if0_40644146"
FTP_PASS = "nish9433"
LOCAL_DIR = r"d:\Java classroom"
REMOTE_DIR = "htdocs"

def upload_file(ftp, local_path, remote_path):
    try:
        with open(local_path, "rb") as f:
            ftp.storbinary(f"STOR {remote_path}", f)
        print(f"Uploaded {local_path} -> {remote_path}")
    except Exception as e:
        print(f"Failed to upload {local_path}: {e}")

def main():
    try:
        # Connect to FTP
        print(f"Connecting to {FTP_HOST}...")
        ftp = ftplib.FTP(FTP_HOST)
        ftp.login(FTP_USER, FTP_PASS)
        print("Logged in successfully.")

        try:
            ftp.cwd(REMOTE_DIR)
            print(f"Changed directory to {REMOTE_DIR}")
        except ftplib.error_perm:
            print(f"Could not change directory to {REMOTE_DIR}, using root.")
            pass

        # Upload index.html
        local_index = os.path.join(LOCAL_DIR, "index.html")
        if os.path.exists(local_index):
            upload_file(ftp, local_index, "index.html")

        # Handle PHP directory
        php_dir = "php"
        try:
            ftp.mkd(php_dir)
        except:
            pass
        
        ftp.cwd(php_dir)
        local_php_dir = os.path.join(LOCAL_DIR, "php")
        if os.path.exists(local_php_dir):
            for filename in os.listdir(local_php_dir):
                if filename.endswith(".php"):
                    local_path = os.path.join(local_php_dir, filename)
                    upload_file(ftp, local_path, filename)
        
        ftp.quit()
        print("All files uploaded successfully.")

    except Exception as e:
        print(f"An error occurred: {e}")

if __name__ == "__main__":
    main()
