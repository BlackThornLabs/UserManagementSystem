package com.github.BlackThornLabs;

import com.github.BlackThornLabs.dao.UserDao;
import com.github.BlackThornLabs.dao.UserDaoImpl;
import com.github.BlackThornLabs.model.User;
import com.github.BlackThornLabs.util.HibernateUtil;

import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class Main {
    private static final UserDao userDao = new UserDaoImpl();
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            printMenu();
            int choice = getIntInput("Choose an option: ");
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    createUser();
                    break;
                case 2:
                    getUserById();
                    break;
                case 3:
                    getAllUsers();
                    break;
                case 4:
                    updateUser();
                    break;
                case 5:
                    deleteUser();
                    break;
                case 6:
                    findUserByEmail();
                    break;
                case 7:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }

        System.out.println("Exiting application...");
        HibernateUtil.shutdown();
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n=== User Management System ===");
        System.out.println("1. Create User");
        System.out.println("2. Get User by ID");
        System.out.println("3. Get All Users");
        System.out.println("4. Update User");
        System.out.println("5. Delete User");
        System.out.println("6. Find User by Email");
        System.out.println("7. Exit");
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void createUser() {
        System.out.println("\n=== Create New User ===");
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        int age = getIntInput("Enter age: ");
        scanner.nextLine(); // consume newline

        User user = new User(name, email, age);
        try {
            userDao.save(user);
            System.out.println("User created successfully with ID: " + user.getId());
        } catch (Exception e) {
            System.err.println("Error creating user: " + e.getMessage());
        }
    }

    private static void getUserById() {
        System.out.println("\n=== Get User by ID ===");
        long id = getIntInput("Enter user ID: ");
        scanner.nextLine(); // consume newline

        try {
            Optional<User> user = userDao.findById(id);
            if (user.isPresent()) {
                printUserDetails(user.get());
            } else {
                System.out.println("User not found with ID: " + id);
            }
        } catch (Exception e) {
            System.err.println("Error retrieving user: " + e.getMessage());
        }
    }

    private static void getAllUsers() {
        System.out.println("\n=== All Users ===");
        try {
            List<User> users = userDao.findAll();
            if (users.isEmpty()) {
                System.out.println("No users found.");
            } else {
                users.forEach(Main::printUserDetails);
            }
        } catch (Exception e) {
            System.err.println("Error retrieving users: " + e.getMessage());
        }
    }

    private static void updateUser() {
        System.out.println("\n=== Update User ===");
        long id = getIntInput("Enter user ID to update: ");
        scanner.nextLine(); // consume newline

        try {
            Optional<User> userOpt = userDao.findById(id);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                System.out.println("Current user details:");
                printUserDetails(user);

                System.out.print("Enter new name (leave blank to keep current): ");
                String name = scanner.nextLine();
                if (!name.isEmpty()) {
                    user.setName(name);
                }

                System.out.print("Enter new email (leave blank to keep current): ");
                String email = scanner.nextLine();
                if (!email.isEmpty()) {
                    user.setEmail(email);
                }

                System.out.print("Enter new age (0 to keep current): ");
                int age = getIntInput("");
                scanner.nextLine(); // consume newline
                if (age > 0) {
                    user.setAge(age);
                }

                userDao.update(user);
                System.out.println("User updated successfully!");
            } else {
                System.out.println("User not found with ID: " + id);
            }
        } catch (Exception e) {
            System.err.println("Error updating user: " + e.getMessage());
        }
    }

    private static void deleteUser() {
        System.out.println("\n=== Delete User ===");
        long id = getIntInput("Enter user ID to delete: ");
        scanner.nextLine(); // consume newline

        try {
            Optional<User> userOpt = userDao.findById(id);
            if (userOpt.isPresent()) {
                userDao.deleteById(id);
                System.out.println("User deleted successfully!");
            } else {
                System.out.println("User not found with ID: " + id);
            }
        } catch (Exception e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
    }

    private static void findUserByEmail() {
        System.out.println("\n=== Find User by Email ===");
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        try {
            List<User> users = userDao.findByEmail(email);
            if (users.isEmpty()) {
                System.out.println("No users found with email: " + email);
            } else {
                users.forEach(Main::printUserDetails);
            }
        } catch (Exception e) {
            System.err.println("Error finding users: " + e.getMessage());
        }
    }

    private static void printUserDetails(User user) {
        System.out.println("\nUser ID: " + user.getId());
        System.out.println("Name: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Age: " + user.getAge());
        System.out.println("Created At: " + user.getCreatedAt().format(formatter));
        System.out.println("---------------------");
    }
}