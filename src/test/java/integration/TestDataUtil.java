package integration;

import com.github.BlackThornLabs.model.User;

/*
* Класс для инициализации тестовых данных User
*/
public class TestDataUtil {

    public static User createTestUserA() {
        return new User("James Gosling", "java@rules.com", 70);
    }
    public static User createTestUserB() {
        return new User("Ada Lovelace", "ada@forever", 36);
    }
    public static User createTestUserC() {
        return new User("Alfonso John Romero", "doom@guy.com", 32);
    }

}
