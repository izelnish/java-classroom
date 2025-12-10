<?php
// Database Configuration
// Setup these variables to match your InfinityFree MySQL Details
$servername = "sql206.infinityfree.com"; 
$username = "if0_40644146"; 
$password = "nish9433"; 
$dbname = "if0_40644146_java_classroom";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die(json_encode(["success" => false, "message" => "Connection failed: " . $conn->connect_error]));
}
?>
