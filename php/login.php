<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

require_once 'config.php';

// Get JSON input
$data = json_decode(file_get_contents("php://input"));

if(isset($data->email) && isset($data->password)) {
    $email = $conn->real_escape_string($data->email);
    $password = $data->password; // In a real app, verify hash

    // Prepare statement
    $stmt = $conn->prepare("SELECT name, role FROM users WHERE email = ? AND password = ?");
    $stmt->bind_param("ss", $email, $password);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        $row = $result->fetch_assoc();
        echo json_encode([
            "success" => true, 
            "message" => "Login Successful!",
            "role" => $row['role'],
            "name" => $row['name']
        ]);
    } else {
        echo json_encode(["success" => false, "message" => "Invalid email or password"]);
    }
    $stmt->close();
} else {
    echo json_encode(["success" => false, "message" => "Invalid input data"]);
}
$conn->close();
?>
