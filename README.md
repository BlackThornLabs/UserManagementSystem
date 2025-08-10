# 🚀 User Management System

Консольное приложение для управления пользователями с использованием **Hibernate 6** и **PostgreSQL**.  
Реализованы базовые CRUD-операции (Create, Read, Update, Delete).

## 📦 Технологии
- **Java 17**
- **Hibernate ORM 6**
- **PostgreSQL**
- **Maven** (сборка)
- **Log4j2** (логирование)

## ✅ Требования
- **Установленная Java 17+**
- **Сервер PostgreSQL (версия 12+)**
- **Maven для сборки**

## ⚙️ Настройка
1. Создайте базу данных в PostgreSQL:
   CREATE DATABASE user_service;
2. Настройте подключение к БД:
Отредактируйте файл src/main/resources/config.properties:

   *db.url=jdbc:postgresql://localhost:5432/user_service*
   *db.username=your_username*
   *db.password=your_password*

