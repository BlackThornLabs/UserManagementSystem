# 🚀 User Management System

Консольное приложение для управления пользователями с использованием **Spring Boot 3**, **Spring Data JPA** и **PostgreSQL**.  
Реализованы базовые CRUD-операции (Create, Read, Update, Delete).

## 📦 Технологии
- **Java 17**
- **Spring Boot**
- **PostgreSQL**
- **Lombok**
- **Maven** (сборка)
- **Log4j2** (логирование)

## 🧪 Тестирование

- **JUnit 5**
- **Testcontainers** (интеграционные тесты с PostgreSQL)
- **Mockito** (мокирование зависимостей)

## ✅ Требования
- **Установленная Java 17+**
- **Сервер PostgreSQL (версия 12+)**
- **Maven для сборки**
- **Docker (для Testcontainers)**

## ⚙️ Настройка
1. Создайте базу данных в PostgreSQL:
   CREATE DATABASE user_service;
2. Настройте подключение к БД в src/main/resources/application.properties:


   **spring.datasource.url=jdbc:postgresql://localhost:5432/user_service**
   **spring.datasource.username=your_username**
   **spring.datasource.password=your_password**
   **spring.jpa.hibernate.ddl-auto=update**

