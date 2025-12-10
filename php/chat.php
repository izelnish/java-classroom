<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

$data = json_decode(file_get_contents("php://input"));
$input = isset($data->message) ? strtolower($data->message) : "";

// Fallback response logic (Offline mode equivalent)
$reply = "I am a basic PHP Bot. Ask me about Java basics!";

if (strpos($input, 'hello') !== false || strpos($input, 'hi') !== false) {
    $reply = "Hello! I am your Java Tutor (hosted on PHP). How can I help?";
} elseif (strpos($input, 'java') !== false) {
    $reply = "Java is a class-based, object-oriented programming language designed to have as few implementation dependencies as possible.";
} elseif (strpos($input, 'class') !== false) {
    $reply = "A class is a blueprint for creating objects (a particular data structure), providing initial values for state and implementations of behavior.";
} elseif (strpos($input, 'object') !== false) {
    $reply = "An object is an instance of a class. It has states and behaviors.";
} elseif (strpos($input, 'variable') !== false) {
    $reply = "Variables are containers for storing data values.";
} elseif (strpos($input, 'loop') !== false) {
    $reply = "Loops are used to execute a block of code repeatedly.";
}

echo json_encode(["reply" => $reply]);
?>
