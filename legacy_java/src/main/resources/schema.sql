CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('student', 'staff') DEFAULT 'student'
);

-- Insert a test user
INSERT IGNORE INTO users (name, email, password, role) VALUES 
('Test Student', 'student@example.com', 'password123', 'student'),
('Test Staff', 'staff@example.com', 'admin123', 'staff');
