<?php
require_once 'config.php';

// SQL to create table
$sql = "CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('student', 'staff') DEFAULT 'student'
)";

if ($conn->query($sql) === TRUE) {
    echo "Table 'users' created successfully or already exists.";
} else {
    echo "Error creating table: " . $conn->error;
}
$conn->close();
?>
