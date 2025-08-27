package com.github.BlackThornLabs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) {
        // Получаем контекст
        ConfigurableApplicationContext context = SpringApplication.run(MainApplication.class, args);

        // Получаем бин Console из контекста (если активен профиль "console")
        try {
            Console console = context.getBean(Console.class);
            console.start(args);
        } catch (Exception e) {
            // Если бина Console нет, логгируем и даем приложению работать как web-сервер
            System.out.println("Профиль console не активен. Запускаем веб-приложение...");
        }
    }
}

