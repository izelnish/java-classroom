<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

require_once 'config.php';

$data = json_decode(file_get_contents("php://input"));

if(isset($data->name) && isset($data->email) && isset($data->password)) {
    $name = $conn->real_escape_string($data->name);
    $email = $conn->real_escape_string($data->email);
    $password = $data->password; 
    $role = isset($data->role) ? $conn->real_escape_string($data->role) : 'student';

    // Check if user exists
    $check = $conn->prepare("SELECT id FROM users WHERE email = ?");
    $check->bind_param("s", $email);
    $check->execute();
    $result = $check->get_result();
    
    if($result->num_rows > 0) {
        echo json_encode(["success" => false, "message" => "User already exists!"]);
        exit();
    }
    $check->close();

    // Insert User
    $stmt = $conn->prepare("INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)");
    $stmt->bind_param("ssss", $name, $email, $password, $role);
    
    if($stmt->execute()) {
        echo json_encode([
            "success" => true, 
            "message" => "Signup successful!",
            "name" => $name,
            "role" => $role
        ]);
    } else {
        echo json_encode(["success" => false, "message" => "Database Error: " . $stmt->error]);
    }
    $stmt->close();
} else {
    echo json_encode(["success" => false, "message" => "All fields are required"]);
}
$conn->close();
?>
