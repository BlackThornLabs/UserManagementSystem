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

        while (true) {
            printMenu();
            int choice = getIntInput("Выберите команду: ");
            scanner.nextLine();

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
                    System.out.println("Выход из приложения...");
                    scanner.close();
                    HibernateUtil.shutdown();
                    return;
                default:
                    System.out.println("Недопустимый параметр! Пожалуйста, попробуйте снова.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("""
        === User Management System ==="
        1. Создать запись
        2. Найти пользователя по ID
        3. Все пользователи
        4. Обновить запись
        5. Удалить запись
        6. Выход
        """
        );
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Пожалуйста, введите допустимый номер: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void createUser() {
        System.out.println("\n=== Создать нового пользователя ===");
        System.out.print("Введите имя: ");
        String name = scanner.nextLine();

        System.out.print("Введите email: ");
        String email = scanner.nextLine();

        int age = getIntInput("Введите возраст: ");
        scanner.nextLine(); // consume newline

        User user = new User(name, email, age);
        try {
            userDao.save(user);
            System.out.println("Пользователь успешно создан с ID: " + user.getId());
        } catch (Exception e) {
            System.err.println("Ошибка создания записи: " + e.getMessage());
        }
    }

    private static void getUserById() {
        System.out.println("\n=== Найти пользователя по ID ===");
        long id = getIntInput("Введите ID пользователя: ");
        scanner.nextLine(); // consume newline

        try {
            Optional<User> user = userDao.findById(id);
            if (user.isPresent()) {
                printUserDetails(user.get());
            } else {
                System.out.println("Не найден пользователь с ID: " + id);
            }
        } catch (Exception e) {
            System.err.println("Ошибка при поиске пользователя: " + e.getMessage());
        }
    }

    private static void getAllUsers() {
        System.out.println("\n=== Все пользователи ===");
        try {
            List<User> users = userDao.findAll();
            if (users.isEmpty()) {
                System.out.println("Пользователей не найдено.");
            } else {
                users.forEach(Main::printUserDetails);
            }
        } catch (Exception e) {
            System.err.println("Ошибка при поиске пользователей: " + e.getMessage());
        }
    }

    private static void updateUser() {
        System.out.println("\n=== Обновить запись ===");
        long id = getIntInput("Введите ID пользователя для обновления: ");
        scanner.nextLine();

        try {
            Optional<User> userOpt = userDao.findById(id);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                System.out.println("Детали текущей записи:");
                printUserDetails(user);

                System.out.print("Введите новое имя (оставьте пустым, чтобы сохранить текущее): ");
                String name = scanner.nextLine();
                if (!name.isEmpty()) {
                    user.setName(name);
                }

                System.out.print("Введите новый email (оставьте пустым, чтобы сохранить текущее): ");
                String email = scanner.nextLine();
                if (!email.isEmpty()) {
                    user.setEmail(email);
                }

                System.out.print("Введите новый возраст (0 чтобы сохранить текущий): ");
                int age = getIntInput("");
                scanner.nextLine();
                if (age > 0) {
                    user.setAge(age);
                }

                userDao.update(user);
                System.out.println("Пользователь обновлён успешно!");
            } else {
                System.out.println("Не найден пользователь с ID: " + id);
            }
        } catch (Exception e) {
            System.err.println("Ошибка при обновлении пользователя: " + e.getMessage());
        }
    }

    private static void deleteUser() {
        System.out.println("\n=== Удалить запись ===");
        long id = getIntInput("Введите ID пользователя для удаления: ");
        scanner.nextLine();

        try {
            Optional<User> userOpt = userDao.findById(id);
            if (userOpt.isPresent()) {
                userDao.delete(id);
                System.out.println("Пользователь успешно удалён!");
            } else {
                System.out.println("Не найден пользователь с ID: " + id);
            }
        } catch (Exception e) {
            System.err.println("Ошибка удаления пользователя: " + e.getMessage());
        }
    }

    private static void printUserDetails(User user) {
        System.out.println("\nID: " + user.getId());
        System.out.println("Имя: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Возраст: " + user.getAge());
        System.out.println("Запись создана: " + user.getCreatedAt().format(formatter));
        System.out.println("---------------------");
    }
}