package com.java.bus;

import java.sql.*;
import java.util.Scanner;

// Database utility class for managing connections
class DatabaseUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/bus_reservation";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

// User entity class
class User {
    private int userId;
    private String username;
    private String password;
    private String email;

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

// DAO class for user-related database operations
class UserDAO {
    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public void createUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.executeUpdate();
        }
    }

    public User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setEmail(rs.getString("email"));
                    return user;
                }
            }
        }
        return null;
    }
}

// Service class for user-related business logic
class UserService {
    private UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void registerUser(String username, String password, String email) throws SQLException {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        userDAO.createUser(user);
    }

    public User loginUser(String username, String password) throws SQLException {
        User user = userDAO.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}

// Main class for running the program
public class BusReservation {
    public static void main(String[] args) {
        try (Connection connection = DatabaseUtil.getConnection()) {
            UserDAO userDAO = new UserDAO(connection);
            UserService userService = new UserService(userDAO);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                int option = scanner.nextInt();
                scanner.nextLine(); // consume newline

                if (option == 1) {
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();
                    userService.registerUser(username, password, email);
                    System.out.println("Registration successful!");
                } else if (option == 2) {
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    User user = userService.loginUser(username, password);
                    if (user != null) {
                        System.out.println("Login successful! Welcome, " + user.getUsername());
                    } else {
                        System.out.println("Login failed. Invalid credentials.");
                    }
                } else if (option == 3) {
                    break;
                } else {
                    System.out.println("Invalid option. Please try again.");
                }
            }
            scanner.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
