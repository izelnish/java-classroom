package com.javaclassroom;

import org.springframework.web.bind.annotation.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allow requests from our HTML file
public class UserController {

    // Reusing our raw JDBC logic for simplicity
    private Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/java_classroom", "root", "");
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        String email = payload.get("email");
        // In a real app, never store passwords as plain text!
        String password = payload.get("password");

        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM users WHERE email = ? AND password = ?"; // Basic check
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                response.put("success", true);
                response.put("message", "Login Successful!");
                response.put("role", rs.getString("role")); // Assuming 'role' column exists
                response.put("name", rs.getString("name")); // Assuming 'name' column exists
            } else {
                response.put("success", false);
                response.put("message", "Invalid email or password");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Database Error: " + e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    @GetMapping("/test")
    public String test() {
        return "Backend is running!";
    }

    @PostMapping("/signup")
    public Map<String, Object> signup(@RequestBody Map<String, String> payload) {
        String name = payload.get("name");
        String email = payload.get("email");
        String password = payload.get("password");
        String role = payload.get("role");

        Map<String, Object> response = new HashMap<>();

        try (Connection conn = getConnection()) {
            // Check if user exists
            String checkSql = "SELECT count(*) FROM users WHERE email = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, email);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    response.put("success", false);
                    response.put("message", "User already exists!");
                    return response;
                }
            }

            // Insert new user
            String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, password);
                stmt.setString(4, role);
                stmt.executeUpdate();
            }

            response.put("success", true);
            response.put("message", "Signup successful!");
            response.put("name", name);
            response.put("role", role);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Database error: " + e.getMessage());
        }
        return response;
    }

    @PostMapping("/chat")
    public Map<String, String> chat(@RequestBody Map<String, String> payload) {
        String userMessage = payload.get("message");
        // SECURITY UPDATE: Keys are removed for GitHub publishing.
        String apiKey = "YOUR_OPENAI_API_KEY";
        String aiReply = "";

        if (apiKey.equals("YOUR_OPENAI_API_KEY") || !apiKey.startsWith("sk-")) {
            return Map.of("reply", "⚠️ API Key missing! Open UserController.java and paste your OpenAI Key.");
        }

        try {
            String apiUrl = "https://api.openai.com/v1/chat/completions";
            String jsonBody = "{ \"model\": \"gpt-3.5-turbo\", \"messages\": [{ \"role\": \"user\", \"content\": \"" +
                    escapeJson(userMessage) +
                    "\" }] }";

            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create(apiUrl))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(java.net.http.HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            java.net.http.HttpResponse<String> response = client.send(request,
                    java.net.http.HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String body = response.body();
                int contentIndex = body.indexOf("\"content\": \"");
                if (contentIndex != -1) {
                    int start = contentIndex + 11;
                    int end = body.indexOf("\"", start);
                    while (end != -1 && body.charAt(end - 1) == '\\') {
                        end = body.indexOf("\"", end + 1);
                    }
                    if (end != -1) {
                        aiReply = body.substring(start, end).replace("\\n", "\n").replace("\\\"", "\"");
                    }
                }
            } else {
                System.out.println("AI Error " + response.statusCode() + ": " + response.body());
                aiReply = getFallbackResponse(userMessage) + " (AI Offline)";
            }

        } catch (Exception e) {
            e.printStackTrace();
            aiReply = getFallbackResponse(userMessage) + " (Backend Error)";
        }

        Map<String, String> result = new HashMap<>();
        result.put("reply", aiReply);
        return result;
    }

    private String getFallbackResponse(String input) {
        if (input == null)
            return "Please say something!";
        input = input.toLowerCase();

        if (input.contains("hello") || input.contains("hi") || input.contains("hey"))
            return "Hello! I am the Java Classroom Assistant (Offline Mode). Ask me about Java concepts!";

        // Java Basics
        if (input.contains("java"))
            return "Java is a popular, high-level, class-based, object-oriented programming language designed to have as few implementation dependencies as possible.";
        if (input.contains("jvm"))
            return "JVM (Java Virtual Machine) is the engine that drives the Java code. It converts Java bytecode into machine language.";
        if (input.contains("jdk"))
            return "JDK (Java Development Kit) is a software development environment used for developing Java applications. It includes JRE and development tools.";
        if (input.contains("jre"))
            return "JRE (Java Runtime Environment) provides the libraries, the Java Virtual Machine, and other components to run applets and applications written in the Java programming language.";

        // OOP Concepts
        if (input.contains("class"))
            return "A class is a blueprint or template for creating objects. It defines the properties and behaviors that the objects will have.";
        if (input.contains("object"))
            return "An object is an instance of a class. It has state (fields) and behavior (methods).";
        if (input.contains("inheritance"))
            return "Inheritance is a mechanism where one class acquires the properties and behaviors of a parent class. It uses the 'extends' keyword.";
        if (input.contains("polymorphism"))
            return "Polymorphism allows objects to be treated as instances of their parent class. It basically means 'many forms'.";
        if (input.contains("encapsulation"))
            return "Encapsulation is the wrapping of data (variables) and code acting on the data (methods) together as a single unit, usually by making variables private and providing public getter/setter methods.";
        if (input.contains("abstraction"))
            return "Abstraction is the process of hiding the implementation details and showing only functionality to the user. Abstract classes and Interfaces are used for this.";
        if (input.contains("interface"))
            return "An interface in Java is a reference type, similar to a class, that can contain only constants, method signatures, default methods, static methods, and nested types. It is a contract for what a class can do.";

        // Syntax
        if (input.contains("variable"))
            return "Variables are containers for storing data values (like int, String, boolean).";
        if (input.contains("loop"))
            return "Loops (for, while, do-while) are used to execute a block of code repeatedly until a specific condition is met.";
        if (input.contains("if") || input.contains("else"))
            return "Conditional statements (if-else) let you execute different code blocks based on boolean conditions.";
        if (input.contains("method") || input.contains("function"))
            return "A method is a block of code which only runs when it is called. You can pass data, known as parameters, into a method.";
        if (input.contains("constructor"))
            return "A constructor is a special method that is used to initialize objects. The constructor is called when an object of a class is created.";
        if (input.contains("static"))
            return "The static keyword means that the variable or method belongs to the class, rather than to a specific instance of the class.";
        if (input.contains("final"))
            return "The final keyword is a non-access modifier used for classes, attributes and methods, which makes them non-changeable (impossible to inherit or override).";

        // General
        if (input.contains("error") || input.contains("exception"))
            return "Exceptions are events that occur during the execution of programs that disrupt the normal flow of instructions. Try handling them with try-catch blocks.";
        if (input.contains("thank"))
            return "You're welcome! Happy coding!";
        if (input.contains("help"))
            return "I can define basic Java terms for you like: Class, Object, Inheritance, Polymorphism, Loops, Variables, etc.";

        return "I am currently in Offline Mode (OpenAI Quota Exceeded). I can only answer basic definitions about Java keywords (e.g., 'What is a class?', 'Define inheritance').";
    }

    // Helper to prevent JSON breaking
    private String escapeJson(String input) {
        if (input == null)
            return "";
        return input.replace("\"", "\\\"").replace("\n", " ");
    }
}
