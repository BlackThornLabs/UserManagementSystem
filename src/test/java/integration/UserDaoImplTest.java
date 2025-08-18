package integration;

import com.github.BlackThornLabs.dao.UserDao;
import com.github.BlackThornLabs.dao.UserDaoImpl;
import com.github.BlackThornLabs.model.User;
import com.github.BlackThornLabs.util.HibernateUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static integration.TestDataUtil.*;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserDaoImplTest {
    @SuppressWarnings("resource")
    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private UserDao userDao;

    @BeforeAll
    static void beforeAll() {
        System.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        System.setProperty("hibernate.connection.username", postgres.getUsername());
        System.setProperty("hibernate.connection.password", postgres.getPassword());
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl();
        try (var session = HibernateUtil.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            session.createMutationQuery("DELETE FROM User").executeUpdate();
            transaction.commit();
        }
    }

    @AfterAll
    static void afterAll() {
        HibernateUtil.shutdown();
    }

    @Test
    void shouldSaveAndFindUserById() {
        User user = createTestUserA();

        userDao.save(user);
        Optional<User> foundUser = userDao.findById(user.getId());

        assertTrue(foundUser.isPresent());
        assertAll("Проверка всех полей пользователя",
                () -> assertEquals("James Gosling", foundUser.get().getName()),
                () -> assertEquals("java@rules.com", foundUser.get().getEmail()),
                () -> assertEquals(70, foundUser.get().getAge()),
                () -> assertThat(foundUser.get().getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now())
        );


    }

    @Test
    void shouldReturnEmptyOptionalWhenUserNotFound() {
        Optional<User> foundUser = userDao.findById(999L);
        assertThat(foundUser).isEmpty();
    }

    @Test
    void shouldFindAllUsers() {
        userDao.save(createTestUserB());
        userDao.save(createTestUserC());

        List<User> testUsers = userDao.findAll();
        assertThat(testUsers).hasSize(2);
        assertThat(testUsers).extracting(User::getName).containsExactly("Ada Lovelace", "Alfonso John Romero");
    }

    @Test
    void shouldUpdateUser() {
        User userToUpdate = createTestUserC();
        userDao.save(userToUpdate);

        userToUpdate.setName("John Romero");
        userToUpdate.setEmail("free@guy.com");
        userToUpdate.setAge(57);
        userDao.update(userToUpdate);

        Optional<User> updatedUser = userDao.findById(userToUpdate.getId());
        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getName()).isEqualTo("John Romero");
        assertThat(updatedUser.get().getEmail()).isEqualTo("free@guy.com");
        assertThat(updatedUser.get().getAge()).isEqualTo(57);
    }

    @Test
    void shouldDeleteUser() {
        User userToDelete = new User("To Delete", "delete@example.com", 50);
        userDao.save(userToDelete);
        assertThat(userDao.findById(userToDelete.getId())).isPresent();

        userDao.delete(userToDelete.getId());
        assertThat(userDao.findById(userToDelete.getId())).isEmpty();
    }
}
