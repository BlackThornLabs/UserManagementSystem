package com.github.BlackThornLabs;

import com.github.BlackThornLabs.dto.UserRequest;
import com.github.BlackThornLabs.dto.UserResponse;
import com.github.BlackThornLabs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
@Profile("default")
@RequiredArgsConstructor
public class ConsoleRunner implements CommandLineRunner {
    private final UserService userService;
    private final Scanner scanner = new Scanner(System.in);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void run(String... args) {
        System.out.println("===...powered by Spring Boot and Alyosha ===");

        boolean running = true;

        while (running) {
            printMenu();
            int choice = getIntInput("Выберите команду: ");
            scanner.nextLine();

            switch (choice) {
                case 1 -> createUser();
                case 2 -> getUserById();
                case 3 -> getUserByEmail();
                case 4 -> getAllUsers();
                case 5 -> updateUser();
                case 6 -> deleteUser();
                case 7 -> running = false;
                default -> System.out.println("Недопустимый параметр! Пожалуйста, попробуйте снова.");
            }
        }
        System.out.println("Выход из приложения...");
        scanner.close();
    }

    private void printMenu() {
        System.out.println("""
        === User Service Основное Меню ==="
        1. Создать запись
        2. Найти пользователя по ID
        3. Найти пользователя по email
        4. Все пользователи
        5. Обновить запись
        6. Удалить запись
        7. Выход
        """
        );
    }

    private int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Пожалуйста, введите допустимый номер: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private void createUser() {
        System.out.println("\n=== Создать нового пользователя ===");

        System.out.print("Введите имя: ");
        String name = scanner.nextLine();

        System.out.print("Введите email: ");
        String email = scanner.nextLine();

        int age = getIntInput("Введите возраст: ");
        scanner.nextLine();

        UserRequest request = new UserRequest();
        request.setName(name);
        request.setEmail(email);
        request.setAge(age);

        try {
            UserResponse response = userService.createUser(request);
            System.out.println("Пользователь успешно создан с ID: " + response.getId());
        } catch (Exception e) {
            System.err.println("Ошибка создания записи: " + e.getMessage());
        }
    }

    private void getUserById() {
        System.out.println("\n=== Найти пользователя по ID ===");
        long id = getIntInput("Введите ID пользователя: ");
        scanner.nextLine();

        try {
            Optional<UserResponse> user = userService.getUserById(id);
            if (user.isPresent()) {
                printUserDetails(user.get());
            } else {
                System.out.println("Не найден пользователь с ID: " + id);
            }
        } catch (Exception e) {
            System.err.println("Ошибка при поиске пользователя: " + e.getMessage());
        }
    }

    private void getUserByEmail() {
        System.out.println("\n=== Найти пользователя по email ===");
        System.out.println("Введите ID пользователя: ");
        String email = scanner.nextLine();
        try {
            Optional<UserResponse> user = userService.getUserByEmail(email);
            if (user.isPresent()) {
                printUserDetails(user.get());
            } else {
                System.out.println("Не найден пользователь с email: " + email);
            }
        } catch (Exception e) {
            System.err.println("Ошибка при поиске пользователя: " + e.getMessage());
        }
    }

    private void getAllUsers() {
        System.out.println("\n=== Все пользователи ===");
        try {
            var users = userService.getAllUsers();
            if (users.isEmpty()) {
                System.out.println("Пользователей не найдено.");
            } else {
                users.forEach(this::printUserDetails);
            }
        } catch (Exception e) {
            System.err.println("Ошибка при поиске пользователей: " + e.getMessage());
        }
    }

    private void updateUser() {
        System.out.println("\n=== Обновить запись ===");
        long id = getIntInput("Введите ID пользователя для обновления: ");
        scanner.nextLine();

        try {
            Optional<UserResponse> currentUser = userService.getUserById(id);
            if (currentUser.isEmpty()) {
                System.out.println("Не найден пользователь с ID: " + id);
                return;
            }
            //Вывод в консоль текущих данных пользователя перед внесением изменений
            System.out.print("Данные текущего пользователя: ");
            printUserDetails(currentUser.get());

            UserRequest request = new UserRequest();

            System.out.print("Введите новое имя (оставьте пустым, чтобы сохранить текущее): ");
            String name = scanner.nextLine();
            request.setName(name.isEmpty()? currentUser.get().getName() : name);

            System.out.print("Введите новый email (оставьте пустым, чтобы сохранить текущий): ");
            String email = scanner.nextLine();
            request.setEmail(email.isEmpty()? currentUser.get().getEmail() : email);

            System.out.print("Введите новый возраст (оставьте пустым, чтобы сохранить текущий): ");
            String ageInput = scanner.nextLine();
            if (!ageInput.isEmpty()) {
                try {
                    int age = Integer.parseInt(ageInput);
                    request.setAge(age);
                } catch (NumberFormatException e) {
                    System.out.println("Неверный формат возраста, оставляем текущий");
                }
            }

            Optional<UserResponse> updatedUser = userService.updateUser(id, request);
            if (updatedUser.isPresent()) {
                System.out.println("Пользователь успешно обновлён!");
            } else System.out.println("Пользователь не найден в процессе обновления.");
        } catch (Exception e) {
            System.err.println("Ошибка при обновлении пользователя: " + e.getMessage());
        }
    }

    private void deleteUser() {
        System.out.println("\n=== Удалить запись ===");
        long id = getIntInput("Введите ID пользователя для удаления: ");
        scanner.nextLine();
        try {
            boolean deleted = userService.deleteUser(id);
            if (deleted) {
                System.out.println("Пользователь с id: " + id + "успешно удалён!");
            } else {
                System.out.println("Не найден пользователь с ID: " + id);
            }
        } catch (Exception e) {
            System.err.println("Ошибка удаления пользователя: " + e.getMessage());
        }
    }

    private void printUserDetails(UserResponse user) {
        System.out.println("\nID: " + user.getId());
        System.out.println("Имя: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Возраст: " + user.getAge());
        System.out.println("Запись создана: " + user.getCreatedAt().format(formatter));
        System.out.println("---------------------");
    }
}